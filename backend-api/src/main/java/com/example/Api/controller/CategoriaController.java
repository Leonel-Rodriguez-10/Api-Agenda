package com.example.Api.controller;

import com.example.Api.models.Categoria;
import com.example.Api.repository.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository repo;

    public CategoriaController(CategoriaRepository repo) {
        this.repo = repo;
    }

    // Crear
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crear(@RequestBody Categoria c) {
        return repo.save(c);
    }

    // Listar
    @GetMapping
    public List<Categoria> listar() {
        return repo.findAll();
    }

    // Modificar
    @PutMapping("/{id}")
    public Categoria modificar(@PathVariable Long id, @RequestBody Categoria in) {
        Categoria c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        c.setNombre(in.getNombre());
        return repo.save(c);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
