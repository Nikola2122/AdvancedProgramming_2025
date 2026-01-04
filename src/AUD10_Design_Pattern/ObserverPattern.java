package AUD10_Design_Pattern;


import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(float temperature);
}

abstract class ObserverAdapter implements Observer {
    Subject s;
    public ObserverAdapter(Subject s) {
        this.s = s;
    }
}

interface Subject {
    void registerObserver(Observer o);

    void unregisterObserver(Observer o);

    void notifyObservers();
}

class WeatherStation implements Subject {

    int temperature;
    List<Observer> observers;

    public WeatherStation(int temperature) {
        this.temperature = temperature;
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        this.observers.remove(o);
    }

    private void updateObservers() {
        for (Observer o : observers) {
            o.update(temperature);
        }
    }

    @Override
    public void notifyObservers() {
        this.updateObservers();
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        this.notifyObservers();
    }
}

class PhoneObserver extends ObserverAdapter {

    public PhoneObserver(Subject s) {
        super(s);
        s.registerObserver(this);
    }

    @Override
    public void update(float temperature) {
        System.out.println("Phone Observer temp: " + temperature);
    }
}

class WatchObserver implements Observer {

    @Override
    public void update(float temperature) {
        System.out.println("Watch Observer temp: " + temperature);
    }
}

public class ObserverPattern {
    static void main() {
        WeatherStation s1 = new WeatherStation(1);
        PhoneObserver phone = new PhoneObserver(s1);
        WatchObserver watch = new WatchObserver();
        s1.registerObserver(phone);
        s1.registerObserver(watch);

        s1.setTemperature(10);
    }
}
