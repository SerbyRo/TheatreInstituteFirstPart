package Domain;

public class TheatreException extends Exception{
    public TheatreException(){

    }
    public TheatreException(String message) {
        super(message);
    }

    public TheatreException(String message, Throwable cause) {
        super(message, cause);
    }
}
