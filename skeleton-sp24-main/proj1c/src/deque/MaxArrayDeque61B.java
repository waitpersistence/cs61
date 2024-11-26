package deque;
import  java.util.Comparator;
public class MaxArrayDeque61B<T> extends ArrayDeque61B<T>{
    public Comparator<T> getcomparator(){
        return new comparator();
    }
    public class comparator implements Comparator<T>{
        @Override
        public int compare(T o1, T o2) {
            return 01-02;
        }
    }
    private Comparator cpr;
    public MaxArrayDeque61B(Comparator<T> c){
        cpr = c;
    };
    public T max(){
        if(size()==0){
            return null;
        }
        T MAX = this.get(0);
        for(int i = 0;i < size(); i++){
            if(cpr.compare(MAX,this.get(i))<0) {
                MAX = this.get(i);
            }
        }
        return MAX;
    }
    public T max(Comparator<T> c){
        if(this.size()==0){
            return null;
        }
        T max = this.get(0);
        for (int i = 1; i < size(); i++) {
            if (c.compare(max, get(i)) < 0) {
                max = get(i);
            }
        }
        return max;

    }


}
