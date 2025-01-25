package com.ganzz.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Category name should not be empty")
    private String name;
}
