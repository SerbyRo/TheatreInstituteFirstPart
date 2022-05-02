package Exceptions;

public abstract class DuplicateException extends Exception{

    public DuplicateException() {
    }

    public DuplicateException(String message) {
        super(message);
    }
}