/**
 * This class handles the creation/maintenance of binary search tree
 */
public class BST {

    /**
     * Constructor
     */
    public BST() {
        outputStream = "";
        size = 0;
    }

    /**
     * Prompts a search in the tree
     * @param val value to search for
     * @return output string
     */
    public boolean search(int val)
    {
        if (root == null)
        {
            return false;
        }
        return searchHelper(root, val);
    }
    /**
     * Searches the tree for a given value
     * @param node root node
     * @param val value to search for
     * @return output string
     */
    private boolean searchHelper(Node node, int val)
    {
        // if we have found the node or reached the bottom of a tree
        if (node == null)
        {
            return false;
        }

        if (node.getData() == val)
        {
            return true;
        }

        // if value is less than root, search left tree
        if (val < node.getData())
        {
            return searchHelper(node.getLeft(), val);
        }
        // else search right tree
        else
        {
            return searchHelper(node.getRight(), val);
        }
    }

    /**
     * Inserts node with given value to BST
     * @param val value
     * @return reference to node
     */
    public Node insert(int val)
    {
        if (root == null)
        {
            root = new Node(val, null, null);
            size += 1;
            return root;
        }
        else
        {
            return insertHelper(root, val);
        }
    }

    /**
     * Inserts a node
     * @param node the location to check insertion
     * @param val value to insert
     * @return node that has been inserted/exists
     */
    private Node insertHelper(Node node, int val)
    {
        // if we have reached the bottom of the tree, return a new node
        if (node == null)
        {
            return (new Node(val, null, null));
        }

        // if it is less than the node we are checking, insert to left
        if (val < node.getData())
        {
            node.setLeft(insertHelper(node.getLeft(), val));
        }

        // if it is greater than the node we are checking, insert to right
        else if (val > node.getData())
        {
            node.setRight(insertHelper(node.getRight(), val));
        }


        // check if unbalanced
        if (node.getBalanceFactor() < -1 || node.getBalanceFactor() > 1)
        {
            node = balanceTree(node);
        }

        return node;
    }

    public Node remove(int val)
    {
        root = removeHelper(root, val);
        return root;
    }

    /**
     * Removes a node with the given value from the tree
     * @param node root
     * @param val value
     * @return node that was removed
     */
    private Node removeHelper(Node node, int val)
    {
        // base case
        if (node == null)
        {
            System.out.println("Could not find node to remove");
            return null;
        }

        // if less than current node
        if (val < node.getData())
        {
            node.setLeft(removeHelper(node.getLeft(), val));
        }
        // if greater than current node
        else if (val > node.getData())
        {
            node.setRight(removeHelper(node.getRight(), val));
        }
        // if we have found the node
        else
        {
            // if there are no children, just delete
            if (node.getLeft() == null && node.getRight() == null)
            {
                System.out.println("Removing node with no children");
                size -= 1;
                return null;
            }

            // if it has just left child
            else if (node.getRight() == null && node.getLeft() != null)
            {
                System.out.println("Removing node with left child");
                size -= 1;
                return node.getLeft();
            }

            // if it has just right child
            else if (node.getRight() != null && node.getLeft() == null)
            {
                System.out.println("Removing node with right child");
                size -= 1;
                return node.getRight();
            }

            // if it has both children, replace with inorder successor (smallest in right sub tree)
            else
            {
                System.out.println("Removing node with both children");
                // update value
                node.setData(getSmallestValue(node.getRight()));

                // remove duplicate
                removeHelper(node.getRight(), node.getData());
            }
        }

        // check if unbalanced
        if (node.getBalanceFactor() < -1 || node.getBalanceFactor() > 1)
        {
            node = balanceTree(node);
        }

        return node;
    }

    public String inOrderTraversal()
    {
        outputStream = "";
        inOrderTraversalHelper(root);
        return outputStream;
    }

    /**
     * Prints the tree contents in-order
     * @param node root node
     */
    private void inOrderTraversalHelper(Node node)
    {
        // if we get to the bottom of a subtree, return
        if (node == null)
        {
            return;
        }

        // traverse the left (lesser values)
        inOrderTraversalHelper(node.getLeft());

        // print node contents
        outputStream += (node.getData() + "->");

        // traverse the right (greater values)
        inOrderTraversalHelper(node.getRight());
    }

    public String preOrderTraversal()
    {
        outputStream = "";
        preOrderTraversalHelper(root);
        return outputStream;
    }

    /**
     * Prints the tree contents pre-order
     * @param node root node
     */
    private void preOrderTraversalHelper(Node node)
    {
        // if we get to the bottom of a subtree, return
        if (node == null)
        {
            return;
        }

        // print node contents
        outputStream += (node.getData() + "->");

        // traverse the left
        preOrderTraversalHelper(node.getLeft());

        // traverse the right
        preOrderTraversalHelper(node.getRight());
    }

    public String postOrderTraversal()
    {
        outputStream = "";
        postOrderTraversalHelper(root);
        return outputStream;
    }

    /**
     * Prints the tree in post-order
     * @param node root node
     */
    private void postOrderTraversalHelper(Node node)
    {
        if (node == null)
        {
            return;
        }

        // traverse the left
        postOrderTraversalHelper(node.getLeft());

        // traverse the right
        postOrderTraversalHelper(node.getRight());

        // print node contents
        outputStream += (node.getData() + "->");
    }

    /**
     * Returns the smallest value of a subtree given root
     * @param node root node
     * @return value of smallest node
     */
    private int getSmallestValue(Node node)
    {
        int smallest = node.getData();

        // iterate through left subtree
        while (node.getLeft() != null) {
            smallest = node.getLeft().getData();
            node = node.getLeft();
        }

        // this will return the bottom of the left subtree
        return smallest;
    }

    /**
     * Balances the bst (AVL Tree method)
     * @param node root
     * @return node
     */
    public Node balanceTree(Node node)
    {
        int bf = node.getBalanceFactor();

        if (bf < -1 && node.getLeft().getBalanceFactor() == -1)
        {
            return rotateRight(node);
        }

        if (bf < -1 && node.getRight().getBalanceFactor() == 1)
        {
            return rotateLeftThenRight(node);
        }

        if (bf > 1 && node.getRight().getBalanceFactor() == 1)
        {
            return rotateLeft(node);
        }

        if (bf > 1 && node.getLeft().getBalanceFactor() == -1)
        {
            return rotateRightThenLeft(node);
        }

        return node;
    }

    /**
     * Rotates a node left
     * @param node root
     * @return node
     */
    private Node rotateLeft(Node node)
    {
        // store the right node in a new node
        Node rotatedNode = node.getRight();

        // set the right tree of node to the left of the new node
        node.setRight(rotatedNode.getLeft());

        // set the left of the new node to the original node
        rotatedNode.setLeft(node);

        return rotatedNode;
    }

    /**
     * Rotates a node right
     * @param node root
     * @return node
     */
    private Node rotateRight(Node node)
    {
        Node rotatedNode = node.getLeft();
        node.setLeft(rotatedNode.getRight());
        rotatedNode.setRight(node);
        return rotatedNode;
    }

    /**
     * Rotates a node right then left
     * @param node root
     * @return node
     */
    private Node rotateRightThenLeft(Node node)
    {
        node.setRight(rotateRight(node.getRight()));
        node = rotateLeft(node);
        return node;
    }

    /**
     * Rotates node left then right
     * @param node root
     * @return node
     */
    private Node rotateLeftThenRight(Node node)
    {
        node.setLeft(rotateLeft(node.getLeft()));
        node = rotateRight(node);
        return node;
    }

    Node root;
    String outputStream;
    int size;
}
