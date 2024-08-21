package com.example.practica20240813.servicios.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.practica20240813.modelos.Alumno;

public interface IAlumnoService {
    Page<Alumno> buscarTodosPaginados(Pageable pageable);

    List<Alumno> obtenerTodos();

    Optional<Alumno> buscarPorId(Long id);

    Alumno crearOEditar(Alumno grupo);
    void eliminarPorId(Long id);
}
