package AUD1;

class ComboLock {
    private int combo;
    private boolean opened;

    public ComboLock(int combo) throws BadComboException {
        if(!isItGood(combo)) {
            throw new BadComboException("Bad combo");
        }
        this.combo = combo;
        opened = false;
    }

    public boolean isItGood(int combo){
        return Integer.toString(combo).length() == 3;
    }

    public boolean open(int combo){
        this.opened = (combo == this.combo);
        return this.opened;
    }
    public boolean isOpened(){
        return opened;
    }

    public void changeCombo(int oldCombo, int newCombo){
        if (open(oldCombo) && isItGood(newCombo)) {
            this.combo = newCombo;
            this.opened = false;
            System.out.println("changed");
        }
        else{
            System.out.println("not changed");
        }
    }
    public void lock(){
        this.opened = true;
    }
}

class BadComboException extends Exception{
    String message;
    public BadComboException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

public class Lock {
    public static void main(String[] args) throws BadComboException {
        ComboLock comboLock = null;
        try {
            comboLock = new ComboLock(2);
        }
        catch (BadComboException e) {
            System.out.println(e.getMessage());
        }
        if (comboLock != null){
            System.out.println(comboLock.isOpened());
            System.out.println(comboLock.open(122));
            System.out.println(comboLock.open(100));
            comboLock.changeCombo(122,133);
            comboLock.changeCombo(100,133);
            System.out.println(comboLock.open(133));

        }
    }
}
