package entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * description:
 * <br />
 * Created by mace on 2018/11/19 15:30.
 */
public class OverDueMap<K, V> {
    private static Logger LOG= LoggerFactory.getLogger(OverDueMap.class);
    private LinkedList<HashMap<K, V>> _buckets;
    private static final int DEFAULT_NUM_BUCKETS = 3;
    private int m_num_bucket = DEFAULT_NUM_BUCKETS;

    public OverDueMap(int numBuckets) {
        if(numBuckets<2) {
            throw new IllegalArgumentException("numBuckets must be >= 2");
        }
        m_num_bucket = numBuckets;
        _buckets = new LinkedList<>();
        for(int i=0; i<numBuckets; i++) {
            _buckets.add(new HashMap<K, V>());
        }
    }

    public boolean containsKey(K key) {
        for(HashMap<K, V> bucket: _buckets) {
            if(bucket.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public V get(K key) {
        for(HashMap<K, V> bucket: _buckets) {
            if(bucket.containsKey(key)) {
                return bucket.get(key);
            }
        }
        return null;
    }

    //每次先清除之前的信息，再把当前信息放在第一个位置，也就是最新的位置
    public void put(K key, V value) {
        remove(key);
        Iterator<HashMap<K, V>> it = _buckets.iterator();
        HashMap<K, V> bucket = it.next();
        bucket.put(key, value);
    }

    /**
     * 遍历Buckets中所有的hashmap，把此imsi的数据清除
     * @param key
     * @return
     */
    public Object remove(K key) {
        for(HashMap<K, V> bucket: _buckets) {
            if(bucket.containsKey(key)) {
                return bucket.remove(key);
            }
        }
        return null;
    }

    public int size() {
        int size = 0;
        for(HashMap<K, V> bucket: _buckets) {
            size+=bucket.size();
        }
        return size;
    }
}
