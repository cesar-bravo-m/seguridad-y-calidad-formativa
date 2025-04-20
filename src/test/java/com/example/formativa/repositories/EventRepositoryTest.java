package com.example.formativa.repositories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.formativa.models.Event;

@DataJpaTest
@ActiveProfiles("test")
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event event1;
    private Event event2;
    private Event event3;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();

        event1 = new Event();
        event1.setDescription("Concierto de Rock");
        event1.setCategory("Música");
        event1.setTime("2023-12-31 20:00");
        event1.setAddress("Arena Madrid");
        event1.setOrganizers("Promotora XYZ");
        event1.setAvailableServices("Bar, Parking");
        event1.setAttractions("Bandas en vivo");
        event1.setImage("rock-concert.jpg");
        eventRepository.save(event1);

        event2 = new Event();
        event2.setDescription("Exposición de Arte Moderno");
        event2.setCategory("Arte");
        event2.setTime("2023-11-15 10:00");
        event2.setAddress("Museo de Arte Contemporáneo");
        event2.setOrganizers("Fundación ABC");
        event2.setAvailableServices("Guía, Cafetería");
        event2.setAttractions("Obras de artistas internacionales");
        event2.setImage("art-exhibition.jpg");
        eventRepository.save(event2);

        event3 = new Event();
        event3.setDescription("Festival de Cine");
        event3.setCategory("Cine");
        event3.setTime("2023-10-01 18:00");
        event3.setAddress("Centro Cultural");
        event3.setOrganizers("Asociación de Cineastas");
        event3.setAvailableServices("Proyección, Mesa redonda");
        event3.setAttractions("Películas premiadas");
        event3.setImage("film-festival.jpg");
        eventRepository.save(event3);
    }

    @Test
    @DisplayName("Debería encontrar los 10 eventos más recientes ordenados por ID descendente")
    void findTop10ByOrderByIdDesc_ShouldReturnEventsInDescendingOrder() {
        List<Event> events = eventRepository.findTop10ByOrderByIdDesc();

        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertEquals(3, events.size());
        
        assertTrue(events.get(0).getId() > events.get(1).getId());
        assertTrue(events.get(1).getId() > events.get(2).getId());
    }

    @Test
    @DisplayName("Debería encontrar los 10 eventos más antiguos ordenados por ID ascendente")
    void findTop10ByOrderByIdAsc_ShouldReturnEventsInAscendingOrder() {
        List<Event> events = eventRepository.findTop10ByOrderByIdAsc();

        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertEquals(3, events.size());
        
        assertTrue(events.get(0).getId() < events.get(1).getId());
        assertTrue(events.get(1).getId() < events.get(2).getId());
    }

    @Test
    @DisplayName("Debería encontrar eventos por descripción (ignorando mayúsculas/minúsculas)")
    void findByDescriptionContainingIgnoreCase_ShouldReturnMatchingEvents() {
        List<Event> events = eventRepository.findByDescriptionContainingIgnoreCase("rock");

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Concierto de Rock", events.get(0).getDescription());
    }

    @Test
    @DisplayName("Debería encontrar eventos por categoría (ignorando mayúsculas/minúsculas)")
    void findByCategoryContainingIgnoreCase_ShouldReturnMatchingEvents() {
        List<Event> events = eventRepository.findByCategoryContainingIgnoreCase("arte");

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Arte", events.get(0).getCategory());
    }

    @Test
    @DisplayName("Debería encontrar eventos por tiempo (coincidencia parcial)")
    void findByTimeContaining_ShouldReturnMatchingEvents() {
        List<Event> events = eventRepository.findByTimeContaining("2023-12");

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("2023-12-31 20:00", events.get(0).getTime());
    }

    @Test
    @DisplayName("Debería encontrar eventos por dirección (ignorando mayúsculas/minúsculas)")
    void findByAddressContainingIgnoreCase_ShouldReturnMatchingEvents() {
        List<Event> events = eventRepository.findByAddressContainingIgnoreCase("madrid");

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Arena Madrid", events.get(0).getAddress());
    }

    @Test
    @DisplayName("Debería buscar eventos con todos los criterios")
    void searchEvents_WithAllParameters_ShouldReturnMatchingEvents() {
        List<Event> events = eventRepository.searchEvents("Rock", "Música", "2023-12", "Madrid");

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Concierto de Rock", events.get(0).getDescription());
        assertEquals("Música", events.get(0).getCategory());
        assertEquals("2023-12-31 20:00", events.get(0).getTime());
        assertEquals("Arena Madrid", events.get(0).getAddress());
    }

    @Test
    @DisplayName("Debería buscar eventos con criterios parciales")
    void searchEvents_WithPartialParameters_ShouldReturnMatchingEvents() {
        List<Event> events = eventRepository.searchEvents("Arte", null, "2023-11", null);

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Exposición de Arte Moderno", events.get(0).getDescription());
        assertEquals("Arte", events.get(0).getCategory());
        assertEquals("2023-11-15 10:00", events.get(0).getTime());
    }

    @Test
    @DisplayName("Debería buscar eventos con un solo criterio")
    void searchEvents_WithSingleParameter_ShouldReturnMatchingEvents() {
        List<Event> events = eventRepository.searchEvents(null, "Cine", null, null);

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Festival de Cine", events.get(0).getDescription());
        assertEquals("Cine", events.get(0).getCategory());
    }

    @Test
    @DisplayName("Debería devolver todos los eventos cuando no se proporcionan criterios")
    void searchEvents_WithNoParameters_ShouldReturnAllEvents() {
        List<Event> events = eventRepository.searchEvents(null, null, null, null);

        assertNotNull(events);
        assertEquals(3, events.size());
    }
} 