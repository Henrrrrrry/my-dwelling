import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class BTreeNode<K, V> {
    private List<Element<K, V>> elements;
    private List<BTreeNode<K, V>> children;
    private BTreeNode<K, V> parent;
    private Comparator<K> comparator;

    BTreeNode(Comparator<K> comparator) {
        this.comparator = comparator;
        elements = new ArrayList<>();
        children = new ArrayList<>();
        this.parent = null;
    }

    public List<Element<K, V>> getElements() {
        return elements;
    }

    public void setElements(List<Element<K, V>> elements) {
        this.elements = elements;
    }

    public List<BTreeNode<K, V>> getChildren() {
        return children;
    }

    public void setChildren(List<BTreeNode<K, V>> children) {
        this.children = children;
    }

    public BTreeNode<K, V> getParent() {
        return parent;
    }

    public void setParent(BTreeNode<K, V> parent) {
        this.parent = parent;
    }

    public Comparator<K> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    /**
     * 返回是否为叶子节点
     * @return
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