package AUD3;

public class InvalidBalanceException extends Exception {

    public InvalidBalanceException(double balance, double amount) {
        super(String.format("Can't withdraw %.2f, you only have %.2f.", amount, balance));
    }
}
