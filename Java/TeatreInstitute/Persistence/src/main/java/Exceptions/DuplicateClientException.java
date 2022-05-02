package Exceptions;

public class DuplicateClientException extends DuplicateException{

    public DuplicateClientException() {
    }

    public DuplicateClientException(String message) {
        super(message);
    }
}
