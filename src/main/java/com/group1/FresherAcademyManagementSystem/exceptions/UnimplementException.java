package com.group1.FresherAcademyManagementSystem.exceptions;

public class UnimplementException extends Exception {
    public UnimplementException(String classname) {
        super(classname + " is unimplemented");
    }
}
