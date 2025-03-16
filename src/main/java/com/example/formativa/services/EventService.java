package com.example.formativa.services;

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
}
