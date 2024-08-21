package com.example.practica20240813.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.practica20240813.modelos.TelefonosAlumno;

public interface ITelefonoAlumnoRepository extends JpaRepository<TelefonosAlumno, Long> {
}
