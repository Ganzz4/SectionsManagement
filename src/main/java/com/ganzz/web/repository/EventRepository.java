package com.ganzz.web.repository;

import com.ganzz.web.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    @Query("SELECT s from Event s WHERE s.name LIKE CONCAT('%', :query, '%')")
    List<Event> searchEvents(String query);
}
