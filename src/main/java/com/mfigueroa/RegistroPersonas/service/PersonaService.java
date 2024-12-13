package com.mfigueroa.RegistroPersonas.service;

import com.mfigueroa.RegistroPersonas.model.Persona;
import com.mfigueroa.RegistroPersonas.repository.PersonaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;


    public List<Persona>getAllPersonas(){
        return personaRepository.findAll();
    }
    public Optional<Persona>getPersonaById(Long id){
        return personaRepository.findById(id);
    }

    public List<Persona>getPersonaByName(String nombre){
        return personaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Persona createPersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public Persona updatePersona(Long id, Persona personaData) {
        Optional<Persona> persona = personaRepository.findById(id);
        if(persona.isPresent()){
            Persona p = persona.get();
            p.setNombre(personaData.getNombre());
            p.setApellido(personaData.getApellido());
            p.setEdad(personaData.getEdad());
            p.setDireccion(personaData.getDireccion());
            p.setEmail(personaData.getEmail());
            return personaRepository.save(p);
        }
        return null;
    }


    public void deletePersona(Long id){
        personaRepository.deleteById(id);
    }

}


