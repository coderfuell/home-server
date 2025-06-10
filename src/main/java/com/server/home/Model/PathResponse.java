package com.server.home.Model;


public class PathResponse {
    private String path;
    private boolean isDirectory;
    private String timeModified;
    private String size;

    public PathResponse() {
    }

    public PathResponse(String path, boolean isDirectory, String timeModified) {
        this.path = path;
        this.isDirectory = isDirectory;
        this.timeModified = timeModified;
    }

    public String getPath() {
        return this.path;
    }

    public boolean getIsDirectory(){
        return this.isDirectory;
    }

    public String getTimeModified(){
        return this.timeModified;
    }

    public String getSize() {
        return size;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setIsDirectory(boolean isDirectory){
        this.isDirectory = isDirectory;
    }

    public void setTimeModified(String timeModified){
        this.timeModified = timeModified;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
