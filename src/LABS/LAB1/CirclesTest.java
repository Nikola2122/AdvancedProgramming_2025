package LABS.LAB1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable{
    void moveUp();
    void moveDown();
    void moveRight();
    void moveLeft();
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();
    String throwing();
    boolean isValidPos(int max_x, int max_y);
}

class ObjectCanNotBeMovedException extends RuntimeException {
    public ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends RuntimeException {
    public MovableObjectNotFittableException(String message) {
        super(message + " can not be fitted into the collection");
    }
}

class MovablePoint implements Movable{
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() {
        if(this.y + ySpeed > MovablesCollection.y_MAX){
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);
        }
        this.y += ySpeed;
    }

    @Override
    public void moveDown() {
        if(this.y - ySpeed < 0){
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);
        }
        this.y -= ySpeed;
    }

    @Override
    public void moveRight() {
        if(this.x + xSpeed > MovablesCollection.x_MAX){
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        }
        this.x += xSpeed;
    }

    @Override
    public void moveLeft() {
        if(this.x - xSpeed < 0){
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        }
        this.x -= xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public String throwing() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }

    @Override
    public boolean isValidPos(int max_x, int max_y) {
        if(getCurrentYPosition() > max_y){
            return false;
        } else if (getCurrentYPosition() < 0) {
            return false;
        } else if (getCurrentXPosition() > max_x) {
            return false;
        } else if (getCurrentXPosition() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }
}

class MovableCircle implements Movable{
    int radius;
    MovablePoint movingPoint;
    int upperLimit;
    int downLimit;
    int leftLimit;
    int rightLimit;

    public MovableCircle(int radius, MovablePoint movingPoint) {
        this.radius = radius;
        this.movingPoint = movingPoint;
        this.upperLimit = movingPoint.getCurrentYPosition() + radius;
        this.downLimit = movingPoint.getCurrentYPosition() - radius;
        this.leftLimit = movingPoint.getCurrentXPosition() - radius;
        this.rightLimit = movingPoint.getCurrentXPosition() + radius;
    }

    @Override
    public void moveUp() {
        movingPoint.moveUp();
    }

    @Override
    public void moveDown() {
        movingPoint.moveDown();
    }

    @Override
    public void moveRight() {
        movingPoint.moveRight();
    }

    @Override
    public void moveLeft() {
        movingPoint.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return movingPoint.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return movingPoint.getCurrentYPosition();
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public String throwing() {
        return String.format("Movable circle with center (%d,%d) and radius %d",
                movingPoint.getCurrentXPosition(),
                movingPoint.getCurrentYPosition(),
                radius);
    }

    @Override
    public boolean isValidPos(int max_x, int max_y) {
        if (upperLimit > max_y) {
            return false;
        } else if (downLimit < 0) {
            return false;
        } else if (rightLimit > max_x) {
            return false;
        } else if (leftLimit < 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d",
                movingPoint.getCurrentXPosition(),
                movingPoint.getCurrentYPosition(),
                radius);
    }
}

class MovablesCollection{
    private List<Movable> movables;
    static public int x_MAX;
    static public int y_MAX;

    public MovablesCollection(int x_MAX, int y_MAX){
        this.movables = new ArrayList<>();
        MovablesCollection.x_MAX = x_MAX;
        MovablesCollection.y_MAX = y_MAX;
    }

    void addMovableObject(Movable m){
        if(m.isValidPos(x_MAX, y_MAX)){
            movables.add(m);
        }
        else{
            throw new MovableObjectNotFittableException(m.throwing());
        }
    }

    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction){
        movables.forEach(m->{
            if(m.getType().equals(type)){
                try{
                    switch(direction){
                        case UP:
                            m.moveUp();
                            break;
                        case DOWN:
                            m.moveDown();
                            break;
                        case LEFT:
                            m.moveLeft();
                            break;
                        case RIGHT:
                            m.moveRight();
                    }
                }catch (ObjectCanNotBeMovedException e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Collection of movable objects with size %d:\n", movables.size()));
        sb.append(movables.stream().map(Object::toString).collect(Collectors.joining("\n")));
        sb.append("\n");
        return sb.toString();
    }

    static void setxMax(int x){
        x_MAX = x;
    }
    static void setyMax(int y){
        y_MAX = y;
    }
}


public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                }
                catch (MovableObjectNotFittableException ex){
                    System.out.println(ex.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }
                catch (MovableObjectNotFittableException ex){
                    System.out.println(ex.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
