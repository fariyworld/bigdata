package business.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.mapreduce.Counters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import business.entity.LOG_COUNTERS;

public class CountersUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountersUtil.class);
	
	public static <T> T createLogBean(Class<T> clazz, Counters counters, Class<? extends Enum> enumType, String... prefixs){
		try {
			List<Enum> counterEnums = fetchCounterEnum(enumType, prefixs);
			T t = clazz.newInstance();
			Field [] fields = clazz.getDeclaredFields();//JavaBean对象属性数组
			for(Enum counterEnum : counterEnums){
				for (Field field : fields) {
					if(field.getName().equalsIgnoreCase(counterEnum.toString().substring(counterEnum.toString().indexOf("_")+1))){
						boolean flag = field.isAccessible();
						field.setAccessible(true);
						field.set(t, counters.findCounter(counterEnum).getValue());
						field.setAccessible(flag);
					}
				}
			}
			return t;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			// TODO: handle finally clause
		}
	}
	private static List<Enum> fetchCounterEnum(Class<? extends Enum> enumType, String... prefixs){
		try {
			Method method = enumType.getMethod("values");
			Enum[] values = (Enum[]) method.invoke(null, null);
			if(prefixs.length == 0){
				return Arrays.asList(values);
			}
			List<Enum> counterEnumList = new ArrayList<>();
			for(String prefix : prefixs){
				for (Enum value : values) {
					if(value.toString().substring(0, value.toString().indexOf("_")).equalsIgnoreCase(prefix)){
						counterEnumList.add(value);
					}
				}
			}
			return counterEnumList;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			// TODO: handle finally clause
		}
	}
	
	public static void main(String[] args) {
		System.out.println(fetchCounterEnum(LOG_COUNTERS.class, "MRLOCATE"));
	}
}
