package com.example.practica20240813.servicios.interfaces;

import com.example.practica20240813.modelos.Aula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IAulaService {
    Page<Aula> buscarTodosPaginados(Pageable pageable);

    List<Aula> obtenerTodos();

    Optional<Aula> buscarPorId(Short id);

    Aula crearOEditar(Aula grupo);

    void eliminarPorId(Short id);
}
