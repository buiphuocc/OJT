package com.group1.FresherAcademyManagementSystem.exceptions;

import java.io.Serial;

public class ClassEntityNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1;

    public ClassEntityNotFoundException(String message){
        super(message);
    }
}
