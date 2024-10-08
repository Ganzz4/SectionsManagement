package repository;

import com.ganzz.web.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section,Long> {
    Optional<Section> findByTitle(String title);
}
