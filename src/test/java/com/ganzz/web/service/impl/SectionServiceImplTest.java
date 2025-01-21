package com.ganzz.web.service.impl;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Section;
import com.ganzz.web.models.UserEntity;
import com.ganzz.web.repository.SectionRepository;
import com.ganzz.web.repository.UserRepository;
import com.ganzz.web.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceImplTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhotoServiceImpl photoService;

    @InjectMocks
    private SectionServiceImpl sectionService;

    private Section section;
    private SectionDto sectionDto;
    private UserEntity user;
    private static final String USERNAME = "testUser";
    private static final String PHOTO_URL = "photo.jpg";
    private static final String VALID_PHOTO_URL = "valid-photo.jpg";

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setUsername(USERNAME);

        section = new Section();
        section.setId(1L);
        section.setTitle("Test Section");
        section.setPhotoUrl(VALID_PHOTO_URL);
        section.setCreatedBy(user);

        sectionDto = SectionDto.builder()
                .id(1L)
                .title("Test Section")
                .photoUrl(PHOTO_URL)
                .build();
    }

    @Test
    void findAllSections_ShouldReturnAllSections() {
        List<Section> sections = Collections.singletonList(section);
        when(sectionRepository.findAll()).thenReturn(sections);

        List<SectionDto> result = sectionService.findAllSections();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(section.getTitle());
        verify(sectionRepository).findAll();
    }

    @Test
    void saveSection_ShouldSaveAndReturnSection() {
        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getSessionUser).thenReturn(USERNAME);
            when(userRepository.findByUsername(USERNAME)).thenReturn(user);
            when(photoService.getValidPhotoUrl(PHOTO_URL)).thenReturn(VALID_PHOTO_URL);
            when(sectionRepository.save(any(Section.class))).thenReturn(section);

            Section result = sectionService.saveSection(sectionDto);

            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(section.getTitle());
            assertThat(result.getPhotoUrl()).isEqualTo(VALID_PHOTO_URL);
            assertThat(result.getCreatedBy()).isEqualTo(user);
            verify(sectionRepository).save(any(Section.class));
        }
    }

    @Test
    void findSectionById_ShouldReturnSection() {
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(section));

        SectionDto result = sectionService.findSectionById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(section.getTitle());
        verify(sectionRepository).findById(1L);
    }

    @Test
    void updateSection_ShouldUpdateSection() {
        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getSessionUser).thenReturn(USERNAME);
            when(userRepository.findByUsername(USERNAME)).thenReturn(user);
            when(photoService.getValidPhotoUrl(PHOTO_URL)).thenReturn(VALID_PHOTO_URL);

            sectionService.updateSection(sectionDto);

            verify(sectionRepository).save(any(Section.class));
            verify(photoService).getValidPhotoUrl(PHOTO_URL);
        }
    }

    @Test
    void delete_ShouldDeleteSection() {
        long sectionId = 1L;

        sectionService.delete(sectionId);

        verify(sectionRepository).deleteById(sectionId);
    }

    @Test
    void searchSections_WithEmptyQuery_ShouldReturnAllSections() {
        List<Section> sections = Collections.singletonList(section);
        when(sectionRepository.findAll()).thenReturn(sections);

        List<SectionDto> result = sectionService.searchSections("");

        assertThat(result).hasSize(1);
        verify(sectionRepository).findAll();
        verify(sectionRepository, never()).searchSections(any());
    }

    @Test
    void searchSections_WithQuery_ShouldReturnFilteredSections() {
        String query = "test";
        List<Section> sections = Collections.singletonList(section);
        when(sectionRepository.searchSections(query)).thenReturn(sections);

        List<SectionDto> result = sectionService.searchSections(query);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(section.getTitle());
        verify(sectionRepository).searchSections(query);
        verify(sectionRepository, never()).findAll();
    }
}