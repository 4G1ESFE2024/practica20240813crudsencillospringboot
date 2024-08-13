package com.example.practica20240813.controladores;
import com.example.practica20240813.modelos.Aula;
import com.example.practica20240813.servicios.interfaces.IAulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/aulas")
public class AulaController {
    @Autowired
    private IAulaService aulaService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // si no está seteado se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Aula> aulas = aulaService.buscarTodosPaginados(pageable);
        model.addAttribute("aulas", aulas);

        int totalPages = aulas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "aula/index";
    }

    @GetMapping("/create")
    public String create(Aula aula){
        return "aula/create";
    }

    @PostMapping("/save")
    public String save(Aula aula, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(aula);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "aula/create";
        }

        aulaService.crearOEditar(aula);
        attributes.addFlashAttribute("msg", "Aula creado correctamente");
        return "redirect:/aulas";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Short id, Model model){
        Aula aula = aulaService.buscarPorId(id).get();
        model.addAttribute("aula", aula);
        return "aula/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Short id, Model model){
        Aula aula = aulaService.buscarPorId(id).get();
        model.addAttribute("aula", aula);
        return "aula/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Short id, Model model){
        Aula aula = aulaService.buscarPorId(id).get();
        model.addAttribute("aula", aula);
        return "aula/delete";
    }

    @PostMapping("/delete")
    public String delete(Aula aula, RedirectAttributes attributes){
        aulaService.eliminarPorId(aula.getId());
        attributes.addFlashAttribute("msg", "Aula eliminado correctamente");
        return "redirect:/aulas";
    }
}
