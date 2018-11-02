package spark.streaming.job.wordcount

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.Minutes
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.spark.rdd.NewHadoopRDD

/**
 * spark-streaming 基于HDFS的实时计算 wordcount
 */
object DStreamWordCountApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
//      .setMaster("local[*]")
      .setAppName("spark_streaming_hdfs_wordcount")

    val ssc = new StreamingContext(conf, Minutes(1))
    
    val rdd = ssc.sparkContext.newAPIHadoopFile(
        "hdfs://bigdata:9000/", 
        classOf[TextInputFormat], 
        classOf[LongWritable], 
        classOf[Text], 
        ssc.sparkContext.hadoopConfiguration)
      .flatMap(kv => kv._2.toString().split("\\s", -1))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
      
      val rdd1 = ssc.sparkContext.newAPIHadoopRDD(
          ssc.sparkContext.hadoopConfiguration, 
          classOf[TextInputFormat], 
          classOf[LongWritable], 
          classOf[Text])
      
//    ssc.textFileStream(args(0))
//      .flatMap { _.split("\\s") }
//      .map(word => (word, 1))
//      .reduceByKey(_ + _)
//      .print()

    ssc.start()
    ssc.awaitTermination()
  }
}