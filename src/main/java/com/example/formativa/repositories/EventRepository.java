package com.example.formativa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.formativa.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findTop10ByOrderByIdDesc();
    List<Event> findTop10ByOrderByIdAsc();
} 