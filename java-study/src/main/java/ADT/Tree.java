package ADT;

/**
 * description: 二叉树
 * <br />
 * Created by mace on 2019/1/8 15:12.
 */
public interface Tree<T> {

    //查找节点
    Node find(T key);
    //插入新节点
    boolean insert(T key);
    //删除节点
    boolean delete(T key);
}
