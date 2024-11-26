import deque.ArrayDeque61B;
import org.junit.jupiter.api.Test;
import deque.LinkedListDeque61B;
import static com.google.common.truth.Truth.assertThat;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import static com.google.common.truth.Truth.assertWithMessage;
public class ArrayDeque61BTest {
    @Test
    public void toStringTest(){
        ArrayDeque61B<Integer> a = new ArrayDeque61B<>();
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        String s ="[3, 2, 1]";
        assertEquals(a.toString(),s);
    }
    @Test
    public void IteratorTest(){
        ArrayDeque61B<Integer> a = new ArrayDeque61B<>();
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        int sum = 0;
        for(int n : a){
            sum = sum + n;
        }
        assertEquals(6,sum);

    }
}
