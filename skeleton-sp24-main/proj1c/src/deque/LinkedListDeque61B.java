package deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
//import the ArrayList class
public class LinkedListDeque61B<T> implements  Deque61B<T> {
    private  int size;
    private  Node sentinel;
    //private Node last;
    @Override
    public void addFirst(T x) {
        size++;
        Node node = new Node(x);
        node._next = sentinel._next;
        node._prev = sentinel;
        sentinel._next._prev = node;
        sentinel._next = node;
    }

    @Override
    public void addLast(T x) {
        Node node = new Node(x);
        node._prev = sentinel._prev;
        node._next = sentinel;
        sentinel._prev._next = node;
        sentinel._prev = node;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> result = new ArrayList<>();
        if (size == 0) {
            return null;
        }
        for (int i = 1; i <= size; i++) {
            result.add(get(i));
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size--;
        Node res;
        res = sentinel._next;
        sentinel._next = sentinel._next._next; //第一个节点变为第二个节点
        sentinel._next._prev = sentinel._next; //此时的第一个节点前面
        res._next = null;
        res._prev = null;
        return (T) res._item;

    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        size--;
        Node res;
        res = sentinel._prev;
        sentinel._prev = sentinel._prev._prev; //最后一个节点变为倒数第二个节点
        sentinel._prev._next = sentinel._next;
        res._next = null;
        res._prev = null;
        return (T) res._item;
    }

    @Override
    public T get(int index) {
        if (index > size || index <= 0) {
            return null;
        }
        Node node = sentinel._next;
        for (int i = 1; i < index; i++) {
            node = node._next;
        }
        return (T) node._item;
    }

    @Override
    public T getRecursive(int index) {
        return (T) getrec(sentinel._next, index);
    }
    public T getrec(Node node, int index) {
        if (index == 1) {
            return (T) node._item;
        } else {
            return (T) getrec(node._next, index - 1);
        }

    }
    public LinkedListDeque61B() {
        sentinel = new Node(null);
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }
    private class LinkedListIterator implements Iterator<T>{
        int pos = 0;

        @Override
        public boolean hasNext() {
            return (pos<size);
        }

        @Override
        public T next() {
            T returnitem = get(pos);
            pos++;
            return returnitem;
        }
    }

    public class Node<T> {
        T _item;
        Node _prev;
        Node _next;
        public Node(T item) {
            this._item = item;
            this._next = this;
            this._prev = this;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LinkedListDeque61B<?>))
            return false;
        if(((LinkedListDeque61B<?>) obj).size != this.size){
            return false;
        }
        for(int i = 0;i < this.size; i++){
            if(this.get(i) != ((LinkedListDeque61B<?>) obj).get(i) )
                return false;
        }
        return true;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size(); i++) {
            sb.append(get(i));
            if (i != size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
