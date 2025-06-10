package com.server.home.Exception;

import java.io.IOException;

public class IsDirectoryException extends IOException {
    public IsDirectoryException() {
    }

    public IsDirectoryException(String m){
        super(m);
    }
    
}
