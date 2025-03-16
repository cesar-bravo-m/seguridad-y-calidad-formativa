package com.example.formativa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.formativa.models.Event;
import com.example.formativa.services.EventService;

@Controller
public class RouteController {

    @Autowired
    private EventService eventService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            model.addAttribute("error", "El evento solicitado no existe o no está disponible.");
        }
        model.addAttribute("event", event);
        return "details";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String location,
            Model model) {
        
        List<Event> searchResults = null;
        
        // If simple search query is provided
        if (query != null && !query.isEmpty()) {
            model.addAttribute("query", query);
            // Search in all fields
            searchResults = eventService.searchEvents(query, query, query, query);
        } 
        // If advanced search parameters are provided
        else if ((description != null && !description.isEmpty()) || 
                 (category != null && !category.isEmpty()) || 
                 (date != null && !date.isEmpty()) || 
                 (location != null && !location.isEmpty())) {
            
            searchResults = eventService.searchEvents(description, category, date, location);
            
            // Add search parameters to model for displaying in the form
            model.addAttribute("description", description);
            model.addAttribute("category", category);
            model.addAttribute("date", date);
            model.addAttribute("location", location);
        }
        
        // Add search results to model
        model.addAttribute("searchResults", searchResults);
        
        return "search";
    }
}
