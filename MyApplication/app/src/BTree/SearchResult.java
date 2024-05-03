class SearchResult<K, V> {
    /**
     * 是否找到对应关键字
     */
    private boolean found;

    /**
     * 对应的节点
     */
    private BTreeNode<K, V> node;

    /**
     * 关键字在node的index, -1代表不在该节点中
     */
    private int index;

    public SearchResult(boolean found, BTreeNode<K, V> node, int index) {
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

    public BTreeNode<K, V> getNode() {
        return node;
    }

    public void setNode(BTreeNode<K, V> node) {
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

        SearchResult<?, ?> that = (SearchResult<?, ?>) o;

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