package com.example.formativa.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String description;
    private String address;
    private String time;
    private String organizers;
    private String image;

    @Column(name = "available_services")
    private String availableServices;
    
    private String attractions;

    public Event() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrganizers() {
        return organizers;
    }

    public void setOrganizers(String organizers) {
        this.organizers = organizers;
    }

    public String getAvailableServices() {
        return availableServices;
    }

    public void setAvailableServices(String availableServices) {
        this.availableServices = availableServices;
    }

    public String getAttractions() {
        return attractions;
    }

    public void setAttractions(String attractions) {
        this.attractions = attractions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
               Objects.equals(category, event.category) &&
               Objects.equals(description, event.description) &&
               Objects.equals(address, event.address) &&
               Objects.equals(time, event.time) &&
               Objects.equals(organizers, event.organizers) &&
               Objects.equals(image, event.image) &&
               Objects.equals(availableServices, event.availableServices) &&
               Objects.equals(attractions, event.attractions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, description, address, time, organizers, 
                          image, availableServices, attractions);
    }

    @Override
    public String toString() {
        return "Event{" +
               "id=" + id +
               ", category='" + category + '\'' +
               ", description='" + description + '\'' +
               ", address='" + address + '\'' +
               ", time='" + time + '\'' +
               ", organizers='" + organizers + '\'' +
               ", image='" + image + '\'' +
               ", availableServices='" + availableServices + '\'' +
               ", attractions='" + attractions + '\'' +
               '}';
    }
} 