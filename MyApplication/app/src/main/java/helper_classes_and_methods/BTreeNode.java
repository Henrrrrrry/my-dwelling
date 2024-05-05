package helper_classes_and_methods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
     * 返回是否为叶子节点。
     * @return true 如果这个节点没有子节点，否则返回 false。
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