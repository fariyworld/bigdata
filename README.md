# bigdata
大数据学习项目

## 打包命令
### 1.hadoop-study
__mvn clean package -Dmaven.test.skip=true__
``` -Dmaven.test.skip=true 不执行测试用例, 也不编译测试用例类...	
``` -DskipTests				不执行测试用例, 但编译测试用例类生成相应的class文件至target/test-classes下...

### 2.spark-study
``` 使用maven-scala插件打包, 注意需依赖pom.xml hadoop、spark相关的jar, 只是在编译使用,并不打入jar
__mvn clean scala:compile compile package -Dmaven.test.skip=true__


## 集群下运行Job命令
``` mapreduce job
__(hadoop jar hadoop-study.jar mapreduce.job.wordcount.WordCountRunner /test/data/wc.txt /test/data/out_wc 1>wc.log 2>&1 &)__

``` spark job
__(spark-submit --master yarn --deploy-mode client --class spark.job.WordCountApplicationWithJava spark-study-0.0.1-SNAPSHOT.jar /test/data/wc.txt 1>wc_java.log 2>&1 &)__

__(spark-submit --master yarn --deploy-mode client --class spark.job.WordCountApplicationWithScala spark-study-0.0.1-SNAPSHOT.jar WordCountAppConfig.xml 1>wc_scala.log 2>&1 &)__

