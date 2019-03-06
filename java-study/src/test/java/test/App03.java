package test;

import java.util.*;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * description:
 * <br />
 * Created by mace on 2019/1/2 14:11.
 */
public class App03 {

    @Test
    public void test01(){
        String str1 = new String("1");
        str1.intern();
        String str2 = "1";
        System.out.println(str1 == str2);  //结果是 false

        String str3 = new String("2") + new String("2");
        str3.intern();
        String str4 = "22";
        System.out.println(str3 == str4);  //结果是 true

    }

    @Test
    public void test02(){

        for (int i = 0; i < 3; i++) {
             try {
                 System.out.println(i);
                 int num = i/1;
                 System.out.println(num);
                 return;
             } catch (Exception e) {
                 System.out.println("抛出异常");
             }
        }
    }

    @Test
    public void test03(){
        int[] arr = {2,0,1,5,8,3,6,4,9,7};
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void test04(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        System.out.println(list);
        for(Iterator<Integer> iterator = list.iterator(); iterator.hasNext();){
            Integer value = iterator.next();
//            System.out.println(value);
            if(value%2 == 0){
                iterator.remove();
                System.out.println("delete " + value + " success");
            }
        }
        System.out.println(list);
    }

    @Test
    public void test05(){
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            set.add(i);
        }
        set.add(3);
        System.out.println(set);
        for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext();) {
            Integer val = iterator.next();
            if(val%2 == 0){
                iterator.remove();
                System.out.println("delete " + val + " success");
            }
        }
        System.out.println(set);
    }

    @Test
    public void test06(){
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put(i, i);
        }
        System.out.println(map);
        for(Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator(); iterator.hasNext();){
            Map.Entry<Integer, Integer> item = iterator.next();
            Integer key = item.getKey();
            Integer value = item.getValue();
            if(value%2 == 0){
                iterator.remove();
                System.out.println("delete " + key + "=" +value + " success");
            }
        }
        System.out.println(map);



    }

    @Test
    public void test07(){

//        System.out.println(10_000);
    	Iterable<String> aIterable = null;
    	List<String> list = IterableUtils.toList(aIterable);
    }

    public static void main(String[] args) {
        Stack<String> ops = new Stack<>();
        Stack<Double> vals = new Stack<>();
        Scanner in = new Scanner(System.in);
        System.out.println("start");
        boolean flag = true;
        while(flag){
            String s = in.nextLine();
            if("(".equals(s)) continue;
            else if("+".equals(s)) ops.push(s);
            else if("-".equals(s)) ops.push(s);
            else if("*".equals(s)) ops.push(s);
            else if("/".equals(s)) ops.push(s);
            else if(")".equals(s)){
                String op = ops.pop();
                double val = vals.pop();
                if("+".equals(op)) val = vals.pop() + val;
                else if("-".equals(op)) val = vals.pop() - val;
                else if("*".equals(op)) val = vals.pop() * val;
                else if("/".equals(op)) val = vals.pop() / val;
                vals.push(val);
            }
            else if("=".equals(s)) flag = false;
            else vals.push(Double.valueOf(s));
        }
        System.out.println(vals.pop());

    }

    @Test
    public void test08(){
    	long currentTimeSecond = System.currentTimeMillis()/1000;
    	long hourTime = currentTimeSecond - currentTimeSecond%3600;
    	Date currentDate = new Date(hourTime*1000);
    	System.out.println(currentDate);
    	Calendar currentZeroDate = Calendar.getInstance();
    	currentZeroDate.setTime(currentDate);
    	currentZeroDate.add(Calendar.DATE, 1);
    	currentZeroDate.set(Calendar.HOUR, 0);
    	System.out.println(currentZeroDate.getTime());
    }
    
    
    /**
     * 脱敏手机号 隐藏4-7位号码
     */
    @Test
    public void test09(){
    	String phone = "18500348251";
    	String newphone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    	System.out.println(newphone);
    }
    
    @Test
    public void test10(){
    	//当前时间 2019-02-18 15:56:13
    	Date date = DateUtil.date();
    	//一天的开始 零点 2019-02-18 00:00:00
    	Date beginOfDay = DateUtil.beginOfDay(date);
    	//明天的开始 零点 2019-02-19 00:00:00
    	DateTime endOfDay = DateUtil.offsetDay(beginOfDay, 1);
    	System.out.println(date);
    	System.out.println(beginOfDay);
    	System.out.println(endOfDay);
    }
    
    @Test
    public void test11(){
    	// 定时任务
    	Timer timer = new Timer();
    	// 默认时间间隔一天
    	long PERIOD_TIME = 24 * 60 * 60 * 1000;
    	//当前时间 2019-02-18 15:56:13
    	Date date = DateUtil.date();
    	//一天的开始 零点 2019-02-18 00:00:00
    	Date beginOfDay = DateUtil.beginOfDay(date);
    	//明天的开始 零点 2019-02-19 00:00:00
    	DateTime endOfDay = DateUtil.offsetDay(beginOfDay, 1);
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 1.GETSET命令获取当前计数器的值并且重置为0
				
				// 2.把当前获取到的计数器的值存入map作为历史数据 innerkey:时间 value:计数器的值
			}
		}, endOfDay, PERIOD_TIME);
    }
    
    @Test
    public void test12(){
    	// 定时任务
    	Timer timer = new Timer();
    	// 默认时间间隔一分钟
    	long PERIOD_TIME = 60 * 1000;
    	//当前时间 2019-02-18 15:56:13
    	Date date = DateUtil.date();
    	//一天的开始 零点 2019-02-18 15:56:00
    	Date beginOfMin = beginOfMin(date);
    	//明天的开始 零点 2019-02-18 15:57:00
    	Date endOfMin = DateUtil.offsetMinute(beginOfMin, 1);
    	System.out.println(date);
    	System.out.println(beginOfMin);
    	System.out.println(endOfMin);
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("运行定时任务时间: " + DateUtil.date());
				// 1.GETSET命令获取当前计数器的值并且重置为0
				System.out.println("1.GETSET命令获取当前计数器的值并且重置为0");
				// 2.把当前获取到的计数器的值存入map作为历史数据 innerkey:时间 value:计数器的值
				System.out.println("2.把当前获取到的计数器的值存入map作为历史数据 innerkey:时间 value:计数器的值");
			}
		}, endOfMin, PERIOD_TIME);
    }
    
    public Date beginOfMin(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(date.getTime());
    	calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
    	return new DateTime(calendar);
    }
    
    
    @Test
    public void test13(){
    	LinkedList<String> userList = new LinkedList<>();
    	String infos = "18500348251_20190219 17:04:36_首都机场|18500348251_20190219 17:04:37_首都机场|18500348251_20190219 17:04:38_首都机场|18500348251_20190219 17:04:39_首都机场|18500348251_20190219 17:04:40_首都机场|18500348251_20190219 17:04:41_首都机场|18500348251_20190219 17:04:42_首都机场|18500348251_20190219 17:04:43_首都机场|18500348251_20190219 17:04:44_首都机场|18500348251_20190219 17:04:45_首都机场|18500348251_20190219 17:04:46_首都机场|18500348251_20190219 17:04:47_首都机场|18500348251_20190219 17:04:48_首都机场";
    	String[] array = infos.split("\\|", -1);
    	for (String info : array) {
    		if (userList.size() >= 10) {
        		userList.removeLast();
        	}
    		userList.addFirst(info);
		}
    	for (String user : userList) {
			System.out.println(user);
		}
    	
    }
}
