package com.scm.scm_project.helper;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(){
        
    }
    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
