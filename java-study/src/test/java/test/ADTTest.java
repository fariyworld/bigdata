package test;

import ADT.Node;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:
 * <br />
 * Created by mace on 2019/1/8 15:28.
 */
public class ADTTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ADTTest.class);

    @Test
    public void test01(){

        LOGGER.info("开始创建一个五层二叉树");
        LOGGER.info("1.开始创建节点...");
        Node<Integer> root = new Node<>(49);
        Node<Integer> node1 = new Node<>(29);
        Node<Integer> node2 = new Node<>(79);
        Node<Integer> node3 = new Node<>(25);
        Node<Integer> node4 = new Node<>(33);
        Node<Integer> node5 = new Node<>(76);
        Node<Integer> node6 = new Node<>(95);
        Node<Integer> node7 = new Node<>(9);
        Node<Integer> node8 = new Node<>(31);
        Node<Integer> node9 = new Node<>(40);
        Node<Integer> node10 = new Node<>(64);
        Node<Integer> node11 = new Node<>(77);
        Node<Integer> node12 = new Node<>(90);
        Node<Integer> node13 = new Node<>(7);
        Node<Integer> node14 = new Node<>(11);
        Node<Integer> node15 = new Node<>(42);
        Node<Integer> node16 = new Node<>(50);
        Node<Integer> node17 = new Node<>(73);
        Node<Integer> node18 = new Node<>(89);
        Node<Integer> node19 = new Node<>(94);
        LOGGER.info("2.创建节点依赖关系...生成树");
        LOGGER.info("根节点:{}",root.getData());
        root.setLeftChildNode(node1);
        root.setRightChildNode(node2);

        node1.setLeftChildNode(node3);
        node1.setRightChildNode(node4);
        node2.setLeftChildNode(node5);
        node2.setRightChildNode(node6);

        node3.setLeftChildNode(node7);
        node4.setLeftChildNode(node8);
        node4.setRightChildNode(node9);
        node5.setLeftChildNode(node10);
        node5.setRightChildNode(node11);
        node6.setLeftChildNode(node12);

        node7.setLeftChildNode(node13);
        node7.setRightChildNode(node14);
        node9.setRightChildNode(node15);
        node10.setLeftChildNode(node16);
        node10.setRightChildNode(node17);
        node12.setLeftChildNode(node18);
        node12.setRightChildNode(node19);

        LOGGER.info("3.成功生成树...");
        LOGGER.info("该树包含的所有节点有:");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("二叉树中的节点个数:{}",getNodeNumRec(root));
        LOGGER.info("二叉树的深度:{}",getDepthRec(root));
        LOGGER.info("查找是否存在‘77’节点 -> {}", find(root, 77)==null?"不存在":"存在");
        LOGGER.info("查找是否存在‘0’节点 -> {}", find(root, 0)==null?"不存在":"存在");
        LOGGER.info("插入新节点‘66’ -> {}", insert(root, 66));
        LOGGER.info("插入新节点‘11’ -> {}", insert(root, 11));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("删除‘94’节点 -> {}", delete(root, 94));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("删除‘95’节点 -> {}", delete(root, 95));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("删除‘31’节点 -> {}", delete(root, 31));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("删除‘33’节点 -> {}", delete(root, 33));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
//        LOGGER.info("查找‘79’节点的后继节点 => {}", findSuccessorNode(node2).getData());
//        LOGGER.info("查找‘29’节点的后继节点 => {}", findSuccessorNode(find(root, 29)).getData());
//        LOGGER.info("查找‘76’节点的后继节点 => {}", findSuccessorNode(find(root, 76)).getData());
//        LOGGER.info("查找‘42’节点的后继节点 => {}", findSuccessorNode(find(root, 42)).getData());
//        LOGGER.info("查找‘0’节点的后继节点 => {}", findSuccessorNode(find(root, 0))==null?"null":findSuccessorNode(find(root, 0)).getData());
        LOGGER.info("删除‘79’节点 -> {}", delete(root, 79));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("插入新节点‘94’ -> {}", insert(root, 94));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("插入新节点‘95’ -> {}", insert(root, 95));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
        LOGGER.info("删除‘89’节点 -> {}", delete(root, 89));
        LOGGER.info("重新遍历该树...");
        inorderTraversalRec(root);
        System.out.println();
    }

    /**
     * description: 中序遍历 升序
     * 递归解法：
     *     如果二叉树为空，空操作
     *     如果二叉树不为空，中序遍历左子树，访问根节点，中序遍历右子树
     * <br /><br />
     * create by mace on 2019/1/8 16:00.
     * @param root
     * @return: void
     */
    void inorderTraversalRec(Node root){
        if(null == root){
            return;
        }
        // 中序遍历左子树
        inorderTraversalRec(root.getLeftChildNode());
        // 访问根节点
        System.out.print(root.getData()+", ");
        // 中序遍历右子树
        inorderTraversalRec(root.getRightChildNode());
    }


    /**
     * description: 查找节点
     * 查找某个节点，我们必须从根节点开始遍历。
     * 　　①、查找值比当前节点值大，则搜索右子树；
     * 　　②、查找值等于当前节点值，停止搜索（终止条件）；
     * 　　③、查找值小于当前节点值，则搜索左子树；
     * <br /><br />
     * create by mace on 2019/1/8 16:50.
     * @param root
     * @param key
     * @return: ADT.Node
     */
    Node find(Node<Integer> root, int key){
        Node<Integer> current = root;
        while(null != current){
            if(current.getData() < key){
                current = current.getRightChildNode();
            }else if(current.getData() == key){
                return current;
            }else{
                current = current.getLeftChildNode();
            }
        }
        return null;
    }

    /**
     * description: 插入节点
     * 要插入节点，必须先找到插入的位置。
     * 与查找操作相似，由于二叉搜索树的特殊性，待插入的节点也需要从根节点开始进行比较，
     * 小于根节点则与根节点左子树比较，反之则与右子树比较，直到左子树为空或右子树为空，则插入到相应为空的位置，
     * 在比较的过程中要注意保存父节点的信息 及 待插入的位置是父节点的左子树还是右子树，才能插入到正确的位置。
     * <br /><br />
     * create by mace on 2019/1/8 17:16.
     * @param data
     * @return: boolean
     */
    boolean insert(Node<Integer> root, int data){
        Node<Integer> newNode = new Node(data);
        if(null == root){
            root = newNode;
            return true;
        }else{
            Node<Integer> current = root;
            Node<Integer> parrentNode = null;
            while(null != current){
                parrentNode = current;
                if (current.getData() > data){
                    //左子节点
                    current = current.getLeftChildNode();
                    if(null == current){
                        parrentNode.setLeftChildNode(newNode);
                        return true;
                    }
                }else if (current.getData() < data) {
                    //右子节点
                    current = current.getRightChildNode();
                    if(null == current){
                        parrentNode.setRightChildNode(newNode);
                        return true;
                    }
                }else{
                    LOGGER.info("该树中已存在‘{}’的节点,插入失败...",data);
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * description: 删除节点是二叉搜索树中最复杂的操作，删除的节点有三种情况
     * 　　1、该节点是叶节点（没有子节点）
     * 　　2、该节点有一个子节点
     * 　　3、该节点有两个子节点
     * 步骤:
     *  ①、查找删除值，找不到直接返回false
     * <br /><br />
     * create by mace on 2019/1/8 17:39.
     * @param root
     * @param key
     * @return: boolean
     */
    boolean delete(Node<Integer> root, int key){
        LOGGER.info("Will try to delete node {}", key);
        Node<Integer> current = root;
        Node<Integer> parrentNode = null;
        boolean isLeftChildNode = false;
        while(key != current.getData()){
            parrentNode = current;
            if(current.getData() > key){
                LOGGER.info("Going to left child");
                isLeftChildNode = true;
                current = current.getLeftChildNode();
            }else if(current.getData() < key){
                LOGGER.info("Going to right child");
                isLeftChildNode = false;
                current = current.getRightChildNode();
            }
            if(null == current){
                //查找删除值, 找不到直接返回false
                LOGGER.info("Can't find node with that value");
                return false;
            }
        }
        LOGGER.info("Have found node to delete");
        if(null == current.getLeftChildNode() && null == current.getRightChildNode()){
            //没有子节点
            if(current == root){
                root = null;
            }else if(isLeftChildNode){
                parrentNode.setLeftChildNode(null);
            }else{
                parrentNode.setRightChildNode(null);
            }
            return true;
        }else if(null != current.getLeftChildNode() && null == current.getRightChildNode()){
            //有一个子节点且为左子节点  将其父节点原本指向该节点的引用，改为指向该节点的左子节点即可
            LOGGER.info("Will replace node with its left subtree");
            if(current == root){
                root = current.getLeftChildNode();
            }else if(isLeftChildNode){
                parrentNode.setLeftChildNode(current.getLeftChildNode());
            }else{
                parrentNode.setRightChildNode(current.getLeftChildNode());
            }
            LOGGER.info("Node was replaced by its left subtree");
            return true;
        }else if(null == current.getLeftChildNode() && null != current.getRightChildNode()){
            //有一个子节点且为右子节点  将其父节点原本指向该节点的引用，改为指向该节点的右子节点即可
            LOGGER.info("Will replace node with its right subtree");
            if(current == root){
                root = current.getRightChildNode();
            }else if(isLeftChildNode){
                parrentNode.setLeftChildNode(current.getRightChildNode());
            }else{
                parrentNode.setRightChildNode(current.getRightChildNode());
            }
            LOGGER.info("Node was replaced by its right subtree");
            return true;
        }else{
            //有两个子节点
            LOGGER.info("有两个子节点,用中序后继节点来代替删除的节点...");
            // 1.找到删除节点的中序后继节点 => 比删除节点关键值大的节点集合中最小的一个节点
            Node successorNode = findSuccessorNode(current);
            if(current == root){
                root = successorNode;
            }else if(isLeftChildNode){
                parrentNode.setLeftChildNode(successorNode);
            }else{
                parrentNode.setRightChildNode(successorNode);
            }
            // 后继节点的左节点为 删除节点的左节点
            successorNode.setLeftChildNode(current.getLeftChildNode());
            LOGGER.info("Removed node in 2-step process");
            return true;
        }
    }

    /**
     * description: 求二叉树中的节点个数
     * 递归解法： O(n)
     *     如果二叉树为空，节点个数为0
     *     如果二叉树不为空，二叉树节点个数 = 左子树节点个数 + 右子树节点个数 + 1
     * <br /><br />
     * create by mace on 2019/1/8 16:27.
     * @param root
     * @return: int
     */
    int getNodeNumRec(Node root) {
        if (root == null) {
            return 0;
        }
        return getNodeNumRec(root.getLeftChildNode()) + getNodeNumRec(root.getRightChildNode()) + 1;
    }

    /**
     * description: 求二叉树的深度（高度）
     * 递归解法： O(n)
     *     如果二叉树为空，二叉树的深度为0
     *     如果二叉树不为空，二叉树的深度 = max(左子树深度， 右子树深度) + 1
     * <br /><br />
     * create by mace on 2019/1/8 16:28.
     * @param root
     * @return: int
     */
    int getDepthRec(Node root) {
        if (root == null) {
            return 0;
        }
        return Math.max(getDepthRec(root.getLeftChildNode()), getDepthRec(root.getRightChildNode())) + 1;
    }

    /**
     * description: 找到当前节点的中序后继节点 => 比当前节点关键值大的节点集合中最小的一个节点
     * ①、找到当前节点的右节点
     * ②、转到该右节点的左子节点，依次顺着左子节点找下去，最后一个左子节点即是后继节点；如果该右节点没有左子节点，那么该右节点便是后继节点。
     * <br /><br />
     * create by mace on 2019/1/9 10:39.
     * @param delNode
     * @return: ADT.Node
     */
    Node findSuccessorNode(Node delNode){
        if (null == delNode){
            LOGGER.info("参数为空...");
            return null;
        }
        // 后继节点的父节点
        Node successorNodeParent = delNode;
        // 后继节点, 可能没有右子节点
        Node successorNode = delNode;
        // 当前位置
        Node current = delNode.getRightChildNode();
        //当前节点没有了左子节点, 则当前节点为后继节点
        while (null != current) {
            successorNodeParent = successorNode;
            successorNode = current;
            current = current.getLeftChildNode();
        }
        LOGGER.info("‘{}’节点的中序后继节点为‘{}’",current.getData(), successorNode.getData());
        LOGGER.info("Will replace node with its successor node {}", successorNode.getData());
        // 后继节点不是右子节点
        if (successorNode != delNode.getRightChildNode()) {
            if(null != successorNode.getRightChildNode()){
                LOGGER.info("and replace {} with its right subtree", successorNode.getRightChildNode().getData());
            }
            // 后继节点的父节点为 后继节点的右子节点
            successorNodeParent.setLeftChildNode(successorNode.getRightChildNode());
            // 后继节点的右节点为 删除节点的右节点
            successorNode.setRightChildNode(delNode.getRightChildNode());
        }
        return successorNode;
    }

}
