package test;


import java.util.ArrayList;
import java.util.List;

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
	
	@Test
	public void test03(){
		
		List<Integer> list = new ArrayList<>();
		list.add(1);
		System.out.println(list.get(0));
	}
	
	
	@Test
	public void test04(){
		
		String path = "FDD_LTE_MRE_HUAWEI_552961_20180301170000.zip";
		
		System.out.println(path.endsWith(".zip"));
		
		System.out.println(System.getProperty("user.home"));
	}
}


