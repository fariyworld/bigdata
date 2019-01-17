package miscellaneous;

public class DataChecker {

	/**
	 * 验证double数值是否有效,即不等于Double.POSITIVE_INFINITY
	 * @param value		待验证的数值
	 * @return			有效:true
	 */
	public static boolean isEffective(Double value){
		return value!=Double.POSITIVE_INFINITY;
	}
}
