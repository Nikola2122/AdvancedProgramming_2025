package AUD5;

import java.util.ArrayList;
import java.util.Collections;

class PriorityQueueElement<E> implements Comparable<PriorityQueueElement<E>> {
    private int priority;
    private E element;

    public PriorityQueueElement(E element, int priority) {
        this.priority = priority;
        this.element = element;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return String.format("Priority: %d, Element: %s", priority, element);
    }


    @Override
    public int compareTo(PriorityQueueElement o) {
        return Integer.compare(o.getPriority(), this.priority);
    }
}

public class PriorityQueue <T extends Comparable<T>>{
    private ArrayList<PriorityQueueElement<T>> list;

    public PriorityQueue() {
        list = new ArrayList<PriorityQueueElement<T>>();
    }

    void addElement(T element, int priority) {
        PriorityQueueElement<T> el = new PriorityQueueElement<>(element, priority);
        list.add(el);
        Collections.sort(list);
    }

    T removeElement (){
        if(list.isEmpty()){
            return null;
        }
        return list.removeFirst().getElement();
    }

    public static void main(String[] args) {
        PriorityQueue<String> pq = new PriorityQueue<String>();
        pq.addElement("nikola", 0);
        pq.addElement("nikola2", 100);
        pq.addElement("nikola3", 15);
        pq.addElement("nikola4", 20);
        System.out.println(pq.removeElement());
        System.out.println(pq.removeElement());
        System.out.println(pq.removeElement());
        System.out.println(pq.removeElement());
    }
}
