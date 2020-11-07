package exceptions;

public class conditionalJumpException extends Exception{
    public conditionalJumpException(String jumpTo) {
        super(jumpTo);
    }
}
