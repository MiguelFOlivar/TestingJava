package com.mfigueroa.RegistroPersonas.controllers;

import com.mfigueroa.RegistroPersonas.model.Persona;
import com.mfigueroa.RegistroPersonas.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class HelloController {
    @Autowired
    private PersonaService personaService;

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("mensaje", "Bienvenidos a ,,,");
        model.addAttribute("personas", getAllPersonas());
        return "index";
    }

    @GetMapping("/getAll")
    public List<Persona> getAllPersonas() {
       return personaService.getAllPersonas();
        //return ResponseEntity.ok(personas);
    }

    @GetMapping("/getById")
    public ResponseEntity<Persona> getPersonaById(@RequestParam Long id) {
        Optional<Persona> persona = personaService.getPersonaById(id);
        return persona.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/create")
    public String createPersona() {
        //Persona createdPersona = personaService.createPersonas(persona);
        return "formPersona";
    }

    @GetMapping("/update")
    public String updatePersona() {
        return "editPersona";
    }

    @GetMapping("/delete")
    public String deletePersona() {
//        Optional<Persona> persona = personaService.getPersonaById(id);
//        if(persona.isPresent()){
//            personaService.deletePersona(id);
//            return ResponseEntity.ok("Registro con id: " + id + ", eliminado exitosamente...");
//            //return ResponseEntity.status(HttpStatus.ACCEPTED).body("Registro eliminado");
//        }
//        //return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        return ResponseEntity.badRequest().body("El registro no fué encontrado");
        return "forward:index";
    }

}
