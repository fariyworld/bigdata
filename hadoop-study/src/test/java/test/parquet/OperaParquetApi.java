package test.parquet;

import static org.apache.parquet.format.converter.ParquetMetadataConverter.NO_FILTER;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.schema.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperaParquetApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperaParquetApi.class);

	public static MessageType getSchema(String filePath) {
		try {
			Configuration conf = new Configuration();
			ParquetMetadata parquetMetadata = ParquetFileReader.readFooter(conf, new Path(filePath), NO_FILTER);
			// 获取 parquet 格式文件的全部 schema
			return parquetMetadata.getFileMetaData().getSchema();
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("获取schema异常, {}", e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			// TODO: handle finally clause
		}
	}
}
