package com.server.home.Model;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PathList {

    public static List<Path> getPathList(String uri) {
        Path path = Paths.get(uri);

        // Stream<Path> stream = Files.list(Paths.get())
        return null;
    }

    public static void main(String[] args) throws IOException{
        FileSystem fs = FileSystems.getDefault();

        Iterable<Path> it = fs.getRootDirectories();

        for (Path p: it){
            System.out.println(p);
        }

        System.out.println(fs);

        Object o = Files.getAttribute(Paths.get("C:\\D\\programming\\kaushik\\home\\trialDirectory"), "size");
        System.out.println(o.toString());
    }

}
