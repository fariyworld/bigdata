package mapreduce.job.decodeparquet;

import static org.apache.parquet.format.converter.ParquetMetadataConverter.NO_FILTER;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;
/**
 * 解析parquet文件
 * @author 15257
 *
 */
public class DecodeParquetApp {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		String filePath = "D:\\BONC\\Shanxi\\data\\parquet\\002186_0.0";
		try {
			ParquetMetadata parquetMetadata = ParquetFileReader.readFooter(conf, new Path(filePath), NO_FILTER);
			// 获取 parquet 格式文件的全部 schema
			MessageType schema = parquetMetadata.getFileMetaData().getSchema();
			System.out.println(schema.toString());
			List<Type> fields = schema.getFields();
			for (Type field : fields) {
				System.out.println(field.getName());
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
