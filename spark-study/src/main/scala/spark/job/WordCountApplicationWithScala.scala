package spark.job

import scala.collection.mutable.ListBuffer

import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import spark.common.CustomHadoopConfiguration
import spark.tools.ReadConfigFile

/**
 * spark-submit --master yarn --class spark.job.WordCountApplicationWithScala spark-study-0.0.1-SNAPSHOT.jar WordCountAppConfig.xml
 * 
 */
object WordCountApplicationWithScala {

  @transient lazy val LOGGER = Logger.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {

    var configFile = ""

    if (args.length != 1) {
      LOGGER.info("args must be 1, please input the config param xml file.")
      System.exit(1)
    } else {
      LOGGER.info("you input one param, this param is used as config param")
      configFile = args(0)
    }

    //TODO 读取配置
    val conf = new CustomHadoopConfiguration()
    ReadConfigFile.readXmlConfigFile2HadoopConfiguration(configFile, conf)

    val sparkConf = new SparkConf()
      .setAppName("wordcount_spark_scala")
    /*.setMaster("local[1]")*/

    val queuename = conf.get("queuename")
    if (StringUtils.isNotBlank(queuename)) sparkConf.set("spark.yarn.queue", queuename)

    val sc = new SparkContext(sparkConf)

    try {

      val wordsInputPath = conf.get("words.inputpath")
      val resultPath = conf.get("resultpath")

      val fs = FileSystem.get(sc.hadoopConfiguration)
      val outPath = new Path(resultPath)
      if (fs.exists(outPath)) {
        fs.delete(outPath, true)
        LOGGER.warn("输出路径存在, 已删除...")
      }

      val longAccum = sc.longAccumulator("count")
      val doubleAccum = sc.doubleAccumulator("ratio")

      sc.textFile(wordsInputPath, 1)
        .flatMap(lineTxt => StringUtils.splitByWholeSeparatorPreserveAllTokens(lineTxt, " "))
        .mapPartitions(iter => {
          val resultList = new ListBuffer[Tuple2[String, Int]]()
          while (iter.hasNext) {
            //获取当前数据(一行)
            longAccum.add(1L)
            doubleAccum.add(1D)
            val lineTxt = iter.next()
            resultList += Tuple2(lineTxt, 1)
          }
          resultList.iterator
        })
        .reduceByKey(_ + _, 1)
        .saveAsTextFile(resultPath)

      LOGGER.warn("longAccum value == " + longAccum.value)
      LOGGER.warn("doubleAccum value == " + doubleAccum.value)

    } catch {
      // TODO handle error
      case ex: Exception => {
        ex.printStackTrace()
      }
    } finally {
      // TODO stop spark
      sc.stop()
    }
  }
}