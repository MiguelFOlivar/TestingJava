package com.mfigueroa.RegistroPersonas.controllers;

import com.mfigueroa.RegistroPersonas.model.Persona;
import com.mfigueroa.RegistroPersonas.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Persona>> getAllPersonas() {
        List<Persona> personas = personaService.getAllPersonas();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/getById")
    public ResponseEntity<Persona> getPersonaById(@RequestParam Long id) {
        Optional<Persona> persona = personaService.getPersonaById(id);
        return persona.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/getByName")
    public ResponseEntity<List<Persona>> getPersonaByName(@RequestParam String nombre) {
        List<Persona> personas = personaService.getPersonaByName(nombre);
        if(!personas.isEmpty()) {
            return ResponseEntity.ok(personas);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPersona(@RequestBody Persona persona) {
        try {
            Persona createdPersona = personaService.createPersona(persona);
            //return ResponseEntity.ok("Registro creado exitosamente: \n" + createdPersona);
            //return ResponseEntity.status(HttpStatus.CREATED).build();
            return new ResponseEntity<>(createdPersona, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Persona> updatePersona(@RequestParam Long id, @RequestBody Persona personaDetails) {
        Persona updatedPersona = personaService.updatePersona(id, personaDetails);
        if ( updatedPersona != null ) {
            personaService.updatePersona(id,personaDetails);
            //return ResponseEntity.ok("Registro actualizado exitosamente...\n" + updatedPersona);
            return ResponseEntity.ok(updatedPersona);
        }
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        //return ResponseEntity.ok("Registro no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePersona(@RequestParam Long id) {
        Optional<Persona> persona = personaService.getPersonaById(id);
        if(persona.isPresent()){
            personaService.deletePersona(id);
           return ResponseEntity.ok("Registro con id: " + id + ", eliminado exitosamente...");
            //return ResponseEntity.status(HttpStatus.ACCEPTED).body("Registro eliminado");
        }
        //return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.badRequest().body("El registro no fué encontrado");
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
