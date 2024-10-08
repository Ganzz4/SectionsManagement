package com.ganzz.web.repository;

import com.ganzz.web.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section,Long> {
    Optional<Section> findByTitle(String title);
}
