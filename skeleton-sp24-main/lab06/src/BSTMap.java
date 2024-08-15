import edu.princeton.cs.algs4.SET;
import org.apache.commons.collections.MapIterator;
/**
 * 注意递归
 * 后面写Blog的时候注意
 * */
import java.util.*;

//实现二叉树，太复杂了，要学会写helper
public class BSTMap<K extends Comparable<K>,V> implements Map61B{
    public class Node{
        public K key;
        public V value;
        public Node left;
        public Node right;
        Node(K key, V value){
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }
    BSTMap(){
        size = 0;
        root = null;
    }
    private Node root;
    public int size;
    @Override
    public void put(Object key, Object value) {
        Node n = new Node((K) key, (V) value);
        if(root == null){
            root = n;//初始化时 root就是根
            size++;
        }else{
            put_helper(key,value,root);
            size++;
        }

    }
    //put helper
    public void put_helper(Object key, Object value,Node s){
        Node n = new Node((K) key, (V) value);
        if(s.key == n.key){
            s.value=n.value;
            size--;
        }else if(s.key.compareTo(n.key) < 0){
            if(s.right != null){
                put_helper(key,value, s.right);
            }else{
                s.right = n;
            }
        }else if(s.key.compareTo(n.key) > 0){
            if(s.left != null){
                put_helper(key,value,s.left);
            }else{
                s.left = n;
            }
        }

    }


    @Override
    public Object get(Object key) {
        if(root == null){
            return null;
        }
        V re_value = get_helper((K) key, root).value;
        return re_value;
    }
    //get helper
    public Node get_helper(K key,Node n) {
        if(n == null){
            return null;
        }
        if(n.key.compareTo((K) key)==0){
            return n;
        } else if (key.compareTo(n.key) < 0) {
            return get_helper(key,n.left);
        } else if (key.compareTo(n.key) > 0) {
            return get_helper(key, n.right);
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if(get_helper((K) key,root)!=null){
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;

    }

    @Override
    public Set keySet() {
        Set kSet = (Set<K>) new SET<>();
        MapIterator m = new MapIterator();
        for(Node n : m.stack){
            kSet.add(n.key);
        }
        return kSet;
    }

    /*
    Hibbard 删除的步骤
    在二叉搜索树中删除节点时，需要考虑以下几种情况：

    删除的节点是叶子节点（没有子节点）。
    删除的节点只有一个子节点。
    删除的节点有两个子节点。
    * */
    @Override
    public Object remove(Object key) {

        return null;
    }
    public Node remove_helper(K key){

        Node n = get_helper(key,root);
        if(n==null){
            return null;
        }
        Node parent = parent(key,root);
        //叶子
        if(n.left==null&&n.right==null){
            if(parent.left==n){
                parent.left=null;
            } else if (parent.right==n) {
                parent.right=null;
            }
        }
        //一个的
        if(n.left!=null) {//左边不是空
            if (zuoyou(n, parent)) {
                //是右边 真
                parent.right = n.left;
                n.left=null;
            } else {
                parent.left = n.left;
                n.left=null;
            }
        } else if (n.right!=null) {//右边不是空
            if(zuoyou(n,parent)){
                parent.right=n.right;
                n.right=null;
            }else {
                parent.left=n.right;
                n.right=null;
            }
        }
        return n;
        //两个都有
        // 情况3：节点有两个子节点

    }
    class Node {
        int key;
        Node left, right;

        public Node(int item) {
            key = item;
            left = right = null;
        }
    }

    class BinarySearchTree {
        Node root;

        BinarySearchTree() {
            root = null;
        }

        // 插入新节点
        void insert(int key) {
            root = insertRec(root, key);
        }

        Node insertRec(Node root, int key) {
            if (root == null) {
                root = new Node(key);
                return root;
            }

            if (key < root.key)
                root.left = insertRec(root.left, key);
            else if (key > root.key)
                root.right = insertRec(root.right, key);

            return root;
        }

        // 删除指定的节点
        void deleteKey(int key) {
            root = deleteRec(root, key);
        }

        Node deleteRec(Node root, int key) {
            if (root == null) {
                return root;
            }
            /**这一步非常重要此时的节点是root
             * 比的值是root.left的值
             * 并根据这个去修改root.left的引用，这个递归要抓住！！！！
             * */
            if (key < root.key) {
                root.left = deleteRec(root.left, key);
            } else if (key > root.key) {
                root.right = deleteRec(root.right, key);
            } else {
                // 找到要删除的节点

                // 情况1：节点是叶子节点
                if (root.left == null && root.right == null) {
                    return null; // 直接删除叶子节点
                }

                // 情况2：节点只有一个子节点
                if (root.left == null)
                    return root.right;
                else if (root.right == null)
                    return root.left;

                // 情况3：节点有两个子节点，找到右子树的最小值节点（后继节点）
                root.key = minValue(root.right);

                // 删除后继节点
                root.right = deleteRec(root.right, root.key);
            }

            return root;
        }

        int minValue(Node root) {
            int minv = root.key;
            while (root.left != null) {
                minv = root.left.key;
                root = root.left;
            }
            return minv;
        }

        // 中序遍历
        void inorder() {
            inorderRec(root);
        }

        void inorderRec(Node root) {
            if (root != null) {
                inorderRec(root.left);
                System.out.print(root.key + " ");
                inorderRec(root.right);
            }
        }
    }

    /**找到父节点*/
    public Node parent(K key,Node n){
        if(root==null){
            return null;
        }
        if(key.compareTo(n.left.key)==0){
            return n;
        }else if(key.compareTo(n.right.key)==0){
            return n;
        }else if(key.compareTo(n.key)<0){
            return parent(key,n.left);
        } else if (key.compareTo(root.key)>0) {
            return parent(key,n.right);
        }
        return null;

    }
    public boolean zuoyou(Node n,Node parent){
        if(parent.left==n){
            parent.left=null;
            return false;//左边是假
        } else if (parent.right==n) {
            parent.right=null;
            return true;//右边是真
        }
        return false;
    }


    @Override
    public Iterator iterator() {
        return new MapIterator();
    }
    public class MapIterator implements  Iterator{
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            Node node = stack.pop();
            K key = node.key;
            if(node.right != null){
                pushLeft(node.right);//提取出来，检测右边一条条左侧
                //递归
            }
            return key;

        }
        //用栈保存站点，先保存左侧
        private Stack<Node> stack = new Stack<>();
        MapIterator(){
            pushLeft(root);
        }
        //左侧全部压入栈内.
        private void pushLeft(Node node){
            while(node !=null){
                stack.push(node);
                node = node.left;
            }
        }

    }









}
