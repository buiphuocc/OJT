package com.group1.FresherAcademyManagementSystem.exceptions;

public class StatusMismatchException extends RuntimeException {
    public StatusMismatchException() {
        super("Student status in batch is mismatch");
    }
}
