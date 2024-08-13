package com.example.practica20240813.servicios.implementaciones;

import com.example.practica20240813.modelos.Alumno;
import com.example.practica20240813.repositorios.IAlumnoRepository;
import com.example.practica20240813.servicios.interfaces.IAlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService implements IAlumnoService {
      @Autowired
    private IAlumnoRepository AlumnoRepository;

    @Override
    public Page<Alumno> buscarTodosPaginados(Pageable pageable) {
        return AlumnoRepository.findAll(pageable);
    }

    @Override
    public List<Alumno> obtenerTodos() {
        return AlumnoRepository.findAll();
    }

    @Override
    public Optional<Alumno> buscarPorId(Long id) {
        return AlumnoRepository.findById(id);
    }

    @Override
    public Alumno crearOEditar(Alumno Alumno) {
        return AlumnoRepository.save(Alumno);
    }

    @Override
    public void eliminarPorId(Long id) {
        AlumnoRepository.deleteById(id);
    }
}
