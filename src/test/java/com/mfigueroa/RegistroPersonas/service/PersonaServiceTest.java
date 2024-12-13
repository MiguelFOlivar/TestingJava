package com.mfigueroa.RegistroPersonas.service;

import com.mfigueroa.RegistroPersonas.model.Persona;
import com.mfigueroa.RegistroPersonas.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonaServiceTest {

    @Mock// creamos un mock del repositorio
    private PersonaRepository personaRepository;

    @InjectMocks// inyectamos el mock creado en la clase personaService
    private PersonaService personaService;

    @BeforeEach//iniciamos los mocks antes de cada prueba
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPersonas(){

        //vamos a crear objetos de tipo persona simulados
        Persona persona1 = new Persona("Juan", "Pérez", 25, "Calle Falsa 123", "juan@correo.com");
        Persona persona2 = new Persona("María", "Gómez", 30, "Calle Real 456", "maria@correo.com");

        // Simulamos el comportamiento del repositorio para que retorne las personas
        List<Persona> personasMock = Arrays.asList(persona1, persona2);

        when( personaRepository.findAll()).thenReturn( personasMock ); // Mockito devuelve la lista simulada

        // Ahora llamamos al servicio para obtener las personas
        List<Persona> personas = personaService.getAllPersonas();

        // **** Ahora vamos a verificar que los resultados sean los esperados ****
        //La lista no debe estar vacía y ademas debe retornar dos personas


        assertNotNull( personas ); //comprobamos que la lista no esté vacía
        assertEquals(2, personas.size());//comprobamos que el tamaño de la lista sea dos
        assertEquals("Juan", personas.get(0).getNombre());
        assertEquals("María", personas.get(1).getNombre());

    }

    @Test
    public void gtPersonaById(){
        Persona persona = new Persona("Juan", "Pérez", 25, "Calle Falsa 123", "juan@correo.com");
        when( personaRepository.findById(3L)).thenReturn(Optional.of(persona) );

        Optional<Persona> result = personaService.getPersonaById(3L);

        assertTrue( result.isPresent() );
        assertEquals("Juan", result.get().getNombre());
    }

    @Test
    public void testPersonaById_NotFound() {

        when( personaRepository.findById(1L)).thenReturn(Optional.empty() );

        Optional<Persona> result = personaService.getPersonaById(1L);

        assertFalse( result.isPresent() );
    }

    @Test
    public void testCreatePersona() {
        Persona persona = new Persona("Juan", "Pérez", 25, "Calle Falsa 123", "juan@correo.com");
        when( personaRepository.save(persona) ).thenReturn(persona);

        Persona result = personaService.createPersona( persona );

        //verificamos que el metodo save del repositorio se haya llamado exactamente 1 vez
        verify( personaRepository, times(1)).save(persona);

        assertNotNull( result );
        assertEquals("Juan", result.getNombre());
    }

    @Test
    public void testUpdatePersona() {
        Persona personaExistente = new Persona("John", "Doe", 35, "Calle Fake 432", "john@correo.com");
        Persona personaActualizada = new Persona("John", "Torvalds", 26, "Calle Real 789", "john@nuevo.com");

        //primero vamos a simular que la persona existe y es devuelta por el metodo findById
        when( personaRepository.findById(5L)).thenReturn(Optional.of(personaExistente) );

        //Ahora vamos a simular que se guarda la persona de manera correcta despues de actualizarla
        when( personaRepository.save( personaExistente )).thenReturn( personaActualizada );

        Persona result = personaService.updatePersona(5L, personaActualizada);
        assertNotNull( result );
        assertEquals(26, result.getEdad());
        assertEquals("Torvalds", result.getApellido());

    }
    @Test
    public void testUpdatePersona_Not_Found() {
        Persona persona = new Persona("John", "Doe", 35, "Calle Fake 432", "john@correo.com");
        when( personaRepository.findById(1L)).thenReturn(Optional.empty() );

        Persona personaResult = personaService.updatePersona(1L, persona);

        assertNull( personaResult );

    }


    @Test
    public void testDeletePersona() {
        personaService.deletePersona(1L);
        verify(personaRepository, times(1)).deleteById(1L);
    }
}