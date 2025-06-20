package com.server.home.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.home.Dto.CreateDirectoryDto;
import com.server.home.Exception.IsDirectoryException;
import com.server.home.Model.PathResponse;
import com.server.home.Services.ControllerService;

@RestController
@RequestMapping("/directories")
public class PathController {
    @Autowired
    private ControllerService controllerService;

    @GetMapping("/list/{*path}")
    public List<PathResponse> getMethodName(@PathVariable String path) throws IOException {
        List<PathResponse> response = new ArrayList<>();
        String home = "C:\\";
        Path toFetchIn = Paths.get(home + path);
        System.out.println("");
        System.out.println(Files.exists(toFetchIn));
        if (!Files.exists(toFetchIn)) {
            throw new FileNotFoundException();
        }

        List<Path> pathList = Files.list(toFetchIn).collect(Collectors.toList());

        for (Path p : pathList) {
            response.add(controllerService.getPathResponse(p));
        }
        return response;
    }

    @GetMapping("/file/{*path}")
    public ResponseEntity<Resource> getFile(@PathVariable String path) throws IOException {
        String home = "C:\\";
        Path toFetch = Paths.get(home + path);

        if (!Files.exists(toFetch)) {
            throw new FileNotFoundException();
        }

        if (Files.isDirectory(toFetch)) {
            throw new IsDirectoryException();
        }

        return controllerService.getResourceResponse(toFetch);
    }

    @PostMapping("/create/{*path}")
    public ResponseEntity<Void> createDirectory(@PathVariable String path,  @RequestBody CreateDirectoryDto body) throws IOException {
        String home = "C:";
        Path toCreateIn = Paths.get(home + path);

        Path newCreated = Paths.get(home + path + "\\" + body.getDirectoryName());
        if (!Files.exists(toCreateIn)) {
            throw new FileNotFoundException();
        }
        if (!Files.isDirectory(toCreateIn)){
            throw new NotDirectoryException(toCreateIn.toString());
        }
        Files.createDirectory(newCreated);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload/{*path}")
    public ResponseEntity<Void> uploadFile(@PathVariable String path,@RequestParam MultipartFile file) throws IOException{
        String home = "C:";
        Path toSaveIn = Paths.get(home + path);
        if (!Files.exists(toSaveIn)) {
            throw new FileNotFoundException();
        }
        if (!Files.isDirectory(toSaveIn)) {
            throw new NotDirectoryException(toSaveIn.toString());
        }
        
        String fileName = file.getOriginalFilename();
        InputStream fileIn = file.getInputStream();

        Path filePath = Paths.get(home + path + "/" + fileName);
        
        if (!Files.exists(filePath)){
            Files.createFile(filePath);
        }

        Files.copy(fileIn, filePath, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok().build();

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "path refers to a file")
    @ExceptionHandler(NotDirectoryException.class)
    public void notDirectory() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "path refers to a directory")
    @ExceptionHandler(IsDirectoryException.class)
    public void invalidFileDownload() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "invalid path")
    @ExceptionHandler(FileNotFoundException.class)
    public void fileNotfound() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid directory or file")
    @ExceptionHandler(IOException.class)
    public void raiseError() {
    }

}
