package com.server.home.Dto;

public class CreateDirectoryDto {
    private String directoryName;

    public CreateDirectoryDto() {
    }

    public CreateDirectoryDto(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }    
}
