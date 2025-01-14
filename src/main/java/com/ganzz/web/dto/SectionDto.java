package com.ganzz.web.dto;

import com.ganzz.web.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SectionDto {
    private Long id;
    @NotBlank(message = "Section title should not be empty")
    private String title;
    @NotBlank(message = "Location should not be empty")
    private String location;
    private String photoUrl;
    @NotNull(message = "Category should not be empty")
    private Category category;
    @NotBlank(message = "Content should not be empty")
    private String content;
    @NotBlank(message = "Opening hours  should not be empty")
    private String openingHours;
    @NotBlank(message = "Contact info should not be empty")
    @Pattern(regexp = "^[+]?\\d{10,15}$", message = "Invalid contact number format")
    private String contactInfo;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private List<EventDto> events;
}
