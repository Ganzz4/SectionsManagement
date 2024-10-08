package repository;

import com.ganzz.web.models.Category;
import com.ganzz.web.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    //  methods
}
