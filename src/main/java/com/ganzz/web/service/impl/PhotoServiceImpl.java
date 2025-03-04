package com.ganzz.web.service.impl;

import com.ganzz.web.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;

@Slf4j
@Service
public class PhotoServiceImpl implements PhotoService {

    @Value("${app.default-photo}")
    private String defaultPhoto;

    public String getValidPhotoUrl(String photoUrl) {
        if (photoUrl == null || photoUrl.isEmpty()) {
            return defaultPhoto;
        }

        try {
            if (photoUrl.startsWith("http://") || photoUrl.startsWith("https://")) {
                return validateExternalUrl(photoUrl);
            }

            return validateLocalFile(photoUrl);

        } catch (Exception e) {
            log.error("Error validating photo URL: {}", photoUrl, e);
            return defaultPhoto;
        }
    }

    protected String validateExternalUrl(String photoUrl) {
        try {
            URI uri = new URI(photoUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return photoUrl;
            }
        } catch (Exception e) {
            log.warn("Failed to validate external URL: {}", photoUrl, e);
        }
        return defaultPhoto;
    }

    protected String validateLocalFile(String photoUrl) {
        try {
            Path photoPath = Paths.get(photoUrl);
            if (Files.exists(photoPath) && Files.isReadable(photoPath)) {
                return photoUrl;
            }
        } catch (Exception e) {
            log.warn("Failed to validate local file: {}", photoUrl, e);
        }
        return defaultPhoto;
    }
}