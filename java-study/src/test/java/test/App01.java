package test;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import entity.Person;

public class App01 {

	@Test
	public void test01() {
		
		Person person1 = new Person();
		person1.age = 16;
		person1.name = "mace";
//		person1.setAge(16);
//		person1.setName("mace");
		
		Person person2 = new Person();
		person2.age = 16;
		person2.name = "mace";
//		person2.setAge(17);
//		person2.setName("mace");
		
		System.out.println(person1.equals(person2));
	}
	
	@Test
	public void test02() {
		
		Person person1 = new ChinaPerson();
		person1.setAge(16);
		person1.setName("mace");
		
		Person person2 = new ChinaPerson();
		person2.setAge(17);
		person2.setName("mace");
		
		System.out.println(person1.equals(person2));
	}
	
	/**
	 * 测试List的增删改查、排序
	 */
	@Test
	public void test03(){
		
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(3);
		list.add(4);
		list.add(2);
		
		System.out.println(list.get(0));
	}
	
	
	@Test
	public void test04(){
		
		String path = "FDD_LTE_MRE_HUAWEI_552961_20180301170000.zip";
		
		System.out.println(path.endsWith(".zip"));
		
		System.out.println(System.getProperty("user.home"));
	}
	
	@Test
	public void test05(){
		
		String string = "a b";
		System.out.println(string.split("\\s", -1).length);
	}
	
	
	@Test
	public void test06(){
		
		System.out.println(Double.parseDouble("-Infinity") == Double.NEGATIVE_INFINITY);
	}
	
	@Test
	public void test07(){
		
		String empty = null;
		System.out.println(null == empty);
	}
	
	
	@Test
	public void test08(){
		
		flag :for(int i = 0; i < 10; i++){
			
			for(int j = 0; j < 10; j ++){
				
				if (j == 2)
					continue flag;
				else
					System.out.println(i+" _ "+j);
			}
		}
	}
	
	
	@Test
	public void test09(){
		
		Map<String, Integer> map = new HashMap<>();
		map.put("1", 1);
		map.put(null, 2);
		
		System.out.println(map.containsKey(null));
		System.out.println(map.remove("2"));
		System.out.println(map.remove(null));
		System.out.println(map.containsKey(null));
	}
	
	
	@Test
	public void test10(){
		
		String val = "2|a7ce000407c3c1bf|4613393904421980516|862263033691717|17760173817|6|510000|3|1540881273035|1540881273038|0|-1|-1|1|197188314|460-11-24833-6-3867414545|-1|FF.FF.FF.FF|FFFF:FFFF:FFFF:FFFF|1|6.52.238.130|6.53.207.1|36412|36412|4601110479|460110042859826|-1|FFFF|FFFFFFFF|FFFF|FF|FFFFFFFF|24833|6|3867414545|FFFFFFFF|172316|167421|50|1|5|1|9|1|335038614|-1|-1|-1";
		
		System.out.println(val.split("\\|", -1).length);
	}
	
	
	@Test
	public void test11(){
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println((System.currentTimeMillis()) - formatter.parse("2018-10-31 17:21:55").getTime() >= 3600);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void setL0(){
		System.out.println(Person.L0);
		Person.setL0(105D);
	}
	
	@Test
	public void test12(){
		System.out.println(Person.L0);
	}
}


