package com.example.practica20240813.controladores;
import java.util.ArrayList;
import java.util.List;
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
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
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
    public String create(Alumno alumno,  Model model){
        List<TelefonosAlumno> telefonos= new ArrayList<>(); 
        telefonos.add(new TelefonosAlumno(alumno,""));
        alumno.setTelefonos(telefonos);      
        model.addAttribute(alumno);
        return "alumno/create";
    }

    @PostMapping("/addtelefonos")
    public String addPhone(Alumno alumno,  Model model){
        alumno.getTelefonos().add(new TelefonosAlumno(alumno,""));      
        model.addAttribute(alumno);
        return "alumno/create";
    }

    @PostMapping("/save")
    public String save(Alumno alumno, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(alumno);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "alumno/create";
        }        
        for (TelefonosAlumno item : alumno.getTelefonos()) {
           item.setAlumno(alumno);
        }
        alumnoService.crearOEditar(alumno);
        attributes.addFlashAttribute("msg", "Alumno creado correctamente");
        return "redirect:/alumnos";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model){
        Alumno alumno = alumnoService.buscarPorId(id).get();
        model.addAttribute("alumno", alumno);
        return "alumno/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Alumno alumno = alumnoService.buscarPorId(id).get();
        model.addAttribute("alumno", alumno);
        return "alumno/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id, Model model){
        Alumno alumno = alumnoService.buscarPorId(id).get();
        model.addAttribute("alumno", alumno);
        return "alumno/delete";
    }

    @PostMapping("/delete")
    public String delete(Alumno alumno, RedirectAttributes attributes){
        alumnoService.eliminarPorId(alumno.getId());
        attributes.addFlashAttribute("msg", "Alumno eliminado correctamente");
        return "redirect:/alumnos";
    }
}
