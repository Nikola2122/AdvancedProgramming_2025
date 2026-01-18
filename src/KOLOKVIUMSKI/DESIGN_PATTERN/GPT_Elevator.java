package KOLOKVIUMSKI.DESIGN_PATTERN;


import java.util.*;
import java.util.concurrent.BlockingQueue;

enum Direction {
    UP,
    DOWN,
    IDLE
}

interface IEState {
    void pressedButton(int floor);
}

abstract class EState implements IEState {
    Elevator elevator;

    public EState(Elevator elevator) {
        this.elevator = elevator;
    }
}

class MovingUP extends EState {

    public MovingUP(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void pressedButton(int floor) {

        elevator.queue.add(floor);

        while (!elevator.queue.isEmpty() && elevator.queue.peek() > elevator.currentFloor) {
            elevator.currentFloor = elevator.queue.poll();
            elevator.requestedFloors.add(elevator.currentFloor);
        }
        if (elevator.queue.isEmpty()) {
            this.elevator.state = new Stopped(this.elevator);
        } else {
            this.elevator.state = new MovingDOWN(this.elevator);
            this.elevator.pressButton(elevator.queue.poll());
        }
    }

}

class MovingDOWN extends EState {
    public MovingDOWN(Elevator elevator) {
        super(elevator);
    }
    @Override
    public void pressedButton(int floor) {
        elevator.queue.add(floor);

        while (!elevator.queue.isEmpty() && elevator.queue.peek() < elevator.currentFloor) {
            elevator.currentFloor = elevator.queue.poll();
            elevator.requestedFloors.add(elevator.currentFloor);
        }
        if (elevator.queue.isEmpty()) {
            this.elevator.state = new Stopped(this.elevator);
        } else {
            this.elevator.state = new MovingDOWN(this.elevator);
            this.elevator.pressButton(elevator.queue.poll());
        }
    }
}

class Stopped extends EState {
    public Stopped(Elevator elevator) {
        super(elevator);
    }

    @Override
    public void pressedButton(int floor) {

    }

}


class Elevator {
    int currentFloor;
    List<Integer> requestedFloors;
    Direction direction;
    BlockingQueue<Integer> queue;
    IEState state;

    public Elevator() {
        currentFloor = 0;
        requestedFloors = new ArrayList<>();
        direction = Direction.IDLE;
        state = new Stopped(this);
    }

    void pressButton(int floor) {

    }

    void callElevator(int floor) {

    }

    @Override
    public String toString() {
        return requestedFloors.toString();
    }
}


public class GPT_Elevator {
    static void main() {
        Elevator elevator = new Elevator();
        System.out.println(elevator);
    }
}
