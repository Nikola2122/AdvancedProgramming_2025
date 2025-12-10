package AUD7;

import java.util.List;
import java.util.Random;

class InvalidPickerArguments extends Exception {
    public InvalidPickerArguments(String message) {
        super(message);
    }
}

class FinalistPick{
    int finalists;
    static public Random RANDOM = new Random();

    public FinalistPick(int finalists) {
        this.finalists = finalists;
    }

    public List<Integer> pick(int n){
        if(n>finalists || n <= 0){
            return null;
        }
        return null;
    }
}

public class FinalistTest {
    public static void main(String[] args) {
        
    }
}
