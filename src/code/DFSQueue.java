package code;

import java.util.LinkedList;
public class DFSQueue<T> {
    private LinkedList<T> list = new LinkedList<>();

    public void add(T item) {
        list.addFirst(item);
    }

    public T remove() {
        if (isEmpty()) {
            throw new IllegalStateException("The queue is empty");
        }
        return list.removeFirst();
    }
    public void remove(T item) {
        list.remove(item);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("The queue is empty");
        }
        return list.getFirst();
    }
    public void clear() {
    	list.clear();
    }

}
