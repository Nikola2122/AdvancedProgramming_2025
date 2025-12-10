package AUD3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface InterestBearingAccount {
    public void addInterest();
}

abstract class Account {
    private String name;
    private double balance;
    private int number;
    public static int INC = 1;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.number = INC++;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public int getNumber() {
        return number;
    }

    public abstract TypeAcc getType();

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void withdraw(double amount) throws InvalidBalanceException {
        if (amount > balance) {
            throw new InvalidBalanceException(balance, amount);
        } else balance -= amount;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public String toString() {
        return "Account [name=" + name + ", balance=" + balance + ", number=" + number + "]";
    }
}

class InterestCheckingAccount extends Account implements InterestBearingAccount {

    public static double interest = 0.03;

    public InterestCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public TypeAcc getType() {
        return TypeAcc.INTEREST;
    }

    @Override
    public void addInterest() {
        this.deposit(this.getBalance() * interest);
    }
}

class PlatinumCheckingAccount extends InterestCheckingAccount {

    public PlatinumCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public void addInterest() {
        this.deposit(this.getBalance() * (2 * interest));
    }
}

class NonInterestCheckingAccount extends Account {
    public NonInterestCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public TypeAcc getType() {
        return TypeAcc.NOINTEREST;
    }
}

public class Bank {
    private List<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
        this.accounts.add(new InterestCheckingAccount("Nikola",100));
        this.accounts.add(new PlatinumCheckingAccount("Meli",100));
        this.accounts.add(new NonInterestCheckingAccount("Ivo",100));
    }

    double totalMoney() {
        return accounts.stream().mapToDouble(Account::getBalance).sum();
    }

    void addInterestToAll() {
        accounts.stream().
                filter(a -> a.getType().equals(TypeAcc.INTEREST)).
                map(a -> (InterestBearingAccount) a).
                forEach(InterestBearingAccount::addInterest);
    }

    @Override
    public String toString() {
        return accounts.stream().map(Account::toString).collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        Bank bank = new Bank();
        System.out.println(bank.totalMoney());
        System.out.println(bank);
        bank.addInterestToAll();
        System.out.println();
        System.out.println(bank.totalMoney());
        System.out.println(bank);
    }
}
