package com.server.home.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.server.home.Model.PathResponse;

public class PathResponseModifier {
    public static PathResponse getPathResponse(Path path) throws IOException{

        PathResponse response = new PathResponse();
        String stringPath = path.toString();

        int homeEnd = 0;
        for (int i = 0; i < stringPath.length(); i++){
            if (stringPath.charAt(i) == '\\'){
                homeEnd = i;
                break;
            }
        }

        response.setPath(stringPath.substring(homeEnd+1));
        response.setIsDirectory(Files.isDirectory(path));
        ZonedDateTime istTime = Files.getLastModifiedTime(path).toInstant().atZone(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = istTime.format(formatter);
        response.setTimeModified(formatted);
        return response;

    }
}
