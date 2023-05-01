/**
 * Handles a node of the BST
 */
public class Node {
    public Node(){}
    public Node(int data, Node right, Node left)
    {
        this.data = data;
        this.right = right;
        this.left = left;
        this.height = 1;
    }

    /**
     * returns right node
     * @return right node
     */
    public Node getRight(){
        return right;
    }

    /**
     * returns left node
     * @return left node
     */
    public Node getLeft(){
        return left;
    }

    /**
     * returns data of node
     * @return data
     */
    public int getData(){
        return data;
    }

    /**
     * returns height of node
     * @return height
     */
    public int getHeight(){
        return height;
    }

    /**
     * sets the right node
     * @param right right node
     */
    public void setRight(Node right){
        this.right = right;
    }

    /**
     * sets the left node
     * @param left left node
     */
    public void setLeft(Node left){
        this.left = left;
    }

    /**
     * sets node data
     * @param data data
     */
    public void setData(int data){
        this.data = data;
    }

    /**
     * sets node height
     * @param height height
     */
    public void setHeight(int height){
        this.height = height;
    }

    /**
     * Returns the balance factor of the node
     * @return balance factor
     */
    public int getBalanceFactor(){
        if (this.right == null && this.left == null)
        {
            return 0;
        }

        if (this.right == null)
        {
            return this.left.height;
        }

        if (this.left ==  null)
        {
            return this.right.height;
        }

        return (this.getRight().getHeight() - this.getLeft().getHeight());
    }

    public void updateHeight(){
        // update height
        if (this.left == null && this.right == null)
        {
            this.height = 1;
        }
        else if (this.left == null || this.right == null)
        {
            if (this.left == null)
            {
                this.height = this.right.height+1;
            }
            else
            {
                this.height = this.left.height+1;
            }
        }
        else
        {
            this.height = Math.max(this.left.height, this.right.height)+1;
        }
    }
    private Node right;
    private Node left;
    private int data;
    private int height;
    private int balanceFactor;
}
