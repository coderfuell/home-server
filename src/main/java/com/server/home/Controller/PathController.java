package com.server.home.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.server.home.Model.PathResponse;


@RestController
@RequestMapping("/directories")
public class PathController {

    @GetMapping("/list/{*path}")
    public List<PathResponse> getMethodName(@PathVariable String path) throws IOException{
        List<PathResponse> response = new ArrayList<>();
        String home = "C:\\";
        Path toFetchIn = Paths.get(home + path);
        
        List<Path> pathList = Files.list(toFetchIn).collect(Collectors.toList());

        for (Path p: pathList){
            response.add(PathResponseModifier.getPathResponse(p));
        }      
        return response;
    }

    @GetMapping("/file/{*path}")
    public ResponseEntity<Resource> getFile(@PathVariable String path) throws IOException {
        String home = "C:\\";
        Path toFetch = Paths.get(home + path);
        Resource resource = new UrlResource(toFetch.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/create/{*path}")
    public void createDirectory(@PathVariable String path){
        
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid directory or file")
    @ExceptionHandler(IOException.class)
    public void raiseError(){}
    
}
