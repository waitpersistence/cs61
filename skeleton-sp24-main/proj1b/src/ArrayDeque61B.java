import java.util.ArrayList;
import java.util.List;

public class ArrayDeque61B <T> implements  Deque61B<T>{
    public T[] item;
    private int size;
    private int front;
    private int back;
    public int volume;
    final double PORTION = 0.25;
    public void resizeup(int volume){
        T[] newItem = (T[]) new Object[volume];
        if(isEmpty()){
            return;
        }
        if(item[0] !=null){
            for(int i = 0;i<front;i++){
                newItem[i] = this.get(i);

            }
        } else{
            for(int i = front;i <back;i++ ){
                newItem[i] = this.get(i);
            }
        }
        this.item = newItem;

    }
    public void resizedown(){
        if(volume >= 16 && size< PORTION*volume){
            this.resizeup(size*2);
        } else{
            resizeup(15);
        }

    }
    @Override
    public void addFirst(T x) {
        if(item[0]==null){//第一个还没用
            if(front!=0) {
                item[front] = x;
                front--;
            } else{
                item[front] = x;
                front=back;
            }
        }
        else{//已经用了
            item[front] = x;
            back++;
            front=back;
        }
        size++;
        //this.resizedown();
    }

    @Override
    public void addLast(T x) {
        if(item[volume-1]==null){//最后一个还没用
            if(back!=volume-1) {
                item[back] = x;
                back++;
            } else{
                item[volume-1] = x;
                back=front;
            }
        }
        else{
            item[back] = x;
            front--;
            back=front;
        }
        size++;
        //this.resizedown();
    }

    @Override
    public List<T> toList() {
        List<T> result = new ArrayList<>();
        if(item[0]!=null){
            for(int i = 0;i<back;i++){
                result.add(item[i]);
            }
        }
        else{
            for(int i = front;i<back; i++){
                result.add(item[i]);
            }
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0){
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if(isEmpty()){
            return null;
        }
        T removed=null;
        if(item[0] !=null){
            removed=item[0];
            item[0]=null;
            front = 0;

        }
        else{
            removed = item[front];
            item[front]=null;
            front++;

        }
        size--;
        this.resizedown();
        return removed;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T removed = null;
        if (item[volume-1] != null) {
            removed = item[volume - 1];
            item[volume - 1] = null;
            back = volume - 1;

        } else {
            removed = item[back-1];
            item[back-1] = null;
            back--;

        }
        size--;
        this.resizedown();
        return removed;
    }
    @Override
    public T get(int index) {
        if(index >= size || index < 0){
            return null;
        }
        return item[index];
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }
    public  ArrayDeque61B(){
        this.item = (T[]) new Object[8];
        front = item.length/3;
        back = front + 1;
        size=0;
        volume=8;
    }
}

