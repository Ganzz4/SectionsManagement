package com.ganzz.web.service.impl;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.mapper.SectionMapper;
import com.ganzz.web.model.Section;
import com.ganzz.web.model.UserEntity;
import com.ganzz.web.repository.UserRepository;
import com.ganzz.web.security.SecurityUtil;
import com.ganzz.web.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ganzz.web.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

import static com.ganzz.web.mapper.SectionMapper.mapToSection;
import static com.ganzz.web.mapper.SectionMapper.mapToSectionDto;


@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final PhotoServiceImpl photoService;


    @Override
    public List<SectionDto> findAllSections() {
        List<Section> sections = sectionRepository.findAll();
        return sections.stream().map(SectionMapper::mapToSectionDto).collect(Collectors.toList());
    }

    @Override
    public Section saveSection(SectionDto sectionDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        sectionDto.setPhotoUrl(photoService.getValidPhotoUrl(sectionDto.getPhotoUrl()));

        Section section = mapToSection(sectionDto);

        section.setCreatedBy(user);
        return sectionRepository.save(section);
    }

    @Override
    public SectionDto findSectionById(long sectionId) {
        Section section = sectionRepository.findById(sectionId).get();
        return mapToSectionDto(section);
    }

    @Override
    public void updateSection(SectionDto sectionDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        sectionDto.setPhotoUrl(photoService.getValidPhotoUrl(sectionDto.getPhotoUrl()));

        Section section = mapToSection(sectionDto);

        section.setCreatedBy(user);
        sectionRepository.save(section);
    }

    @Override
    public void delete(long categoryId) {
       sectionRepository.deleteById(categoryId);
    }

    public List<SectionDto> searchSections(String query) {
        List<Section> sections;
        if (query == null || query.trim().isEmpty()) {
            sections = sectionRepository.findAll();
        } else {
            sections = sectionRepository.searchSections(query);
        }
        return sections.stream()
                .map(SectionMapper::mapToSectionDto)
                .collect(Collectors.toList());
    }
}
