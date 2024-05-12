package helper_classes_and_methods;


/**
 * Author: Juliang Xiao u7757949
 *
 * The SearchResult class represents the result of a search operation in a B-tree,
 * encapsulating information about the found status, node, and index of the search key.
 */


class SearchResult {

    private boolean found;
    private BTreeNode node;
    private int index;

    public SearchResult(boolean found, BTreeNode node, int index) {
        this.found = found;
        this.node = node;
        this.index = index;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public BTreeNode getNode() {
        return node;
    }

    public void setNode(BTreeNode node) {
        this.node = node;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "found=" + found +
                ", node=" + node +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        if (found != that.found) return false;
        if (index != that.index) return false;
        return node != null ? node.equals(that.node) : that.node == null;
    }

    @Override
    public int hashCode() {
        int result = (found ? 1 : 0);
        result = 31 * result + (node != null ? node.hashCode() : 0);
        result = 31 * result + index;
        return result;
    }
}