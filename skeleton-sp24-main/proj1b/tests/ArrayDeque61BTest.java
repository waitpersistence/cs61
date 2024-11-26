import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

    //     @Test
//     @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
//     void noNonTrivialFields() {
//         List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
//                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
//                 .toList();
//
//         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
//     }
    //constructor addfirst addlast Test
    @Test
    public void constructorTest() {
        ArrayDeque61B<Integer> a = new ArrayDeque61B<>();
        a.addFirst(12);
        a.addLast(3);
        a.addFirst(1);
        a.addLast(2);
        a.addFirst(1);
        a.addFirst(3);
        assertThat(a.get(3)).isEqualTo(3);
    }

    @Test
    public void toListTest() {
        ArrayDeque61B<Integer> a = new ArrayDeque61B<>();
        a.addFirst(12);
        a.addLast(3);
        a.addFirst(1);
        a.addLast(2);
        a.addFirst(1);
        a.addFirst(3);//fornt=5 back =5
        List<Integer> list = a.toList();
        assertThat(a.toList()).containsExactly(1, 1, 12, 3, 2, 3).inOrder();

    }


    @Test
    public void removeTest() {
        ArrayDeque61B<Integer> a = new ArrayDeque61B<>();
        a.addFirst(12);
        a.addLast(3);
        a.addFirst(1);
        a.addLast(2);
        a.addFirst(1);
        a.addFirst(3);
        a.removeFirst();//front=6 back=6
        a.removeLast();
        assertThat(a.toList()).containsExactly( 1, 12, 3, 2, 3).inOrder();
    }
    @Test
    public void resizeupTest(){
        ArrayDeque61B<Integer> a = new ArrayDeque61B<>();
        a.addFirst(12);
        a.addLast(3);
        a.addFirst(1);
        a.addLast(2);
        a.addFirst(1);
        a.addFirst(3);
        a.resizeup(10);
    }
    @Test
    public void resizedownTest(){
        ArrayDeque61B<Integer> a = new ArrayDeque61B<>();
        a.addFirst(12);
        a.addLast(3);
        a.addFirst(1);
        a.addLast(2);
        a.addFirst(1);
        a.addFirst(3);
        a.getRecursive(2);
    }
}