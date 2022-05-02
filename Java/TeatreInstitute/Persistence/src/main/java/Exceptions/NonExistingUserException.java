package Exceptions;

public class NonExistingUserException extends NonExistingException{
    public NonExistingUserException() {
    }

    public NonExistingUserException(String message) {
        super(message);
    }
}
