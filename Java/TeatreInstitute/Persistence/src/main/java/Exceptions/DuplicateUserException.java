package Exceptions;

public class DuplicateUserException extends DuplicateException{

    public DuplicateUserException() {
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}
