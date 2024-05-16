package helper_classes_and_methods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Author:Juliang Xiao u7757959:Constructs a new BTreeNode with the specified comparator.
 */

public class BTreeNode {
    public List<Element> elements;
    public List<BTreeNode> children;
    public BTreeNode parent;
    private Comparator<String> comparator;

    public BTreeNode(Comparator<String> comparator) {
        this.comparator = comparator;
        elements = new ArrayList<>();
        children = new ArrayList<>();
        this.parent = null;
    }




    public BTreeNode getParent() {
        return parent;
    }

    public void setParent(BTreeNode parent) {
        this.parent = parent;
    }



    /**
     * Author :Juliang Xiao u7747949 :Checks if this node is a leaf node.
     * @return true if this node has no children, false otherwise
     */
    boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public String toString() {
        return "BTreeNode{" +
                "elements=" + elements +
                ", children=" + children +
                '}';
    }
}