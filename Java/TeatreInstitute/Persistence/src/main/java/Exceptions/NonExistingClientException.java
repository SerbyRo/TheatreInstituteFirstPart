package Exceptions;

public class NonExistingClientException extends NonExistingException{
    public NonExistingClientException() {
    }

    public NonExistingClientException(String message) {
        super(message);
    }
}
