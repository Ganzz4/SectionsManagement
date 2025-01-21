package com.ganzz.web.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceImplTest {

    @Spy
    @InjectMocks
    private PhotoServiceImpl photoService;

    private final String DEFAULT_PHOTO = "https://default-photo.com/default.jpg";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(photoService, "defaultPhoto", DEFAULT_PHOTO);
    }

    @Test
    void testGetValidPhotoUrl_ValidExternalUrl() {
        String validUrl = "http://valid-url.com/photo.jpg";
        doReturn(validUrl).when(photoService).validateExternalUrl(validUrl);

        String result = photoService.getValidPhotoUrl(validUrl);

        assertEquals(validUrl, result);
    }

    @Test
    void testGetValidPhotoUrl_InvalidExternalUrl() {
        String invalidUrl = "http://invalid-url.com/photo.jpg";
        doReturn(DEFAULT_PHOTO).when(photoService).validateExternalUrl(invalidUrl);

        String result = photoService.getValidPhotoUrl(invalidUrl);

        assertEquals(DEFAULT_PHOTO, result);
    }

    @Test
    void testGetValidPhotoUrl_ValidLocalFile() {
        String localFilePath = "/path/to/valid/file.jpg";
        doReturn(localFilePath).when(photoService).validateLocalFile(localFilePath);

        String result = photoService.getValidPhotoUrl(localFilePath);

        assertEquals(localFilePath, result);
    }

    @Test
    void testGetValidPhotoUrl_InvalidLocalFile() {
        String invalidLocalFile = "/path/to/invalid/file.jpg";
        doReturn(DEFAULT_PHOTO).when(photoService).validateLocalFile(invalidLocalFile);

        String result = photoService.getValidPhotoUrl(invalidLocalFile);

        assertEquals(DEFAULT_PHOTO, result);
    }

    @Test
    void testGetValidPhotoUrl_NullInput() {
        String result = photoService.getValidPhotoUrl(null);

        assertEquals(DEFAULT_PHOTO, result);
        verify(photoService, never()).validateExternalUrl(anyString());
        verify(photoService, never()).validateLocalFile(anyString());
    }

    @Test
    void testGetValidPhotoUrl_EmptyInput() {
        String result = photoService.getValidPhotoUrl("");

        assertEquals(DEFAULT_PHOTO, result);
        verify(photoService, never()).validateExternalUrl(anyString());
        verify(photoService, never()).validateLocalFile(anyString());
    }
}