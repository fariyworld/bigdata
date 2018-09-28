package spark.tools

import spark.common.CustomHadoopConfiguration
import utils.ParseXml



object ReadConfigFile {
  
  def readXmlConfigFile2HadoopConfiguration(configFile: String, config: CustomHadoopConfiguration): Unit = {
    val configMap = ParseXml.readXml2Map(configFile)
    configMap.foreach{case (key,value) => config.set(key, value)}
  }
}