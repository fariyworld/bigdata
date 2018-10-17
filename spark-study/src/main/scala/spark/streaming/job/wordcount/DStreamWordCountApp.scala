package spark.streaming.job.wordcount

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.Minutes

/**
 * spark-streaming 基于HDFS的实时计算 wordcount
 */
object DStreamWordCountApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("spark_streaming_hdfs_wordcount")

    val ssc = new StreamingContext(conf, Minutes(1))

    ssc.textFileStream("hdfs://cvm:9000/test/data/wcs")
      .flatMap { _.split("\\s") }
      .map(word => (word, 1))
      .reduceByKey(_ + _)
      .print()

    ssc.start()
    ssc.awaitTermination()
  }
}