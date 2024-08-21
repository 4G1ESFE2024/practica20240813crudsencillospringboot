package com.example.practica20240813.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.practica20240813.modelos.Alumno;

public interface IAlumnoRepository extends JpaRepository<Alumno, Long> {
    
}