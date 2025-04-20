package com.example.formativa.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.formativa.models.Event;
import com.example.formativa.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setDescription("Concierto de Rock");
        testEvent.setCategory("Música");
        testEvent.setTime("2023-12-31 20:00");
        testEvent.setAddress("Arena Madrid");
        testEvent.setOrganizers("Promotora XYZ");
        testEvent.setAvailableServices("Bar, Parking");
        testEvent.setAttractions("Bandas en vivo");
        testEvent.setImage("rock-concert.jpg");
    }

    @Test
    @DisplayName("Debería obtener un evento por ID")
    void getEventById_ShouldReturnEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        Event result = eventService.getEventById(1L);
        assertEquals(result.getId(), testEvent.getId());
        assertEquals(result.getDescription(), testEvent.getDescription());
        assertEquals(result.getCategory(), testEvent.getCategory());
        assertEquals(result.getAttractions(), testEvent.getAttractions());
        assertEquals(result.getImage(), testEvent.getImage());
    }
}
