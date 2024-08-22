package com.example.practica20240813.modelos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "aulas")
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;
    @NotBlank(message = "El nombre es requerido")
    private String nombre;   
    private String descripcion;
    private String profesor;
    private String urlPerfil;
    public String getUrlPerfil() {
        return urlPerfil;
    }
    public void setUrlPerfil(String urlPerfil) {
        this.urlPerfil = urlPerfil;
    }
   
    public String getProfesor() {
        return profesor;
    }
    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
    public Short getId() {
        return id;
    }
    public void setId(Short id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }   
    
}
