package helper_classes_and_methods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Author: Xinrui Zhang u7728429:implemented part of b-tree search function
 *
 */
public class BTree {


    private final int NODE_MAX_KEY_SIZE;
    private final int NODE_MIN_KEY_SIZE;
    private BTreeNode root;
    private final Comparator<String> comparator;

    /**
     * Author: Juliang Xiao u7757949: Constructs a B-tree with the given degree and comparator.
     */
    public BTree(int degree, Comparator<String> comparator) {
        this.NODE_MAX_KEY_SIZE = degree - 1;
        this.NODE_MIN_KEY_SIZE = (int) Math.ceil(degree / 2.0) - 1;
        this.root = new BTreeNode(comparator);
        this.comparator = comparator;
    }

    /**
     * Author: Juliang Xiao u7757949: Inserts a new key-value pair into the specified node.
     */
    private int insertElement(BTreeNode node, String key, Dwelling value, int index) {
        if (index == -1) {
            index = findInsertionIndex(node, key);
        }

        node.elements.add(index, new Element(key, value));
        return index;
    }

    /**
     * Author: Juliang Xiao u7757949: Finds the index at which a key should be inserted in a node's elements list.
     */
    private int findInsertionIndex(BTreeNode node, String key) {
        int size = node.elements.size();
        int index = 0;

        while (index < size && comparator.compare(key, node.elements.get(index).getKey()) >= 0) {
            index++;
        }

        return index;
    }

    /**
     * Author: Juliang Xiao u7757949: Inserts a key-value pair into the B-tree.
     */
    public Dwelling put(String key, Dwelling value) {
        SearchResult searchResult = search(key, root);
        BTreeNode node = searchResult.getNode();

        if (searchResult.isFound()) {
            return updateElement(node, searchResult.getIndex(), value);
        }

        insertElement(node, key, value, searchResult.getIndex());
        splitNodeIfNeeded(node);
        return null;
    }

    /**
     * Author: Juliang Xiao u7757949: Updates the value of an existing element in a node.
     */
    private Dwelling updateElement(BTreeNode node, int index, Dwelling value) {
        Element element = node.elements.get(index);
        Dwelling originValue = element.getValue();
        element.setValue(value);
        return originValue;
    }

    /**
     * Author: Juliang Xiao u7757949: Splits a node if it exceeds the maximum number of keys allowed.
     */
    private void splitNodeIfNeeded(BTreeNode node) {
        while (node.elements.size() > NODE_MAX_KEY_SIZE) {
            BTreeNode left = createLeftNode(node);
            Element mid = node.elements.get(NODE_MIN_KEY_SIZE);
            BTreeNode right = createRightNode(node);

            BTreeNode parent = node.parent;
            if (parent == null) {
                parent = createNewParent(left, mid, right);
                root = parent;
                break;
            }

            int i = insertElement(parent, mid.getKey(), mid.getValue(), -1);
            updateParentChildren(parent, i, left, right);
            node.parent = null;
            node = parent;
        }
    }

    /**
     * Author: Juliang Xiao u7757949: Creates a new left node during a node split.
     */
    private BTreeNode createLeftNode(BTreeNode node) {
        BTreeNode left = new BTreeNode(comparator);
        left.elements.addAll(node.elements.subList(0, NODE_MIN_KEY_SIZE));
        if (!node.children.isEmpty()) {
            left.children.addAll(node.children.subList(0, NODE_MIN_KEY_SIZE + 1));
        }
        return left;
    }

    /**
     * Author: Juliang Xiao u7757949: Creates a new right node during a node split.
     */
    private BTreeNode createRightNode(BTreeNode node) {
        BTreeNode right = new BTreeNode(comparator);
        right.elements.addAll(node.elements.subList(NODE_MIN_KEY_SIZE + 1, node.elements.size()));
        if (!node.children.isEmpty()) {
            right.children.addAll(node.children.subList(NODE_MIN_KEY_SIZE + 1, node.children.size()));
        }
        return right;
    }

    /**
     * Author: Juliang Xiao u7757949: Creates a new parent node during a node split.
     */
    private BTreeNode createNewParent(BTreeNode left, Element mid, BTreeNode right) {
        BTreeNode parent = new BTreeNode(comparator);
        parent.elements.add(mid);
        parent.children.add(left);
        parent.children.add(right);
        left.parent = parent;
        right.parent = parent;
        return parent;
    }

    /**
     * Author: Juliang Xiao u7757949: Updates the children of a parent node during a node split.
     */
    private void updateParentChildren(BTreeNode parent, int i, BTreeNode left, BTreeNode right) {
        parent.children.set(i, left);
        parent.children.add(i + 1, right);
        left.parent = parent;
        right.parent = parent;
    }

    /**
     * Author: Juliang Xiao u7757949: Retrieves the value associated with the given key from the B-tree.
     */
    public Dwelling get(String key) {
        SearchResult searchResult = search(key, root);
        return searchResult.isFound() ? searchResult.getNode().elements.get(searchResult.getIndex()).getValue() : null;
    }

    /**
     * Author: Juliang Xiao u7757949: Removes a key-value pair from the B-tree.
     */
    public Dwelling remove(String key) {
        SearchResult searchResult = search(key, root);
        if (!searchResult.isFound()) {
            return null;
        }

        BTreeNode node = searchResult.getNode();
        Dwelling value = node.elements.get(searchResult.getIndex()).getValue();

        if (node.isLeaf()) {
            node.elements.remove(searchResult.getIndex());
        } else {
            SearchResult replaceSearchResult = findReplacementElement(node, searchResult.getIndex());
            BTreeNode replaceNode = replaceSearchResult.getNode();
            node.elements.set(searchResult.getIndex(), replaceNode.elements.remove(replaceSearchResult.getIndex()));
        }

        adjustAfterRemoval(node);
        return value;
    }

    /**
     * Author: Juliang Xiao u7757949: Finds the replacement element during a key removal.
     */
    private SearchResult findReplacementElement(BTreeNode node, int index) {
        SearchResult searchResult = searchPrev(node, index);
        if (searchResult.getNode().elements.size() > NODE_MIN_KEY_SIZE) {
            return searchResult;
        }
        return searchNext(node, index);
    }

    /**
     * Author: Juliang Xiao u7757949: Adjusts the B-tree after a key removal to maintain balance.
     */
    private void adjustAfterRemoval(BTreeNode node) {
        if (root == node || node.elements.size() >= NODE_MIN_KEY_SIZE) {
            return;
        }

        BTreeNode parent = node.parent;
        int index = parent.children.indexOf(node);

        if (rotateLeftIfPossible(node, parent, index) || rotateRightIfPossible(node, parent, index)) {
            return;
        }

        mergeWithSibling(node, parent, index);
    }

    /**
     * Author: Juliang Xiao u7757949: Performs a left rotation if possible during key removal.
     */
    private boolean rotateLeftIfPossible(BTreeNode node, BTreeNode parent, int index) {
        BTreeNode leftSibling = index > 0 ? parent.children.get(index - 1) : null;
        if (leftSibling != null && leftSibling.elements.size() > NODE_MIN_KEY_SIZE) {
            Element parentElement = parent.elements.get(index - 1);
            parent.elements.set(index - 1, leftSibling.elements.remove(leftSibling.elements.size() - 1));
            node.elements.add(0, parentElement);
            if (!node.isLeaf()) {
                node.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
            }
            return true;
        }
        return false;
    }

    /**
     * Author: Juliang Xiao u7757949: Performs a right rotation if possible during key removal.
     */
    private boolean rotateRightIfPossible(BTreeNode node, BTreeNode parent, int index) {
        BTreeNode rightSibling = index < parent.children.size() - 1 ? parent.children.get(index + 1) : null;
        if (rightSibling != null && rightSibling.elements.size() > NODE_MIN_KEY_SIZE) {
            Element parentElement = parent.elements.get(index);
            parent.elements.set(index, rightSibling.elements.remove(0));
            node.elements.add(parentElement);
            if (!node.isLeaf()) {
                node.children.add(rightSibling.children.remove(0));
            }
            return true;
        }
        return false;
    }

    /**
     * Author: Juliang Xiao u7757949: Merges a node with its sibling during key removal.
     */
    private void mergeWithSibling(BTreeNode node, BTreeNode parent, int index) {
        BTreeNode leftSibling = index > 0 ? parent.children.get(index - 1) : null;
        BTreeNode rightSibling = index < parent.children.size() - 1 ? parent.children.get(index + 1) : null;

        if (leftSibling != null) {
            mergeWithLeftSibling(node, parent, index, leftSibling);
        } else if (rightSibling != null) {
            mergeWithRightSibling(node, parent, index, rightSibling);
        }
    }

    /**
     * Author: Juliang Xiao u7757949: Merges a node with its left sibling during key removal.
     */
    private void mergeWithLeftSibling(BTreeNode node, BTreeNode parent, int index, BTreeNode leftSibling) {
        Element parentElement = parent.elements.remove(index - 1);
        parent.children.remove(index);
        leftSibling.elements.add(parentElement);
        leftSibling.elements.addAll(node.elements);
        if (!node.isLeaf()) {
            leftSibling.children.addAll(node.children);
        }

        if (parent == root && parent.elements.isEmpty()) {
            root = leftSibling;
        } else {
            adjustAfterRemoval(parent);
        }
    }

    /**
     * Author: Juliang Xiao u7757949: Merges a node with its right sibling during key removal.
     */
    private void mergeWithRightSibling(BTreeNode node, BTreeNode parent, int index, BTreeNode rightSibling) {
        Element parentElement = parent.elements.remove(index);
        parent.children.remove(index);
        rightSibling.elements.add(0, parentElement);
        rightSibling.elements.addAll(0, node.elements);
        if (!node.isLeaf()) {
            rightSibling.children.addAll(0, node.children);
        }

        if (parent == root && parent.elements.isEmpty()) {
            root = rightSibling;
        } else {
            adjustAfterRemoval(parent);
        }
    }

    /**
     * Author: Juliang Xiao u7757949: Searches for a key in the B-tree.
     */
    private SearchResult search(String key, BTreeNode node) {
        int i = binarySearch(key, node.elements);

        if (i >= 0) {
            return new SearchResult(true, node, i);
        }

        i = -(i + 1);

        if (node.isLeaf()) {
            return new SearchResult(false, node, i);
        } else {
            return search(key, node.children.get(i));
        }
    }

    /**
     * Author: Juliang Xiao u7757949: Performs a binary search on a list of elements.
     */
    private int binarySearch(String key, List<Element> elements) {
        int low = 0;
        int high = elements.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Element midElement = elements.get(mid);
            int comparatorResult = comparator.compare(key, midElement.getKey());

            if (comparatorResult < 0) {
                high = mid - 1;
            } else if (comparatorResult > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }

        return -(low + 1);
    }

    /**
     * Author: Juliang Xiao u7757949: Searches for the previous element of a given index in a node.
     */
    private SearchResult searchPrev(BTreeNode node, int index) {
        BTreeNode child = node.children.get(index);
        int prevIndex = child.elements.size() - 1;

        while (!child.isLeaf()) {
            child = child.children.get(prevIndex + 1);
            prevIndex = child.elements.size() - 1;
        }

        return new SearchResult(true, child, prevIndex);
    }

    /**
     * Author: Juliang Xiao u7757949: Searches for the next element of a given index in a node.
     */
    private SearchResult searchNext(BTreeNode node, int index) {
        BTreeNode child = node.children.get(index + 1);

        while (!child.isLeaf()) {
            child = child.children.get(0);
        }

        return new SearchResult(true, child, 0);
    }







    /**
     * Author: Xinrui Zhang u7728429
     * Description: get all the dwellings from tree
     */

    public List<Dwelling> getDwellings() {
        List<Dwelling> dwellingsList = new ArrayList<>();
        inorderTraversalDwellings(root, dwellingsList);
        return dwellingsList;
    }

    /**
     * Author: Xinrui Zhang u7728429
     * Description: inorder traversal dwellings
     */
    private void inorderTraversalDwellings(BTreeNode node, List<Dwelling> dwellingsList) {
        if (node == null) {
            return;
        }

        int numKeys = node.elements.size();
        for (int i = 0; i < numKeys; i++) {
            if (!node.isLeaf()) {
                inorderTraversalDwellings(node.children.get(i), dwellingsList);
            }
            dwellingsList.add(node.elements.get(i).getValue());
        }
        if (!node.isLeaf()) {
            inorderTraversalDwellings(node.children.get(numKeys), dwellingsList);
        }
    }
}