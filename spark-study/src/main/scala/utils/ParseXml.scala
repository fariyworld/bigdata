package utils

import scala.xml.Elem
import scala.xml.Node
import scala.xml.XML

import org.apache.log4j.Logger

object ParseXml {
  
    @transient lazy val LOGGER = Logger.getLogger(this.getClass)

  /**
    * 读取xml 至 二级节点
    * @param filePath  xml文件路径
    */
  def readXmlConfieFile(filePath: String): Unit = {
    val xmlFile = XML.loadFile(filePath) //根节点
    val childNodes = xmlFile.child
    for (childNode <- childNodes) {
      if (!"#PCDATA".equals(childNode.label)) {
        if (!hasChildNodes(childNode)) {//一级节点
          LOGGER.info(String.format("%-20s%s", childNode.label, childNode.text))
        } else {
          for (node <- childNode.\("_")) {//二级节点
            println(String.format("%-20s%s", childNode.label + "_" + node.label, node.text))
          }
        }
      }
    }
  }

  def readXml(parentNode: Node, parentLabel :String): Unit ={
    if (!"#PCDATA".equals(parentLabel)) {
      if (!hasChildNodes(parentNode)) {
        LOGGER.info(String.format("%-30s%s", parentLabel, parentNode.text))
      }else{
        for (node <- parentNode.\("_")){
          readXml(node, parentLabel + "_" +node.label)
        }
      }
    }
  }

  def readXml(parentNode: Node, parentLabel :String, xmlContentMap :scala.collection.mutable.Map[String, String]): Unit ={
    if (!"#PCDATA".equals(parentLabel)) {
      if (!hasChildNodes(parentNode)) {
        LOGGER.info(String.format("%-30s%s", parentLabel, parentNode.text))
        xmlContentMap += (parentLabel -> parentNode.text)
      }else{
        for (node <- parentNode.\("_")){
          readXml(node, parentLabel + "_" +node.label, xmlContentMap)
        }
      }
    }
  }

  /**
    * 获取所有没有子节点的节点 标签名称（包括其所有父节点的标签名称; 用 _ 连接） 节点文本内容
    * ocate_test_child（三级节点）
    * @param filePath       xml文件路径
    */
  def readXml(filePath: String): Unit = {
    val rootElem = XML.loadFile(filePath) // 根节点
    readXml(rootElem)
  }


  /**
    *
    * @param rootElem       xml 根节点
    */
  def readXml(rootElem :Elem): Unit = {
    val childNodes = rootElem.child       // 所有一级节点
    for (childNode <- childNodes) {
      readXml(childNode, childNode.label)
    }
  }


  /**
    * 获取所有没有子节点的节点 标签名称（包括其所有父节点的标签名称; 用 _ 连接） 节点文本内容
    * @param filePath
    * @return
    */
  def readXml2Map(filePath: String): scala.collection.mutable.Map[String, String] = {
    val xmlFile = XML.loadFile(filePath) // 根节点
    val childNodes = xmlFile.child       // 所有一级节点
    val xmlContentMap :scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map()
    for (childNode <- childNodes) {
      readXml(childNode, childNode.label, xmlContentMap)
    }
    return xmlContentMap
  }


  def readXml2Map(rootElem :Elem): scala.collection.mutable.Map[String, String] = {
    val childNodes = rootElem.child       // 所有一级节点
    val xmlContentMap :scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map()
    for (childNode <- childNodes) {
      readXml(childNode, childNode.label, xmlContentMap)
    }
    return xmlContentMap
  }

  /**
    * 判断当前节点是否有子节点
    * @param node
    * @return
    */
  def hasChildNodes(node :Node): Boolean = {
    return node.\("_").length != 0
  }
}