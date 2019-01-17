package test;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

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

        System.out.println(10_000);
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

}
