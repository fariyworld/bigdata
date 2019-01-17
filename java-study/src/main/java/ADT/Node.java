package ADT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description: 二叉树 节点类
 * <br />
 * Created by mace on 2019/1/8 15:08.
 */
public class Node<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

    // 节点数据
    private T data;
    // 左子节点的引用
    private Node leftChildNode;
    // 右子节点的引用
    private Node rightChildNode;

    public Node(T data) {
        this.data = data;
    }

    //打印节点内容
    public void display(){
        LOGGER.info("当前节点的数据为, {}", data);
    }

    public Node getLeftChildNode() {
        return leftChildNode;
    }

    public void setLeftChildNode(Node leftChildNode) {
        this.leftChildNode = leftChildNode;
    }

    public Node getRightChildNode() {
        return rightChildNode;
    }

    public void setRightChildNode(Node rightChildNode) {
        this.rightChildNode = rightChildNode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
