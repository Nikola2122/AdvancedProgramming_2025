package KOLOKVIUMSKI.DESIGN_PATTERN;
import java.util.*;

interface Observer {
    void update(float temperature, float humidity, float pressure);
    int level();
}

abstract class WeatherObserver implements Observer {

    WeatherObserver(WeatherDispatcher dispatcher) {
        dispatcher.register(this);
    }
}

class CurrentConditionsDisplay extends WeatherObserver {

    CurrentConditionsDisplay(WeatherDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        System.out.println("Temperature: " + temperature + "F\n" + "Humidity: " + humidity + "%");
    }

    @Override
    public int level() {
        return 2;
    }
}

class ForecastDisplay extends WeatherObserver {
    float oldPressure;

    ForecastDisplay(WeatherDispatcher dispatcher) {
        super(dispatcher);
        oldPressure = 0f;
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        if (oldPressure == pressure) {
            System.out.println("Forecast: Same");
        } else if (oldPressure < pressure) {
            System.out.println("Forecast: Improving");
        }
        else{
            System.out.println("Forecast: Cooler");
        }
        oldPressure = pressure;
    }

    @Override
    public int level() {
        return 1;
    }
}

class WeatherDispatcher {
    Set<Observer> observers;
    float temperature;
    float humidity;
    float pressure;

    WeatherDispatcher() {
        this.observers = new HashSet<>();
        this.temperature = 0f;
        this.humidity = 0f;
        this.pressure = 0f;
    }

    public void register(Observer o) {
        this.observers.add(o);
    }

    public void remove(Observer o) {
        this.observers.remove(o);
    }

    private void notifyObservers() {
        observers.stream()
                .sorted(Comparator.comparing(Observer::level, Comparator.reverseOrder()))
                .forEach(o -> o.update(temperature, humidity, pressure));
    }

    public void setMeasurements(float v, float v1, float v2) {
        this.temperature = v;
        this.humidity = v1;
        this.pressure = v2;
        notifyObservers();
        System.out.println();
    }
}
public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}