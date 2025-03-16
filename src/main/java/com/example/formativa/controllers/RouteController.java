package com.example.formativa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
