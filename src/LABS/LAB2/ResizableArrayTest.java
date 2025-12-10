package LABS.LAB2;

import java.util.*;
import java.util.stream.Collectors;

class ResizableArray <T>{
    private T[] array;
    private int size;

    ResizableArray(){
        array = (T[]) new Object[0];
        size = 0;
    }

    void addElement(T element){
        if(size == array.length){
            T [] newArray = (T[]) new Object[size+1];
            if(size!=0) {
                System.arraycopy(array, 0, newArray, 0, size);
            }
            newArray[size++] = element;
            array = newArray;
        }
        else{
            array[size++] = element;
        }
    }

    boolean removeElement(T element){
        if(size == 0){
            return false;
        }
        else{
            if(contains(element)){
                array = (T[]) Arrays.stream(array).filter(f->!f.equals(element)).toArray();
                size--;
                return true;
            }
            else{
                return false;
            }
        }
    }

    boolean contains(T element){
        return Arrays.stream(array).limit(size).filter(f->f.equals(element)).findFirst().isPresent();
    }
    Object [] toArray(){
        return array;
    }
    boolean isEmpty(){
        return size == 0;
    }

    void ensureCapacity(int minCapacity){
        T [] newArray = (T[]) new Object[minCapacity + count()];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    public void setSize(int size) {
        this.size = size;
    }

    int count(){
        return size;
    }

    T elementAt(int idx){
        if (idx<0 || idx>=size){
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[idx];
    }
    static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
        if (src.count() == 0) return;
        dest.ensureCapacity(dest.count() + src.count());
        System.arraycopy(
                src.toArray(), 0,
                dest.toArray(), dest.count(),
                src.count()
        );
        dest.setSize(dest.count() + src.count());
    }
}

class IntegerArray extends ResizableArray<Integer>{

    double sum(){
        if(count()==0){
            return 0;
        }
        return Arrays.stream(toArray()).mapToInt(i-> (int)i).sum();
    }
    double mean(){
        if(count()==0){
            return 0;
        }
        return Arrays.stream(toArray()).mapToDouble(i-> (double) i).average().getAsDouble();
    }
    int countNonZero(){
        return (int) Arrays.stream(toArray()).mapToInt(i -> (int) i).filter(i -> i!=0).count();
    }

    IntegerArray distinct(){
        Object [] arr2 = Arrays.stream(toArray()).distinct().toArray();
        IntegerArray arr3 = new IntegerArray();
        for (Object el: arr2){
            arr3.addElement((int) el);
        }
        return arr3;
    }

    IntegerArray increment(int offset){
        Object [] arr2 = Arrays.stream(toArray()).map(i-> {
            int el = (int) i;
            el += offset;
            return el;
        }).toArray();
        IntegerArray arr3 = new IntegerArray();
        for (Object el: arr2){
            arr3.addElement((int) el);
        }
        return arr3;
    }
}



public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
