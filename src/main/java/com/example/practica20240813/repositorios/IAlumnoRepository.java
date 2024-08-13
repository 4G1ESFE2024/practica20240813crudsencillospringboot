package com.example.practica20240813.repositorios;

import com.example.practica20240813.modelos.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlumnoRepository extends JpaRepository<Alumno, Long> {
    
}
