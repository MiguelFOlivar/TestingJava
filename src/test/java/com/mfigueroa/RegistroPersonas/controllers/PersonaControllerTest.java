package com.mfigueroa.RegistroPersonas.controllers;

import com.mfigueroa.RegistroPersonas.model.Persona;
import com.mfigueroa.RegistroPersonas.service.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class PersonaControllerTest {

    @Mock
    private PersonaService personaService;

    @InjectMocks
    private PersonaController personaController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPersonas() {
        // Creamos objetos de tipo persona
        Persona p1 = new Persona("Mike", "Bloom", 30, "Direccion fake", "fakemail@mail.com");
        Persona p2 = new Persona("Sam", "Doe", 31, "Direccion fake 2", "fakemail2@mail.com");
        Persona p3 = new Persona(null, "Bloom", 32, "Direccion fake3", "fakemail3@mail.com");
        Persona p4 = new Persona("", "Bloom", 33, "Direccion fake4", null);

        //Las almanenamos en la lista, que posteriormente usaremos para simular nuestro mock
        List<Persona> personasMock = Arrays.asList(p1, p2, p3, p4);
        //Al llamarse el metodo getAllPersonas de nuestro servicio, se devuelv dicha lista
        when(personaService.getAllPersonas()).thenReturn(personasMock);

        //llamanos el metodo de nuestro Controlador para recuperar la lista
        ResponseEntity<List<Persona>> response = personaController.getAllPersonas();

        //Comprobamos que el objeto no sea nulo
        assertNotNull(response);

        //verificamos que el primer elemento tenga como atributo nombre=Mike
        assertEquals("Mike", response.getBody().get(0).getNombre());

        //Comprobamos que el 3er registro tenga el campo nombre=null
        assertNull(response.getBody().get(2).getNombre());

    }

    @Test
    void testCrearPersona() {
        Persona p1 = new Persona("Mike", "Bloom", 30, "Direccion fake", "fakemail@mail.com");

        //Definimos el comportamiento de nuestro mock de personaService al llamar a su metodo create
        when(personaService.createPersona(p1)).thenReturn(p1);

        //Llamamos a nuestro controladorPersona
        ResponseEntity<?> response = personaController.createPersona(p1);

        //comprobamos que al ejecutarse el metodo del controlador el codigo sea CREATED
        assertEquals( HttpStatus.CREATED, response.getStatusCode() );

        //Casteamos para obtener el cuerpo de nuestra respuesta como tipo persona
        Persona personaRespuesta = (Persona) response.getBody();

        //comprobamos que el nombre de nuestro objeto contenga el valor "Mike"
        assertEquals("Mike", personaRespuesta.getNombre());


    }

    @Test
    void testCreatePersona_BAD_REQUEST() {
        //creamos un objeto persona con datos invalidos
        Persona personaInvalida = new Persona(null, "Bloom", 30, "Direccion fake", "fakemail@mail.com");

        //simulamos un error al ingresar los datos incorrectos, en este caso "nombre", y arrojamos una excepcion
        when(personaService.createPersona(personaInvalida)).thenThrow(new IllegalArgumentException("El nombre es obligatorio"));

        //Llamamos a nuestro controlador para crear a la persona, con datos invalidos
        ResponseEntity<?> response = personaController.createPersona(personaInvalida);

        //Verificamos que efectivamente se obtenga BAD_REQUEST como respuesta
        assertEquals( HttpStatus.BAD_REQUEST, response.getStatusCode() );

        //Aseguramos que el mensaje de la Exception sea el que esperabamos
        assertEquals("El nombre es obligatorio", response.getBody());
    }
    @Test
    void testGetPersonaById() {
        Persona persona = new Persona("Mike", "Bloom", 30, "Direccion fake", "fakemail@mail.com");

        //Simulamos por medio de nuestro mock la obtencion de un registro por medio del Id
        when(personaService.getPersonaById(1L)).thenReturn(Optional.of(persona));

        //llamamos al metodo de nuestro controlador
        ResponseEntity<Persona> result = personaController.getPersonaById(1L);

        //verificamos que la consulta sea exitosa, es decir que se encuentra el registro
        assertEquals(HttpStatus.OK, result.getStatusCode());

        //comprobamos el nombre que esperabamos de nuestro objeto
        assertEquals("Mike", result.getBody().getNombre());
    }

    @Test
    public void testCreatePersona_InvalidData() {
        Persona personaInvalida = new Persona(null, "", -5, "", "");

        // Simulamos que el servicio lanza una excepción cuando los datos son inválidos
        when(personaService.createPersona(personaInvalida)).thenThrow(new IllegalArgumentException("Datos de persona inválidos"));

        // Llamamos al servicio para crear la persona
        Exception exception = assertThrows(IllegalArgumentException.class, () -> personaService.createPersona(personaInvalida));

        // Verificamos que la excepción sea la esperada
        assertEquals("Datos de persona inválidos", exception.getMessage());
    }

    @Test
    void testGetPersonaById_NOT_FOUND() {
        //Simulamos por medio de nuestro mock, que no existe registro con el id proporcionado
        when(personaService.getPersonaById(1L)).thenReturn(Optional.empty());

        //Llamamos a nuestro controlador para realizar la consulta
        ResponseEntity<Persona> response = personaController.getPersonaById(1L);

        //Verificamos que el status sea NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        //Por lo tanto nos aseguramos tambien que el objeto este vacío
        assertNull(response.getBody());

    }

    @Test
    void testPersonaUpdate() {

       Persona personaActualizada = new Persona("Mike", "Doe", 31, "Direccion Real", "realmail@mail.com");

        when( personaService.updatePersona(1L, personaActualizada)).thenReturn(personaActualizada);
        ResponseEntity<Persona> response = personaController.updatePersona(1L, personaActualizada);

        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertEquals("Direccion Real", response.getBody().getDireccion());
        assertEquals("realmail@mail.com", response.getBody().getEmail());
    }

    @Test
    void testfindByNombre() {
        //Creamos objetos de tipo persona para nuestro mock
        Persona persona1 = new Persona("Mike", "Bloom", 30, "fake", "fakemail@mail.com");
        Persona persona2 = new Persona("MIKE", "Tyson", 60, "fake", "fakemailn@mail.com");

        List<Persona> personasMock = new ArrayList<>();
        personasMock.add(persona1);
        personasMock.add(persona2);

        when(personaService.getPersonaByName("Mike")).thenReturn(personasMock);

        ResponseEntity<List<Persona>> response = personaController.getPersonaByName("Mike");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertTrue( response.getBody().stream()
                .anyMatch(persona -> persona.getNombre().equalsIgnoreCase("MIKE")));

    }

}