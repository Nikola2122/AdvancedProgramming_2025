package AUD3;

public class WrongOperatorEx extends Exception {
    public WrongOperatorEx(char operator) {
        super(String.format("operator %c caused problem", operator));
    }
}
