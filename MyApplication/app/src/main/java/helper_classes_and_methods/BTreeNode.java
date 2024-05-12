package helper_classes_and_methods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Author:Juliang Xiao u7757959:Constructs a new BTreeNode with the specified comparator.
 */

class BTreeNode {
    public List<Element> elements;
    public List<BTreeNode> children;
    public BTreeNode parent;
    private Comparator<String> comparator;

    BTreeNode(Comparator<String> comparator) {
        this.comparator = comparator;
        elements = new ArrayList<>();
        children = new ArrayList<>();
        this.parent = null;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public List<BTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<BTreeNode> children) {
        this.children = children;
    }

    public BTreeNode getParent() {
        return parent;
    }

    public void setParent(BTreeNode parent) {
        this.parent = parent;
    }

    public Comparator<String> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<String> comparator) {
        this.comparator = comparator;
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