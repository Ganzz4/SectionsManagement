package com.ganzz.web.repository;

import com.ganzz.web.models.Category;
import com.ganzz.web.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    //  methods
}
