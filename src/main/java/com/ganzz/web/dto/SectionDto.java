package com.ganzz.web.dto;

import com.ganzz.web.models.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class SectionDto {
    private Long id;
    private String title;
    private String location;
    private String photoUrl;
    private Category category;
    private String content;
    private String openingHours;
    private String contactInfo;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
