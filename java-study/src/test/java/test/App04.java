package test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Test;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/8 17:32.
 */
public class App04 {

    @Test
    public void test01() {
        DateTime now = DateUtil.date();
        System.out.println(now.toString("yyyyMMdd"));
        System.out.println(DateUtil.offsetDay(now, -1).toString("yyyyMMdd"));
        System.out.println(DateUtil.offsetDay(now, -2).toString("yyyyMMdd"));
        System.out.println(DateUtil.offsetDay(now, -3).toString("yyyyMMdd"));
        System.out.println(DateUtil.offsetDay(now, -4).toString("yyyyMMdd"));
        System.out.println(DateUtil.offsetDay(now, -5).toString("yyyyMMdd"));
        System.out.println(DateUtil.offsetDay(now, -6).toString("yyyyMMdd"));
        System.out.println(DateUtil.offsetDay(now, -7).toString("yyyyMMdd"));
    }
}
