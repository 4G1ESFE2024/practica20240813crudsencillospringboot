package com.example.practica20240813.servicios.implementaciones;

import com.example.practica20240813.modelos.Aula;
import com.example.practica20240813.repositorios.IAulaRepository;
import com.example.practica20240813.servicios.interfaces.IAulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaService implements IAulaService {
      @Autowired
    private IAulaRepository AulaRepository;

    @Override
    public Page<Aula> buscarTodosPaginados(Pageable pageable) {
        return AulaRepository.findAll(pageable);
    }

    @Override
    public List<Aula> obtenerTodos() {
        return AulaRepository.findAll();
    }

    @Override
    public Optional<Aula> buscarPorId(Short id) {
        return AulaRepository.findById(id);
    }

    @Override
    public Aula crearOEditar(Aula Aula) {
        return AulaRepository.save(Aula);
    }

    @Override
    public void eliminarPorId(Short id) {
        AulaRepository.deleteById(id);
    }
}
