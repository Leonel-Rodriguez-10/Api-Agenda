package com.example.Api.controller;

import com.example.Api.controller.dto.ContactoRequest;
import com.example.Api.models.Categoria;
import com.example.Api.models.Contacto;
import com.example.Api.repository.CategoriaRepository;
import com.example.Api.repository.ContactoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    private final ContactoRepository repo;
    private final CategoriaRepository categoriaRepo;

    public ContactoController(ContactoRepository repo, CategoriaRepository categoriaRepo) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
    }

    // Crear
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contacto crear(@RequestBody ContactoRequest body) {
        Categoria cat = categoriaRepo.findById(body.idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no existe"));
        Contacto c = new Contacto();
        c.setNombre(body.nombre);
        c.setTelefono(body.telefono);
        c.setCategoria(cat);
        return repo.save(c);
    }

    // Listar
    @GetMapping
    public List<Contacto> listar() {
        return repo.findAll();
    }

    // Modificar
    @PutMapping("/{id}")
    public Contacto modificar(@PathVariable Long id, @RequestBody ContactoRequest body) {
        Contacto c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
        Categoria cat = categoriaRepo.findById(body.idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no existe"));
        c.setNombre(body.nombre);
        c.setTelefono(body.telefono);
        c.setCategoria(cat);
        return repo.save(c);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
