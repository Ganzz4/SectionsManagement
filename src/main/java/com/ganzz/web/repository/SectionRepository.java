package com.ganzz.web.repository;

import com.ganzz.web.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section,Long> {
    Optional<Section> findByTitle(String title);
    @Query("SELECT s from Section s WHERE s.title LIKE CONCAT('%', :query, '%')")
    List<Section> searchSections(String query);
}
