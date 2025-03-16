package com.example.formativa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.formativa.models.Event;
import com.example.formativa.repositories.EventRepository;

@Controller
public class HomeController {
    
    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Event> latestEvents = eventRepository.findTop10ByOrderByIdDesc();
        List<Event> featuredEvents = eventRepository.findTop10ByOrderByIdAsc(); // This is temporary, ideally you'd have a 'featured' flag
        
        model.addAttribute("latestEvents", latestEvents);
        model.addAttribute("featuredEvents", featuredEvents);
        return "home";
    }
} 