package spark.job

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object SampleApp {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[1]").setAppName("test")
    val sc = new SparkContext(sparkConf)

    try {

      val map = scala.collection.mutable.Map[String, Int]()
      sc.textFile("file:/D:/BONC/wordcount.txt").foreach(lineTxt => {
        val array = lineTxt.split("\\s", -1)
        map += (array(1) -> 1)
        println(map.toString())
      })
      println(map.size)
      
    } catch {
      // TODO: handle error
      case ex: Exception => ex.printStackTrace()
    } finally {
      // TODO: handle finally clause
      sc.stop()
    }
  }
}