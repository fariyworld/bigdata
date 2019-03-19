package base;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/19 11:57.
 */
public class App01 {

    @Test
    public void test01() {
        System.out.println(StringDeserializer.class.getCanonicalName());
    }
}
