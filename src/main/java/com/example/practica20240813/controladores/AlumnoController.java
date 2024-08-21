package com.example.practica20240813.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.practica20240813.modelos.Alumno;
import com.example.practica20240813.modelos.TelefonosAlumno;
import com.example.practica20240813.servicios.interfaces.IAlumnoService;

@Controller
@RequestMapping("/alumnos")
public class AlumnoController {
    @Autowired
    private IAlumnoService alumnoService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // si no está seteado se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Alumno> alumnos = alumnoService.buscarTodosPaginados(pageable);
        model.addAttribute("alumnos", alumnos);

        int totalPages = alumnos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "alumno/index";
    }

    @GetMapping("/create")
    public String create(Alumno alumno, Model model) {

        /*
         * if(alumno.getTelefonos()==null)
         * alumno.setTelefonos(new ArrayList<>());
         * TelefonosAlumno telefonoInicial= new TelefonosAlumno(alumno, "");
         * telefonoInicial.setId(-1l);
         * alumno.getTelefonos().add(telefonoInicial);
         */
        model.addAttribute(alumno);
        return "alumno/create";
    }

    @PostMapping("/addtelefonos")
    public String addPhone(Alumno alumno, Model model) {
        if (alumno.getTelefonos() == null)
            alumno.setTelefonos(new ArrayList<>());
        alumno.getTelefonos().add(new TelefonosAlumno(alumno, ""));

        if (alumno.getTelefonos() != null) {
            Long idDet = 0l;
            for (TelefonosAlumno item : alumno.getTelefonos()) {
                if (item.getId() == null || item.getId() < 1) {
                    idDet--;
                    item.setId(idDet);
                }
            }
        }

        model.addAttribute(alumno);
        if (alumno.getId() != null && alumno.getId() > 0)
            return "alumno/edit";
        else
            return "alumno/create";
    }

    @PostMapping("/deltelefonos/{id}")
    public String delPhone(@PathVariable("id") Long id, Alumno alumno, Model model) {
        alumno.getTelefonos().removeIf(elemento -> elemento.getId() == id);
        model.addAttribute(alumno);
        if (alumno.getId() != null && alumno.getId() > 0)
            return "alumno/edit";
        else
            return "alumno/create";
    }

    @PostMapping("/save")
    public String save(Alumno alumno, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute(alumno);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "alumno/create";
        }
        if (alumno.getTelefonos() != null) {
            for (TelefonosAlumno item : alumno.getTelefonos()) {
                if (item.getId() != null && item.getId() < 0)
                    item.setId(null);
                item.setAlumno(alumno);
            }
        }        
        if (alumno.getId() != null && alumno.getId() > 0) {
            // funcionalidad para cuando es modificar un registro
            Alumno alumnoUpdate = alumnoService.buscarPorId(alumno.getId()).get();
            // almacenar en un dicionario los telefono que estan
            // guardados en la base de datos para mejor acceso a ellos
            Map<Long, TelefonosAlumno> telefonosData = new HashMap<>();
            if (alumnoUpdate.getTelefonos() != null) {
                for (TelefonosAlumno item : alumnoUpdate.getTelefonos()) {
                    telefonosData.put(item.getId(), item);
                }               
            }
            // actualizar los registro que viene de la vista hacia el que se encuentra por id
            alumnoUpdate.setNombre(alumno.getNombre());
            alumnoUpdate.setApellido(alumno.getApellido());
            // recorrer los telefonos obtenidos desde la vista y actualizar 
            // alumnoiUpdate para que implemente los cambios
            if (alumno.getTelefonos() != null) {
                for (TelefonosAlumno item : alumno.getTelefonos()) {
                    if (item.getId() == null) {
                        if (alumnoUpdate.getTelefonos() == null)
                            alumnoUpdate.setTelefonos(new ArrayList<>());
                        item.setAlumno(alumnoUpdate);
                        alumnoUpdate.getTelefonos().add(item);
                    }
                    else{
                        if(telefonosData.containsKey(item.getId())){
                            TelefonosAlumno telefonoUpdate= telefonosData.get(item.getId());
                            // actualizar las propiedades de TelefonosAlumno
                            // si ya existe en la base de datos
                            telefonoUpdate.setTelefono(item.getTelefono());
                            // remover del dicionario los telefonos datas para 
                            // saber que cuales se van eliminar que serian
                            // todos aquellos que no se lograron remove porque no viene desde 
                            // la vista
                            telefonosData.remove(item.getId());
                        }
                    }
                }
            }
            if(telefonosData.isEmpty()==false){
                for (Map.Entry<Long, TelefonosAlumno> item : telefonosData.entrySet()) {
                    alumnoUpdate.getTelefonos().removeIf(elemento -> elemento.getId() ==item.getKey() );                                     
                }
                
            }
            alumno = alumnoUpdate;
        }
        alumnoService.crearOEditar(alumno);
        attributes.addFlashAttribute("msg", "Alumno creado correctamente");
        return "redirect:/alumnos";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Alumno alumno = alumnoService.buscarPorId(id).get();
        model.addAttribute("alumno", alumno);
        return "alumno/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Alumno alumno = alumnoService.buscarPorId(id).get();
        model.addAttribute("alumno", alumno);
        return "alumno/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id, Model model) {
        Alumno alumno = alumnoService.buscarPorId(id).get();
        model.addAttribute("alumno", alumno);
        return "alumno/delete";
    }

    @PostMapping("/delete")
    public String delete(Alumno alumno, RedirectAttributes attributes) {
        alumnoService.eliminarPorId(alumno.getId());
        attributes.addFlashAttribute("msg", "Alumno eliminado correctamente");
        return "redirect:/alumnos";
    }
}
