import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LEE
 * @Create: 10:04 pm on 18/04/2024
 */
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