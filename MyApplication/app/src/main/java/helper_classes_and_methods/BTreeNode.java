package helper_classes_and_methods;

import java.util.ArrayList;
import java.util.List;


public class BTreeNode {
    List<Dwelling> keys=new ArrayList<>();
    //    minimum degree
    int t;
    List<BTreeNode> children = new ArrayList<>();

    boolean isLeaf=true;
    public BTreeNode(int t) {
        this.t = t;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}