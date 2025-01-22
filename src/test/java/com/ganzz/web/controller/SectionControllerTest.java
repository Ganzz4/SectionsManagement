package com.ganzz.web.controller;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.model.Section;
import com.ganzz.web.model.UserEntity;
import com.ganzz.web.security.SecurityUtil;
import com.ganzz.web.service.CategoryService;
import com.ganzz.web.service.SectionService;
import com.ganzz.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SectionControllerTest {

    @Mock
    private SectionService sectionService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private SectionController sectionController;

    private static final String DEFAULT_PHOTO = "/images/default-section.jpg";

    private SectionDto createSampleSectionDto() {
        return SectionDto.builder()
                .id(1L)
                .title("Test Section")
                .content("Test Content")
                .createdOn(LocalDateTime.now())
                .build();
    }

    private CategoryDto createSampleCategoryDto() {
        return CategoryDto.builder()
                .id(1L)
                .name("Test Category")
                .build();
    }

    @BeforeEach
    void setUp() throws Exception {
        try (AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this)) {
            ReflectionTestUtils.setField(sectionController, "defaultPhoto", DEFAULT_PHOTO);
        }
    }

    @Test
    void listSections_ShouldReturnAllSectionsAndCorrectView() {
        List<SectionDto> sections = Collections.singletonList(createSampleSectionDto());
        when(sectionService.findAllSections()).thenReturn(sections);

        String viewName = sectionController.listSections(model);

        verify(model).addAttribute("sections", sections);
        assertEquals("sections-list", viewName);
    }

    @Test
    void sectionDetail_WithAuthenticatedUser_ShouldReturnDetailView() {
        SectionDto sectionDto = createSampleSectionDto();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");

        try (MockedStatic<SecurityUtil> securityUtil = mockStatic(SecurityUtil.class)) {
            securityUtil.when(SecurityUtil::getSessionUser).thenReturn("testUser");
            when(sectionService.findSectionById(1L)).thenReturn(sectionDto);
            when(userService.findByUsername("testUser")).thenReturn(user);

            String viewName = sectionController.sectionDetail(1L, model);

            verify(model).addAttribute("section", sectionDto);
            verify(model).addAttribute("user", user);
            verify(model).addAttribute(eq("formattedDate"), any(String.class));
            assertEquals("section-detail", viewName);
        }
    }

    @Test
    void createSectionForm_ShouldReturnCreateFormWithCategories() {
        List<CategoryDto> categories = Collections.singletonList(createSampleCategoryDto());
        when(categoryService.findAllCategories()).thenReturn(categories);

        String viewName = sectionController.createSectionForm(model);

        verify(model).addAttribute(eq("section"), any(Section.class));
        verify(model).addAttribute("categories", categories);
        assertEquals("section-create", viewName);
    }

    @Test
    void deleteSection_ShouldDeleteAndRedirect() {
        long sectionId = 1L;

        String viewName = sectionController.deleteSection(sectionId, model);

        verify(sectionService).delete(sectionId);
        assertEquals("redirect:/sections", viewName);
    }

    @Test
    void searchSection_ShouldReturnMatchingSections() {
        String query = "test";
        List<SectionDto> sections = Collections.singletonList(createSampleSectionDto());
        when(sectionService.searchSections(query)).thenReturn(sections);

        String viewName = sectionController.searchSection(query, model);

        verify(model).addAttribute("sections", sections);
        assertEquals("sections-list", viewName);
    }

    @Test
    void saveSection_WhenValidationErrors_ShouldReturnCreateForm() {
        SectionDto sectionDto = createSampleSectionDto();
        List<CategoryDto> categories = Collections.singletonList(createSampleCategoryDto());
        when(bindingResult.hasErrors()).thenReturn(true);
        when(categoryService.findAllCategories()).thenReturn(categories);

        String viewName = sectionController.saveSection(sectionDto, bindingResult, model);

        verify(model).addAttribute("categories", categories);
        assertEquals("section-create", viewName);
        verify(sectionService, never()).saveSection(any(SectionDto.class));
    }

    @Test
    void saveSection_WhenSuccessful_ShouldSaveAndRedirect() {
        SectionDto sectionDto = createSampleSectionDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = sectionController.saveSection(sectionDto, bindingResult, model);

        verify(sectionService).saveSection(sectionDto);
        assertEquals("redirect:/sections", viewName);
    }

    @Test
    void editSectionForm_ShouldReturnEditFormWithData() {
        long sectionId = 1L;
        SectionDto sectionDto = createSampleSectionDto();
        List<CategoryDto> categories = Collections.singletonList(createSampleCategoryDto());

        when(sectionService.findSectionById(sectionId)).thenReturn(sectionDto);
        when(categoryService.findAllCategories()).thenReturn(categories);

        String viewName = sectionController.editSectionForm(sectionId, model);

        verify(model).addAttribute("section", sectionDto);
        verify(model).addAttribute("categories", categories);
        verify(model).addAttribute("defaultPhotoPath", DEFAULT_PHOTO);
        assertEquals("section-edit", viewName);
    }

    @Test
    void editSection_WhenValidationErrors_ShouldReturnEditForm() {
        Long sectionId = 1L;
        SectionDto sectionDto = createSampleSectionDto();
        List<CategoryDto> categories = Collections.singletonList(createSampleCategoryDto());

        when(bindingResult.hasErrors()).thenReturn(true);
        when(categoryService.findAllCategories()).thenReturn(categories);

        String viewName = sectionController.editSection(sectionId, sectionDto, bindingResult, model);

        verify(model).addAttribute("categories", categories);
        verify(model).addAttribute("section", sectionDto);
        assertEquals("section-edit", viewName);
        verify(sectionService, never()).updateSection(any(SectionDto.class));
    }

    @Test
    void editSection_WhenSuccessful_ShouldUpdateAndRedirect() {
        Long sectionId = 1L;
        SectionDto sectionDto = SectionDto.builder()
                .title("Test Section")
                .content("Test Content")
                .build();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = sectionController.editSection(sectionId, sectionDto, bindingResult, model);

        assertEquals(sectionId, sectionDto.getId());
        verify(sectionService).updateSection(sectionDto);
        assertEquals("redirect:/sections", viewName);
    }
}