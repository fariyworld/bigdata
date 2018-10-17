package miscellaneous;

import java.math.BigDecimal;

import org.junit.Test;

public class App01 {

	@Test
	public void testDateUtil(){
		
		System.out.println(DateUtil.getElapsedTimeBySecond(1538202489L - 1538202479L));
	}
	
	@Test
	public void testBigDecimal(){
		
		BigDecimal num1 = new BigDecimal(4.00000);
	}
	
	@Test
	public void testSpace(){
		
		String string = "a b";
		System.out.println(string.split("\\s").length);
	}
}
