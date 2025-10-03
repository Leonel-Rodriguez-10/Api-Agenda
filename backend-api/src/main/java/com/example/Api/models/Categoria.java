// src/main/java/com/example/Api/models/Categoria.java
package com.example.Api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
// Evita que Jackson intente serializar el proxy de Hibernate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    public Categoria() {}

    // Útil si quieres construir solo con id (por ejemplo al recibir JSON)
    public Categoria(Long id) { this.id = id; }

    public Categoria(String nombre) { this.nombre = nombre; }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
