package spark.job;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.spark.Accumulator;
import org.apache.spark.SerializableWritable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;
import utils.PropertiesUtil;

/**
 * 提交job命令：spark-submit --master yarn --class spark.job.WordCountApplicationWithJava spark-study-0.0.1-SNAPSHOT.jar /test/data/wc.txt
 * 指定master -Dspark.master=local[1]
 * @author 15257
 *
 */
public class WordCountApplicationWithJava {

	private static final Logger LOG = Logger.getLogger(WordCountApplicationWithJava.class);
	
	public static void main(String[] args) {
		
		SparkConf sparkConf = new SparkConf().setAppName("wordcount_spark_java");
		
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		
		LOG.warn("defaultMinPartitions == " + sc.defaultMinPartitions());
		LOG.warn("defaultParallelism == " + sc.defaultParallelism());
				
		final Accumulator<Double> doubleAccumulator = sc.doubleAccumulator(Double.MIN_VALUE, "doubleAccum");
		
//		Accumulator<Long> longAccum = sc.accumulator(Long.MIN_VALUE, "longAccum" , new AccumulatorParam<Long>(){
//			private static final long serialVersionUID = 770193566372144994L;
//			@Override
//			public Long addInPlace(Long init, Long value) {
//				return init + value;
//			}
//			@Override
//			public Long zero(Long init) {
//				return init;
//			}
//			@Override
//			public Long addAccumulator(Long value, Long step) {
//				return value + step;
//			}
//		});
		
		JavaRDD<String> lines = sc.textFile(args[0]);
		
		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

			private static final long serialVersionUID = 7353620161351793108L;

			@Override
			public Iterator<String> call(String arg0) throws Exception {
				PropertiesUtil.getRedisProperties("redis_config.properties", "UTF-8");
				return Arrays.asList(arg0.split(" ")).iterator();
			}
		});
		
		JavaPairRDD<String, Integer> parirs = words.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 2343485898550665984L;

			@Override
			public Tuple2<String, Integer> call(String arg0) throws Exception {
				return new Tuple2<String, Integer>(arg0, 1);
			}
		});
		
		JavaPairRDD<String, Integer> wordcount = parirs.reduceByKey(new Function2<Integer, Integer, Integer>(){

			private static final long serialVersionUID = -8131492004205597175L;

			@Override
			public Integer call(Integer arg0, Integer arg1) throws Exception {
				return arg0 + arg1;
			}
		});
		
		wordcount.foreach(new VoidFunction<Tuple2<String,Integer>>(){

			private static final long serialVersionUID = -5654721647999261307L;

			@Override
			public void call(Tuple2<String, Integer> arg0) throws Exception {
//				longAccum.add(1L);
				doubleAccumulator.add(0.1D);
				LOG.warn(arg0._1+"\t"+arg0._2);
			}
		});
		LOG.warn("doubleAccumulator value == " + doubleAccumulator.value());
//		LOG.warn("longAccum value == " + longAccum.value());
		sc.close();
	}
}
