package AUD3;

import java.util.Scanner;

interface Strategy{
    public double execute(double value1, double value2);
}

class add implements Strategy{
    @Override
    public double execute(double value1, double value2) {
        return value1 + value2;
    }
}
class subtract implements Strategy{
    @Override
    public double execute(double value1, double value2) {
        return value1 - value2;
    }
}
class multiply implements Strategy{
    @Override
    public double execute(double value1, double value2) {
        return value1 * value2;
    }
}
class divide implements Strategy{
    @Override
    public double execute(double value1, double value2) {
        return value1 / value2;
    }
}
public class Calculator {
    private double result;
    private Strategy strategy;

    public Calculator() {
        System.out.println("Calculator is on");
        result = 0.0;
    }

    public String execute(char operation, double value) throws WrongOperatorEx {
        if(operation == '+'){
            strategy = new add();
        }
        else if(operation == '-'){
            strategy = new subtract();
        }
        else if(operation == '*') {
            strategy = new multiply();
        }
        else if(operation == '/' && value != 0) {
            strategy = new divide();
        }
        else throw new WrongOperatorEx(operation);

        result = strategy.execute(result, value);
        return String.format("Result %c %.2f = %.2f \n New result: %.2f", operation,value,result,result);
    }

    public void reset(){
        result = 0.0;
    }

    public String getResult(){
        return String.format("Result = %.2f", result);
    }

    public String getFinal(){
        return String.format("Final result = %.2f", result);
    }
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner sc = new Scanner(System.in);
        String line;
        while(true){
            line = sc.nextLine();
            if(line.length()==1){
                char c = line.toLowerCase().charAt(0);
                if (c=='r'){
                    System.out.println(calculator.getFinal());
                    System.out.println("Y/N again?");
                    line = sc.nextLine();
                    c = line.toLowerCase().charAt(0);
                    if (c=='y'){
                        calculator.reset();
                        System.out.println(calculator.getResult());
                    }
                    else{
                        System.out.println("END");
                        break;
                    }
                }
            }
            else{
                String [] arr = line.split(" ");
                char operation = arr[0].charAt(0);
                double value = Double.parseDouble(arr[1]);
                try {
                    System.out.println((calculator.execute(operation,value)));
                } catch (WrongOperatorEx e) {
                    System.err.println(e.getMessage());
                    System.out.println("Try again");
                }
            }
        }
    }
}
