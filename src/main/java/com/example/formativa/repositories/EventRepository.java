package com.example.formativa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.formativa.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findTop10ByOrderByIdDesc();
    List<Event> findTop10ByOrderByIdAsc();
    
    // Search by description (case insensitive)
    List<Event> findByDescriptionContainingIgnoreCase(String description);
    
    // Search by category (case insensitive)
    List<Event> findByCategoryContainingIgnoreCase(String category);
    
    // Search by date/time (partial match)
    List<Event> findByTimeContaining(String time);
    
    // Search by location/address (case insensitive)
    List<Event> findByAddressContainingIgnoreCase(String address);
    
    // Combined search with all parameters optional
    @Query("SELECT e FROM Event e WHERE " +
           "(:description IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:category IS NULL OR LOWER(e.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
           "(:time IS NULL OR e.time LIKE CONCAT('%', :time, '%')) AND " +
           "(:address IS NULL OR LOWER(e.address) LIKE LOWER(CONCAT('%', :address, '%')))")
    List<Event> searchEvents(
        @Param("description") String description,
        @Param("category") String category,
        @Param("time") String time,
        @Param("address") String address
    );
} 