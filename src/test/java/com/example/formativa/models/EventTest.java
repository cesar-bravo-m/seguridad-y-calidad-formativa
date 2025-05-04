package com.example.formativa.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventTest {

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(event);
        assertNull(event.getId());
        assertNull(event.getCategory());
        assertNull(event.getDescription());
        assertNull(event.getAddress());
        assertNull(event.getTime());
        assertNull(event.getOrganizers());
        assertNull(event.getImage());
        assertNull(event.getAvailableServices());
        assertNull(event.getAttractions());
    }

    @Test
    void testGettersAndSetters() {
        event.setId(1L);
        assertEquals(1L, event.getId());

        event.setCategory("Concierto");
        assertEquals("Concierto", event.getCategory());

        event.setDescription("Gran concierto de rock");
        assertEquals("Gran concierto de rock", event.getDescription());

        event.setAddress("Calle Principal 123");
        assertEquals("Calle Principal 123", event.getAddress());

        event.setTime("20:00");
        assertEquals("20:00", event.getTime());

        event.setOrganizers("Organización XYZ");
        assertEquals("Organización XYZ", event.getOrganizers());

        event.setImage("concierto.jpg");
        assertEquals("concierto.jpg", event.getImage());

        event.setAvailableServices("Parking, WiFi, Bar");
        assertEquals("Parking, WiFi, Bar", event.getAvailableServices());

        event.setAttractions("Banda Principal, DJ");
        assertEquals("Banda Principal, DJ", event.getAttractions());
    }

    @Test
    void testEqualsAndHashCode() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setCategory("Concierto");
        event1.setDescription("Descripción");
        event1.setAddress("Dirección");
        event1.setTime("20:00");
        event1.setOrganizers("Organizador");
        event1.setImage("imagen.jpg");
        event1.setAvailableServices("Servicios");
        event1.setAttractions("Atracciones");

        Event event2 = new Event();
        event2.setId(1L);
        event2.setCategory("Concierto");
        event2.setDescription("Descripción");
        event2.setAddress("Dirección");
        event2.setTime("20:00");
        event2.setOrganizers("Organizador");
        event2.setImage("imagen.jpg");
        event2.setAvailableServices("Servicios");
        event2.setAttractions("Atracciones");

        Event event3 = new Event();
        event3.setId(2L);
        event3.setCategory("Teatro");

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertNotEquals(event1, null);
        assertNotEquals(event1, new Object());

        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testToString() {
        event.setId(1L);
        event.setCategory("Concierto");
        event.setDescription("Gran concierto");
        event.setAddress("Calle Principal");
        event.setTime("20:00");
        event.setOrganizers("Organización XYZ");
        event.setImage("concierto.jpg");
        event.setAvailableServices("Parking");
        event.setAttractions("Banda Principal");

        String toString = event.toString();
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Concierto"));
        assertTrue(toString.contains("Gran concierto"));
        assertTrue(toString.contains("Calle Principal"));
        assertTrue(toString.contains("20:00"));
        assertTrue(toString.contains("Organización XYZ"));
        assertTrue(toString.contains("concierto.jpg"));
        assertTrue(toString.contains("Parking"));
        assertTrue(toString.contains("Banda Principal"));
    }
} 