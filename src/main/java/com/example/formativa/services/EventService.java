package com.example.formativa.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.formativa.models.Event;
import com.example.formativa.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }
    
    public List<Event> searchByDescription(String description) {
        return eventRepository.findByDescriptionContainingIgnoreCase(description);
    }
    
    public List<Event> searchByCategory(String category) {
        return eventRepository.findByCategoryContainingIgnoreCase(category);
    }
    
    public List<Event> searchByTime(String time) {
        return eventRepository.findByTimeContaining(time);
    }
    
    public List<Event> searchByAddress(String address) {
        return eventRepository.findByAddressContainingIgnoreCase(address);
    }
    
    public List<Event> searchEvents(String description, String category, String time, String address) {
        return eventRepository.searchEvents(description, category, time, address);
    }
}
