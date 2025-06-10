package com.server.home.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.server.home.Model.PathResponse;

public class ControlerModifiers {

    public static PathResponse getPathResponse(Path path) throws IOException {

        PathResponse response = new PathResponse();
        String stringPath = path.toString();

        int homeEnd = 0;
        for (int i = 0; i < stringPath.length(); i++) {
            if (stringPath.charAt(i) == '\\') {
                homeEnd = i;
                break;
            }
        }
        response.setPath(stringPath.substring(homeEnd + 1));
        response.setIsDirectory(Files.isDirectory(path));
        if (!response.getIsDirectory()) {
            response.setSize(Files.getAttribute(path, "size").toString());
        }  
        ZonedDateTime istTime = Files.getLastModifiedTime(path).toInstant().atZone(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = istTime.format(formatter);
        response.setTimeModified(formatted);
        return response;

    }

    public static ResponseEntity<Resource> getResourceResponse(Path path) throws IOException {
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
