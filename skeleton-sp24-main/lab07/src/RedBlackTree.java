public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /**
         * Creates a RBTreeNode with item ITEM and color depending on ISBLACK
         * value.
         * @param isBlack
         * @param item
         */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /**
         * Creates a RBTreeNode with item ITEM, color depending on ISBLACK
         * value, left child LEFT, and right child RIGHT.
         * @param isBlack
         * @param item
         * @param left
         * @param right
         */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Creates an empty RedBlackTree.
     */
    public RedBlackTree() {
        root = null;
    }

    /**
     * Flips the color of node and its children. Assume that NODE has both left
     * and right children
     * @param node
     */
    void flipColors(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /**
     * Rotates the given node to the right. Returns the new root node of
     * this subtree. For this implementation, make sure to swap the colors
     * of the new root and the old root!
     * @param node
     * @return
     */
    /**
     * The root of the subtree has changed from b to a.
     * 子树的根已从B更改为A。
     * a and b have moved to the “right”.
     * a和B已移至“右侧”。
     * The two nodes swap colors so that the new root is the same color as the old root.
     * 这两个节点交换颜色，以便新根与旧根的颜色相同。
     * The reorganized subtree still satisfies the binary search property.
     * 重组后的子树仍然满足二叉搜索性质。
     * */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        RBTreeNode n = node;//node为根不变，n保存
        node = n.left;//变根 此时node为原根的左节点即新根；
        n.left = node.right;//第二步
        node.right = n;
        //颜色调整错误，直接调整出错了
//        n.isBlack = !n.isBlack;
//        node.isBlack = !node.isBlack;//变颜色
        node.isBlack = n.isBlack;
        n.isBlack = false;
        return node;
    }

    /**
     * Rotates the given node to the left. Returns the new root node of
     * this subtree. For this implementation, make sure to swap the colors
     * of the new root and the old root!
     * @param node
     * @return
     */
    /**
     * The root of the subtree has changed from a to b.
     * 子树的根已从a更改为B。
     * a and b have moved to the “left”.
     * a和B已经向“左”移动。
     * The two nodes swap colors so that the new root is the same color as the old root.
     * 这两个节点交换颜色，以便新根与旧根的颜色相同。
     * The reorganized subtree still satisfies the binary search property.
     * 重组后的子树仍然满足二叉搜索性质。
     * */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        RBTreeNode n = node;//node为根不变，n保存
        node = n.right;
        n.right = node.left;
        node.left = n;
        //颜色调整错误，直接调整出错了
//        n.isBlack = !n.isBlack;
//        node.isBlack = !node.isBlack;
        node.isBlack = n.isBlack;
        n.isBlack = false;
        return node;
    }

    /**
     * Helper method that returns whether the given node is red. Null nodes (children or leaf
     * nodes) are automatically considered black.
     * @param node
     * @return
     */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    /**
     * Inserts the item into the Red Black Tree. Colors the root of the tree black.
     * @param item
     */
    public void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    /**
     * Inserts the given node into this Red Black Tree. Comments have been provided to help break
     * down the problem. For each case, consider the scenario needed to perform those operations.
     * Make sure to also review the other methods in this class!
     * @param node
     * @param item
     * @return
     */
//    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
//        // TODO: Insert (return) new red leaf node.
//        // TODO: Handle normal binary search tree insertion.
//        // TODO: Rotate left operation
//
//        // TODO: Rotate right operation
//
//        // TODO: Color flip
//
//        return null; //fix this return statement
//    }
    /**要学会递归插入*/
    public RBTreeNode<T> insert(RBTreeNode<T> node,T item){
        if (node == null) {
            // 新建节点时，默认红色
            return new RBTreeNode(false,item);
        }
        if(item.compareTo((T) node.item)<0)//小于 往左边找
        {
//            if(node.left==null){
//                RBTreeNode nee = new RBTreeNode(false,item);
//                node.left = nee;
//                return nee;
//            }else{
//                insert(item,node.left);}
            node.left = insert(node.left,item);//递归插入
        }else if(item.compareTo((T) node.item)>0){
//            if(node.right == null){
//                RBTreeNode nee = new RBTreeNode(false,item);
//                node.right = nee;
//                return nee;
//            }else {
//                insert(item,node.right);
//            }
            node.right = insert(node.right,item);
        }
        //插入完毕
        //修正
        //右边是红的，左边不是红的
        if(isRed(node.right)&&!isRed(node.left)){
            node = rotateLeft(node);//左旋
        }//右边不红，左边红 可以
        //左左红
        if(isRed(node.left) && isRed(node.left.left)){
            node = rotateRight(node);
        }
        //两个都红 翻色
        if(isRed(node.left)&&isRed(node.right)){
            flipColors(node);
        }
        return node;
    }

}
