package hashmap;

import org.checkerframework.checker.units.qual.C;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    public int initialCapacity = 16;
    public double loadFactor = 0.75;
    public int size = 0;
    public final int bigger = 2;
    //放进去
    //根据key获得哈希值
    @Override
    public void put(K key, V value) {
        Node node = new Node(key,value);
        int hashcode = Math.floorMod(key.hashCode(),initialCapacity);
        //先找有没有如果有，就修改值
        for(Node n : buckets[hashcode]){
            if(n.key.equals(node.key)){
                n.value = node.value;
                return;
            }
        }
        //没有
        //要resize
        size++;
        buckets[hashcode].add(node);
        double loader = size / initialCapacity;
        if(loader>=loadFactor){
            this.buckets =this.resize();
        }

    }
    public Collection[] resize(){
        //先创建一个更大的
        int newcapacity = initialCapacity*bigger;
        MyHashMap newmap = new MyHashMap(newcapacity);
        for(int i = 0;i<initialCapacity;i++){
            //提取点出来放进新的里面
            for(Node n : buckets[i]){
                  int hash=Math.floorMod(n.key.hashCode(),initialCapacity);
                  newmap.buckets[hash].add(n);
                  //newmap.put(n.key,n.value);
            }
        }
        return newmap.buckets;
    }
    //找到
    @Override
    public V get(K key) {
        int hashcode = Math.floorMod(key.hashCode(),initialCapacity);
//        if(hashcode<0||hashcode>=initialCapacity){
//            return null;
//        }
        if(buckets==null){
            return null;
        }
        for (Node node : buckets[hashcode]) {
            if (node.key.equals(key)) {
                return node.value; // 返回匹配的值
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int hashcode = Math.floorMod(key.hashCode(),initialCapacity);
        if(buckets==null){
            return false;
        }
        for (Node node : buckets[hashcode]) {
            if (node.key.equals(key)) {
                return true; // 返回匹配的值
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        buckets = null;
        size  = 0;
    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public V remove(K key) {
        int index = Math.floorMod(key.hashCode(),initialCapacity);
        Node toRemove = null;
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                toRemove = node;
                break;
            }
        }

        if (toRemove != null) {
            buckets[index].remove(toRemove);
            size--;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new mymapIterator();
    }
    public class mymapIterator implements Iterator<K> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public K next() {
            return null;
        }
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = new Collection[initialCapacity];
        for(int i = 0;i<initialCapacity;i++){
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        buckets = new Collection[initialCapacity];
        for(int i = 0;i<initialCapacity;i++){
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        buckets = new Collection[initialCapacity];
        for(int i = 0;i<initialCapacity;i++){
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        // TODO: Fill in this method.
        return new LinkedList();
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
