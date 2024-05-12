package helper_classes_and_methods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Author: Xinrui Zhang u7728429:implemented part of b-tree search function
 *
 */
public class BTree {

    /**
     * 普通节点最大关键字数量
     */
    private final int NODE_MAX_KEY_SIZE;

    /**
     * 普通节点最小关键字数量
     */
    private final int NODE_MIN_KEY_SIZE;

    /**
     * 根节点
     */
    private BTreeNode root;

    private final Comparator<String> comparator;

    public BTree(int degree, Comparator<String> comparator) {
        int ROOT_MAX_KEY_SIZE = degree - 1;
        int ROOT_MIN_KEY_SIZE = 1;
        NODE_MAX_KEY_SIZE = degree - 1;
        NODE_MIN_KEY_SIZE = ((int) Math.ceil(degree / 2.0)) - 1;
        root = new BTreeNode(comparator);

        this.comparator = comparator;

        System.out.println("degree: " + degree);
        System.out.println("ROOT_MAX_KEY_SIZE: " + ROOT_MAX_KEY_SIZE);
        System.out.println("ROOT_MIN_KEY_SIZE: " + ROOT_MIN_KEY_SIZE);
        System.out.println("NODE_MAX_KEY_SIZE: " + NODE_MAX_KEY_SIZE);
        System.out.println("NODE_MIN_KEY_SIZE: " + NODE_MIN_KEY_SIZE);
        System.out.println("\n");
    }

    /**
     * 在指定节点插入新的键值对。
     * @param node 目标节点
     * @param key 插入的键
     * @param value 插入的值
     * @param index 插入的位置；如果是 -1，需要在方法内确定插入位置
     * @return 插入后的位置索引
     */

    private int insertElement(BTreeNode node, String key, Dwelling value, int index) {
        if (index == -1) {
            index = 0;
            int size = node.elements.size();

            while (index < size) {
                Element element = node.elements.get(index);
                int comparatorResult = comparator.compare(key, element.getKey());
                if (comparatorResult >= 0) {
                    index++;
                } else {
                    break;
                }
            }
        }

        node.elements.add(index, new Element(key, value));
        return index;
    }

    /**
     * 插入或更新节点中的键值对。
     * @param key 要插入或更新的键
     * @param value 要插入或更新的值
     * @return 如果键存在并被更新，返回原先的值；否则返回 null
     */

    public Dwelling put(String key, Dwelling value) {
        SearchResult searchResult = search(key, root);
        BTreeNode node = searchResult.getNode();
        if (searchResult.isFound()) {
            Element element = node.elements.get(searchResult.getIndex());
            Dwelling originValue = element.getValue();
            element.setValue(value);
            return originValue;
        }

        insertElement(node, key, value, searchResult.getIndex());
        while (node.elements.size() > NODE_MAX_KEY_SIZE) {
            BTreeNode left = new BTreeNode(comparator);
            for (int i = 0; i < NODE_MIN_KEY_SIZE; i++) {
                left.elements.add(node.elements.get(i));
            }
            if (!node.children.isEmpty()) {
                for (int i = 0; i < NODE_MIN_KEY_SIZE + 1; i++) {
                    left.children.add(node.children.get(i));
                }
            }

            Element mid = node.elements.get(NODE_MIN_KEY_SIZE);

            BTreeNode right = new BTreeNode(comparator);
            for (int i = NODE_MIN_KEY_SIZE + 1; i < node.elements.size(); i++) {
                right.elements.add(node.elements.get(i));
            }
            if (!node.children.isEmpty()) {
                for (int i = NODE_MIN_KEY_SIZE + 1; i < node.children.size(); i++) {
                    right.children.add(node.children.get(i));
                }
            }

            BTreeNode parent = node.parent;
            if (null == parent) {
                parent = new BTreeNode(comparator);
                parent.elements.add(mid);
                parent.children.add(left);
                parent.children.add(right);
                left.parent = parent;
                right.parent = parent;
                root = parent;
                break;
            }

            int i = insertElement(parent, mid.getKey(), mid.getValue(), -1);

            // 移除child并插入新的children
            parent.children.set(i, left);
            parent.children.add(i + 1, right);
            left.parent = parent;
            right.parent = parent;
            node.parent = null;
            // do loop
            node = parent;
        }

        return null;
    }

    public Dwelling get(String key) {
        SearchResult searchResult = search(key, root);
        if (searchResult.isFound()) {
            Element element = searchResult.getNode().elements.get(searchResult.getIndex());
            return element.getValue();
        }
        return null;
    }

    /**
     * 根据键删除对应的元素，并返回其值。
     * @param key 要删除的键
     * @return 如果找到并成功删除，返回被删除的值；如果键不存在，返回 null
     */

    public Dwelling remove(String key) {
        SearchResult searchResult = search(key, root);
        if (!searchResult.isFound()) {
            return null;
        }

        BTreeNode node = searchResult.getNode();
        Dwelling value = node.elements.get(searchResult.getIndex()).getValue();
        if (!node.isLeaf()) {
            SearchResult replaceSearchResult = searchPrev(node, searchResult.getIndex());
            BTreeNode replaceNode = replaceSearchResult.getNode();
            if (replaceNode.elements.size() == NODE_MIN_KEY_SIZE) {
                replaceSearchResult = searchNext(node, searchResult.getIndex());
                replaceNode = replaceSearchResult.getNode();

            }
            node.elements.set(searchResult.getIndex(), replaceNode.elements.remove(replaceSearchResult.getIndex()));
            adjust(replaceNode);
        } else {
            node.elements.remove(searchResult.getIndex());
            adjust(node);
        }

        return value;
    }

    private void adjust(BTreeNode node) {
        if (root == node || node.elements.size() >= NODE_MIN_KEY_SIZE) {
            return;
        }

        BTreeNode parent = node.parent;
        int index = parent.children.indexOf(node);

        BTreeNode leftSibling = index > 0 ? parent.children.get(index - 1) : null;
        if (null != leftSibling && leftSibling.elements.size() > NODE_MIN_KEY_SIZE) {
            // 右旋
            Element parentElement = parent.elements.get(index - 1);
            parent.elements.set(index - 1, leftSibling.elements.remove(leftSibling.elements.size() - 1));
            node.elements.add(0, parentElement);
            if (!node.isLeaf()) {
                node.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
            }
            return;
        }

        BTreeNode rightSibling = index < parent.children.size() - 1 ? parent.children.get(index + 1) : null;
        if (null != rightSibling && rightSibling.elements.size() > NODE_MIN_KEY_SIZE) {
            // 左旋
            Element parentElement = parent.elements.get(index);
            parent.elements.set(index, rightSibling.elements.remove(0));
            node.elements.add(parentElement);
            if (!node.isLeaf()) {
                node.children.add(rightSibling.children.remove(0));
            }
            return;
        }

        if (null != leftSibling) {
            // 向左合并
            Element parentElement = parent.elements.remove(index - 1);
            parent.children.remove(index);
            leftSibling.elements.add(parentElement);
            leftSibling.elements.addAll(node.elements);
            if (!node.isLeaf()) {
                leftSibling.children.addAll(node.children);
            }

            if (root == parent && parent.elements.isEmpty()) {
                root = leftSibling;
            } else {
                adjust(parent);
            }
        } else if (null != rightSibling) {
            // 向右合并
            Element parentElement = parent.elements.remove(index);
            parent.children.remove(index);
            rightSibling.elements.add(0, parentElement);
            rightSibling.elements.addAll(0, node.elements);
            if (!node.isLeaf()) {
                rightSibling.children.addAll(0, node.children);
            }

            if (root == parent && parent.elements.isEmpty()) {
                root = rightSibling;
            } else {
                adjust(parent);
            }
        }
    }

    private SearchResult search(String key, BTreeNode node) {
        int i = 0;
        while (i < node.elements.size()) {
            Element element = node.elements.get(i);
            int comparatorResult = comparator.compare(key, element.getKey());
            if (comparatorResult == 0) {
                return new SearchResult(true, node, i);
            } else if (comparatorResult > 0) {
                i++;
            } else {
                break;
            }
        }

        if (node.isLeaf()) {
            return new SearchResult(false, node, i);
        } else {
            return search(key, node.children.get(i));
        }
    }

    /**
     * 获取指定索引位置的前驱元素的搜索结果。
     * @param node 当前节点
     * @param index 当前元素的索引
     * @return 返回前驱元素的搜索结果
     */

    private SearchResult searchPrev(BTreeNode node, int index) {
        BTreeNode child = node.children.get(index);
        while (!child.isLeaf()) {
            child = child.children.get(child.children.size() - 1);
        }

        int prevIndex = child.elements.size() - 1;
        return new SearchResult(true, child, prevIndex);
    }

    /**
     * 获取指定索引位置的后继元素的搜索结果。
     * @param node 当前节点
     * @param index 当前元素的索引
     * @return 返回后继元素的搜索结果
     */

    private SearchResult searchNext(BTreeNode node, int index) {
        BTreeNode child = node.children.get(index + 1);
        while (!child.isLeaf()) {
            child = child.children.get(0);
        }

        return new SearchResult(true, child, 0);
    }


    public List<Dwelling> searchPrefix(String prefix) {
        List<Dwelling> result = new ArrayList<>();
        searchPrefixHelper(prefix, root, result);
        return result;
    }

    private void searchPrefixHelper(String prefix, BTreeNode node, List<Dwelling> result) {
        if (node == null) {
            return;
        }

        int i = 0;
        while (i < node.elements.size()) {
            Element element = node.elements.get(i);
            String key = element.getKey();
            if (key.startsWith(prefix)) {
                result.add(element.getValue());
                searchPrefixHelper(prefix, node.children.get(i), result);
                i++;
            } else if (key.compareTo(prefix) > 0) {
                searchPrefixHelper(prefix, node.children.get(i), result);
                break;
            } else {
                i++;
            }
        }

        if (!node.isLeaf()) {
            searchPrefixHelper(prefix, node.children.get(i), result);
        }
    }




    public List<Dwelling> getDwellings() {
        List<Dwelling> dwellingsList = new ArrayList<>();
        inorderTraversalDwellings(root, dwellingsList);
        return dwellingsList;
    }


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