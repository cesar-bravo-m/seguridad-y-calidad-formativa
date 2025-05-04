package com.example.formativa.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.formativa.models.Event;
import com.example.formativa.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event event1;
    private Event event2;
    private List<Event> events;

    @BeforeEach
    void setUp() {
        event1 = new Event();
        event1.setId(1L);
        event1.setCategory("Concierto");
        event1.setDescription("Concierto de rock");
        event1.setAddress("Calle Principal 123");
        event1.setTime("20:00");
        event1.setOrganizers("Organización XYZ");
        event1.setImage("concierto.jpg");
        event1.setAvailableServices("Parking, WiFi");
        event1.setAttractions("Banda Principal");

        event2 = new Event();
        event2.setId(2L);
        event2.setCategory("Teatro");
        event2.setDescription("Obra de teatro");
        event2.setAddress("Avenida Central 456");
        event2.setTime("19:00");
        event2.setOrganizers("Compañía ABC");
        event2.setImage("teatro.jpg");
        event2.setAvailableServices("Parking");
        event2.setAttractions("Actores principales");

        events = Arrays.asList(event1, event2);
    }

    @Test
    void testGetEventById_WhenEventExists() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));
        
        Event result = eventService.getEventById(1L);
        
        assertNotNull(result);
        assertEquals(event1, result);
        verify(eventRepository).findById(1L);
    }

    @Test
    void testGetEventById_WhenEventDoesNotExist() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());
        
        Event result = eventService.getEventById(99L);
        
        assertNull(result);
        verify(eventRepository).findById(99L);
    }

    @Test
    void testSearchByDescription() {
        when(eventRepository.findByDescriptionContainingIgnoreCase("rock"))
            .thenReturn(Arrays.asList(event1));
        
        List<Event> results = eventService.searchByDescription("rock");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(event1, results.get(0));
        verify(eventRepository).findByDescriptionContainingIgnoreCase("rock");
    }

    @Test
    void testSearchByCategory() {
        when(eventRepository.findByCategoryContainingIgnoreCase("concierto"))
            .thenReturn(Arrays.asList(event1));
        
        List<Event> results = eventService.searchByCategory("concierto");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(event1, results.get(0));
        verify(eventRepository).findByCategoryContainingIgnoreCase("concierto");
    }

    @Test
    void testSearchByTime() {
        when(eventRepository.findByTimeContaining("20:00"))
            .thenReturn(Arrays.asList(event1));
        
        List<Event> results = eventService.searchByTime("20:00");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(event1, results.get(0));
        verify(eventRepository).findByTimeContaining("20:00");
    }

    @Test
    void testSearchByAddress() {
        when(eventRepository.findByAddressContainingIgnoreCase("principal"))
            .thenReturn(Arrays.asList(event1));
        
        List<Event> results = eventService.searchByAddress("principal");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(event1, results.get(0));
        verify(eventRepository).findByAddressContainingIgnoreCase("principal");
    }

    @Test
    void testSearchEvents_WithAllParameters() {
        when(eventRepository.searchEvents("rock", "concierto", "20:00", "principal"))
            .thenReturn(Arrays.asList(event1));
        
        List<Event> results = eventService.searchEvents("rock", "concierto", "20:00", "principal");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(event1, results.get(0));
        verify(eventRepository).searchEvents("rock", "concierto", "20:00", "principal");
    }

    @Test
    void testSearchEvents_WithSomeNullParameters() {
        when(eventRepository.searchEvents(null, "concierto", null, "principal"))
            .thenReturn(Arrays.asList(event1));
        
        List<Event> results = eventService.searchEvents(null, "concierto", null, "principal");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(event1, results.get(0));
        verify(eventRepository).searchEvents(null, "concierto", null, "principal");
    }

    @Test
    void testSearchEvents_WithAllNullParameters() {
        when(eventRepository.searchEvents(null, null, null, null))
            .thenReturn(events);
        
        List<Event> results = eventService.searchEvents(null, null, null, null);
        
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.contains(event1));
        assertTrue(results.contains(event2));
        verify(eventRepository).searchEvents(null, null, null, null);
    }
}
