package Exceptions;

public abstract class NonExistingException extends Exception{
    public NonExistingException() {
    }

    public NonExistingException(String message) {
        super(message);
    }

}
