package spark.common

import org.apache.hadoop.conf.Configuration


/**
 * 自定义 hadoop.Configuration 实现序列化
 */
class CustomHadoopConfiguration extends Configuration with Serializable {

}