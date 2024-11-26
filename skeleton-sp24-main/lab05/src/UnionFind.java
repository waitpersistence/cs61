public class UnionFind {
    // TODO: Instance variables
    private int[] item;
    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        item = new int[N];
        for(int i = 0; i <N;i++){
            item[i] = -1;
        }
        // TODO: YOUR CODE HERE
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        int parent = v;
        while(item[parent]>0){
            parent = item[parent];
        }
        return -item[parent];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        // TODO: YOUR CODE HERE
        int parent = item[v];
        return parent;
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if(find(v1) == find(v2)){
            return true;
        }else {
        return false;
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) throws IllegalArgumentException{
        // TODO: YOUR CODE HERE
        if(v<0||v>item.length){
            throw  new IllegalArgumentException("Invalid index");
        }
        int parent = v;
        while(item[parent]>0){
            parent = item[parent];
        }
        return parent;
    }
    public int root(int v){
        return find(v);
    }
    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if(root(v1) == root(v2)){
            return;
        } else if(sizeOf(v1) <= sizeOf(v2)){
            item[root(v1)] = root(v2);
            item[root(v2)]--;
        } else {
            item[root(v2)] = root(v1);
            item[root(v2)]--;
        }
        return;
    }

}
