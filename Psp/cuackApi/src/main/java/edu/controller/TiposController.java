package edu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TiposController {

    @GetMapping("/tipos")
    public String getTiposAsString() {
        // Devuelve un string fijo de ejemplo
        return "Admin, Profesor, Alumno";
    }
}