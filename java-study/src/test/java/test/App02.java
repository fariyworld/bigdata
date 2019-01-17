package test;

import entity.OverDueMap;
import org.junit.Test;
import scala.Int;
import sun.util.resources.LocaleNames_ga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App02 {

	@Test
	public void test01(){

		String str = "2018-11-16 11:05:38.5414110002018-11-16 11:10:38.546450000646001000000000029424387430012880";
		String[] array = str.split("\001", -1);
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println(System.currentTimeMillis() - formatter.parse("2018-11-16 11:34:54").getTime());
			System.out.println(System.currentTimeMillis());
			System.out.println(formatter.parse("2018-11-16 11:34:54").getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void test02(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("D:\\BONC\\jx\\实时营销\\部署\\baseevent_input_count.log"));
			int linenumber = 0;
			String lineTxt = null;
			Map<Integer, List<Long>> topicCount = new HashMap<>();
			while((lineTxt = reader.readLine())!=null){
				linenumber ++;
				int type = linenumber%10;
				if(type!=1 && type!=0){
//					System.out.println(lineTxt);
					long maxoffset = Long.valueOf(lineTxt.substring(lineTxt.indexOf("==") + 3).trim());
					if(topicCount.containsKey(type)){
						topicCount.get(type).add(maxoffset);
					}else{
						List<Long> tmp = new ArrayList<>();
						tmp.add(maxoffset);
						topicCount.put(type, tmp);
					}
				}
			}

			StringBuilder offset = new StringBuilder();
			for (Map.Entry<Integer, List<Long>> entries : topicCount.entrySet()) {
				offset.delete(0, offset.length());
				List<Long> offsets = entries.getValue();
				for (int i = 1; i < offsets.size(); i++) {
					offset.append(offsets.get(i) - offsets.get(i-1));
					if (i!=offsets.size()-1)
						offset.append("|");
				}
				switch (entries.getKey()){
					case 2:{
						//PartiaFlume
						System.out.println("0|"+offset.toString());
						break;
					}
					case 3:{
						//EMM
						System.out.println("0|"+offset.toString());
						break;
					}
					case 4:{
						//MME
						System.out.println("0|"+offset.toString());
						break;
					}
					case 5:{
						//SMS
						System.out.println("0|"+offset.toString());
						break;
					}
					case 6:{
						//CSFB
						System.out.println("0|"+offset.toString());
						break;
					}
					case 7:{
						//HANDOVER
						System.out.println("0|"+offset.toString());
						break;
					}
					case 8:{
						//lacevent
						System.out.println("0|"+offset.toString());
						break;
					}
					case 9:{
						//scenetopic
						System.out.println("0|"+offset.toString());
						break;
					}
					default: break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Test
	public void test03(){

		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		while (true) {
			threadPool.submit(new Runnable() {
				Set<String> set = new ConcurrentSkipListSet<>();
				@Override
				public void run() {
					set.add(UUID.randomUUID().toString());
				}
			});
		}
	}


	@Test
	public void test04(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("D:\\BONC\\jx\\实时营销\\部署\\everyday_topicsCount.log"));
			int linenumber = 0;
			String lineTxt = null;
			Map<Integer, String> topicCount = new HashMap<>();
			while((lineTxt = reader.readLine())!=null){
				linenumber ++;
				int type = linenumber%10;
				if(type!=0){
					if(type==1){
						topicCount.put(type, (topicCount.get(type) == null ? lineTxt.trim() : topicCount.get(type).concat("|").concat(lineTxt.trim())));
						continue;
					}
					topicCount.put(type, (topicCount.get(type) == null ? lineTxt.substring(lineTxt.lastIndexOf(":")+1) : topicCount.get(type).concat("|").concat(lineTxt.substring(lineTxt.lastIndexOf(":")+1))));
					continue;
				}else{
					continue;
				}
			}
			for (Map.Entry<Integer, String> entries : topicCount.entrySet()) {
				System.out.println(entries.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Test
	public void test05(){

		DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = formatter.parse("2018-11-19 16:48:33");
			System.out.println(date);
			long i = 3200465 / 1000000000L;
			System.out.println(date.getTime()*1000000 + i);
			System.out.println((int)(date.getTime()*1000000 + i));
			System.out.println((int)(date.getTime()/1000 + i));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test06(){
		System.out.println(Integer.MAX_VALUE);
		System.out.println((int)1542599833L);
	}


	@Test
	public void test07(){
		OverDueMap<String, Integer> dueMap = new OverDueMap<>(13);
		dueMap.put("10001", 1);
		dueMap.put("10002", 2);
		dueMap.put("10003", 4);
		dueMap.put("10004", 5);
		System.out.println(dueMap.size());
		System.out.println(dueMap.get("10001"));
	}

	@Test
	public void test08(){

		LinkedList<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);

		System.out.println(list.getFirst());
		System.out.println(list.getLast());

		System.out.println(list.removeLast());
		list.addFirst(0);

		for (Integer integer : list) {
			System.out.println(integer);
		}
	}

	@Test
	public void test09(){
		String str = "428\u00011\u000152358\u00010\u00010\u00010\u0001416\u00013102\u000128985\u00010\u00012018-11-19 16:48:33.578653440\u00012818689326047227317\u0001255\u00010\u00010\u000112\u00010\u000112\u0001255\u0001255\u0001255\u00013322070061\u000136097\u000164\u00010\u00010.0.0.0\u00010.0.0.0\u000133628723\u0001297304\u000110.100.0.3\u000110.107.105.46\u000136412\u00012901\u0001146929409\u000131136\u0001460\u00011\u00011\u00010\u00013200465\u00013200465\u0001\u0001\u0001\u0001\u0001255\u0001255\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010\u00010\u00010\u00010\u00010\u000165535\u000165535\u00010\u00010";
		System.out.println(str.split("\u0001", -1)[40]);
		System.out.println("\u0001");
	}


	@Test
	public void test10(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("D:\\BONC\\jx\\实时营销\\部署\\24小时用户变化.txt"));
			int linenumber = 0;
			String lineTxt = null;
			while((lineTxt = reader.readLine())!=null){
				String[] arr = lineTxt.split("\t", -1);
				String key = arr[0];
				int value = 0;
				for (int i = 1; i < arr.length; i++) {
					value += Integer.valueOf(arr[i]);
				}
				System.out.println(key+"|"+value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void test11(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\15257\\Desktop\\uniq_lacci.log"));
			Set<String> laccis = new HashSet<>();
			String lineTxt = null;
			Map<String, Integer> topicCount = new HashMap<>();
			while((lineTxt = reader.readLine())!=null){
				if(lineTxt.contains("_")){
					laccis.add(lineTxt.substring(lineTxt.lastIndexOf("_")+1));
				}else if(lineTxt.length() >= 8){
					laccis.add(lineTxt);
				}
			}
			for (String lacci : laccis) {
				System.out.println(lacci);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Test
	public void test12(){

		BufferedReader reader = null;
		BufferedReader reader2 = null;
		Set<String> lacci_data = new HashSet<>();
		Set<String> lacci_site = new HashSet<>();
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\15257\\Desktop\\LTE_LAC.txt"));
			reader2 = new BufferedReader(new FileReader("C:\\Users\\15257\\Desktop\\site_lac_ci.log"));
			String lineTxt = null;
			while((lineTxt = reader.readLine())!=null){
				lacci_data.add(lineTxt.trim());
			}
			while((lineTxt = reader2.readLine())!=null) {
				String[] arr = lineTxt.split("\t", -1);
				if (arr[2].equals("4G")) {
					lacci_site.add(String.valueOf(Integer.valueOf(arr[0]) * 256 + Integer.valueOf(arr[1])));
				} else {
					lacci_site.add(arr[0] + "_" + arr[1]);
				}
			}

			for (String lacci_datum : lacci_data) {
				if(!lacci_site.contains(lacci_datum)){
					System.out.println(lacci_datum);
				}
			}

		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		} finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(reader2!=null){
				try {
					reader2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Test
	public void test13(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\15257\\Desktop\\uniq_lacci.log"));
			Set<String> laccis = new HashSet<>();
			String lineTxt = null;
			Map<String, Integer> topicCount = new HashMap<>();
			while((lineTxt = reader.readLine())!=null){
				if(lineTxt.contains("_")){
					laccis.add(lineTxt.substring(lineTxt.lastIndexOf("_")+1));
				}else if(lineTxt.length() >= 8){
					laccis.add(lineTxt);
				}
			}
			for (String lacci : laccis) {
				System.out.println(lacci);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Test
	public void test14(){

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date currentDate = dateFormat.parse("2018-11-01 13:46:33");
			long currentSec = currentDate.getTime() / 1000;
			System.out.println(currentSec - currentSec%300);
			System.out.println(currentSec - currentSec%7200);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test15(){

		System.out.println(new Random().nextInt(1));
	}

	@Test
	public void test16(){
		String str1 = "ab";
		String str2 = "a" + "b";
		System.out.println(str1 == str2);
		System.out.println(str1.equals(str2));
	}
}
