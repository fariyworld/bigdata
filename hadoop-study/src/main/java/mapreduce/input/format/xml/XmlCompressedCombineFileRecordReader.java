package mapreduce.input.format.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.log4j.Logger;


public class XmlCompressedCombineFileRecordReader extends RecordReader<XmlCompressedCombineFileWritable, Text> {

	private static final Logger LOGGER = Logger.getLogger(XmlCompressedCombineFileRecordReader.class);
	
	private long startOffset;
	private long end;
	private long pos;
	private XmlCompressedCombineFileWritable key = new XmlCompressedCombineFileWritable();// 自定义的key
	private Text value = new Text();// 自定义的value

	private FSDataInputStream fsin;
	private DataOutputBuffer buffer = new DataOutputBuffer();

	private FileSystem fs;
	private Path path;

	private int currentIndex;
	private TaskAttemptContext context;
	private CombineFileSplit combineFileSplit;

	private byte[] startTag;
	private byte[] endTag;
	private boolean isMultiLevelCompression = false;
	private String tmpDir = null;
	private List<File> fileList = new ArrayList<>();
	public static final String START_TAG_KEY = "xmlinput.start";
	public static final String END_TAG_KEY = "xmlinput.end";

	private boolean isRead = true;

	public XmlCompressedCombineFileRecordReader(CombineFileSplit split,
		      TaskAttemptContext context, Integer index) {

		this.currentIndex = index;
		this.context = context;
		this.combineFileSplit = split;
	}

	@Override
	public void close() throws IOException {

	}

	/**
	 * 4. 返回 key --> context.getCurrentKey()调用此方法
	 */
	@Override
	public XmlCompressedCombineFileWritable getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	/**
	 * 5. 返回 value --> context.getCurrentValue()调用此方法
	 */
	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return value;
	}

	/**
	 * 3. 获取分片进度
	 */
	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (startOffset == end) {
			return 0.0f;
		} else {
			return Math.min(1.0f, (pos - startOffset) / (float) (end - startOffset));
		}
	}

	/**
	 * 1. 初始化读取资源以及相关的参数
	 */
	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {

		this.combineFileSplit = (CombineFileSplit) split;
		this.context = context;

		Configuration conf = context.getConfiguration();
		// 获取配置
		startTag = conf.get(START_TAG_KEY).getBytes("UTF-8");
		endTag = conf.get(END_TAG_KEY).getBytes("UTF-8");
		isMultiLevelCompression = conf.getBoolean("isMultiLevelCompression", false);
		tmpDir = conf.get("unzompress.dir", SystemUtils.USER_HOME + File.separator + "uncompressTmpDir");
		File root = new File(tmpDir);
		if(!root.exists()){
			root.mkdirs();
			LOGGER.info("成功创建临时解压缩目录 " + tmpDir);
		}
		// 获取分片的开始位置和结束的位置
		this.path = combineFileSplit.getPath(currentIndex);
		this.fs = this.path.getFileSystem(conf);
		this.startOffset = combineFileSplit.getOffset(currentIndex);
		this.end = startOffset + combineFileSplit.getLength(currentIndex);
	}

	/**
	 * 2. context.nextKeyValue()
	 */
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {

		if (key.getFileName() == null) {
			key.setFileName(path.getName());
		}
		value.clear();// 清空value
		if (isRead) {// 只执行一次nextKeyValue() ==> 调用map() 一次
			// 读取文件逻辑
			readFile();
			return true;
		}
		return false;
	}

	public void readFile() throws IOException {
		isRead = false;
		byte[] contents = new byte[(int) (end - startOffset)];
		
		// 1.识别压缩格式
		Configuration conf = context.getConfiguration();
		CompressionCodec codec = findCodec(conf, path);
		// 2.是否压缩
		FSDataInputStream fins = fs.open(path);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();// 文件内容
		
		if (codec == null) {
			// 2.1 判断是否为 .zip 文件 不是则认为是文本 
			if(path.getName().endsWith(".zip")){
				// 2.1.1 是 .zip 文件
				if (isMultiLevelCompression) {
					// 2.1.1.1 多级压缩
					LOGGER.info(path.getName() + " is a compressed file and level is multilevel...");
					deepReadZipCompress(fins, bos);
				} else {
					// 2.1.1.2 一层压缩 解码读取内容
					LOGGER.info(path.getName() + " is a compressed file and level is one...");
					ZipInputStream zins = new ZipInputStream(fins);
					ZipEntry ze;
					while ((ze = zins.getNextEntry()) != null) {
						IOUtils.copy(zins, bos);
						bos.write("\r\n".getBytes());//分隔符, mapper 解析时用 长度-1
					}
					value.set(bos.toByteArray());
					bos.close();
					zins.close();
				}
			}else{
				// 2.1.2 不是压缩
				LOGGER.info(path.getName() + " is not a compressed file...");
				IOUtils.copy(fins, bos);
				value.set(bos.toByteArray());
				bos.close();
				fins.close();
			}
		} else {
			// 2.2 是否多级压缩
			CompressionInputStream ins = codec.createInputStream(fins);
			if (isMultiLevelCompression) {
				// 2.2.1 多级压缩
				LOGGER.info(path.getName() + " is a compressed file and level is multilevel...");
				deepReadCompressionCodec(ins, bos);
			} else {
				// 2.2.2 一层压缩 解码读取内容
				LOGGER.info(path.getName() + " is a compressed file and level is one...");
				int len;
				while((len = ins.read(contents)) != -1){
				      bos.write(contents, 0, len);
				 }
		        value.set(bos.toByteArray());
		        bos.close();
		        fins.close();
		        ins.close();
			}
		}

	}

	/**
	 * 获取压缩解码编码器
	 * 文件扩展名： .deflate	.gz	.bz2 .lzo .lz4 .snappy	
	 * 
	 * @param conf
	 * @param p
	 * @return
	 */
	private CompressionCodec findCodec(Configuration conf, Path path) {
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		return factory.getCodec(path);
	}
	
	private void deepReadZipCompress(FSDataInputStream fins, ByteArrayOutputStream bos) throws IOException{
		ZipInputStream zins = new ZipInputStream(fins);
		ZipInputStream tmpZins = null;
		ZipEntry ze;
		fileList.clear();
		try {
			while ((ze = zins.getNextEntry()) != null) {
				LOGGER.info("uncompressing " + ze.getName());
				File tmp = new File(tmpDir, ze.getName());
				if (ze.isDirectory()) {
					if (!tmp.exists()) {
						tmp.mkdir();
					}
					continue;
				} else {
					tmp.createNewFile();
					fileList.add(tmp);
					FileOutputStream fos = new FileOutputStream(tmp);
					IOUtils.copy(zins, fos);
					fos.close();
				}
			}
			zins.close();
			LOGGER.info("解压完成... 本地读取解压后的文件...");
			for (File tmpFile : fileList) {
				tmpZins = new ZipInputStream(new FileInputStream(tmpFile), Charset.forName("GBK"));
				ZipEntry entry;
				while ((entry = tmpZins.getNextEntry()) != null) {
					IOUtils.copy(tmpZins, bos);
					bos.write("\r\n".getBytes());
				}
				tmpZins.close();
				LOGGER.info("读取完成, 删除临时文件 " + tmpFile.getPath());
				tmpFile.deleteOnExit();
			}
			value.set(bos.toByteArray());
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			if(bos!=null){
				bos.close();
			}
		}
	}
	
	private void deepReadCompressionCodec(CompressionInputStream ins, ByteArrayOutputStream bos) throws IOException{
		fileList.clear();
		TarArchiveInputStream tmpins = null;
		TarArchiveInputStream taris = null;
		TarArchiveEntry entry = null;
		try {
			taris = new TarArchiveInputStream(ins);
			while ((entry = taris.getNextTarEntry()) != null) {
				File destPath = new File(tmpDir, entry.getName());
				if (entry.isDirectory()) {
					if (!destPath.exists()) {
						destPath.mkdir();
					}
					continue;
				} else {
					LOGGER.info("uncompressing " + entry.getName());
					fileList.add(destPath);
					destPath.createNewFile();
					FileOutputStream fos = new FileOutputStream(destPath);
					IOUtils.copy(taris, fos);
					fos.close();
				}
			}
			taris.close();
			LOGGER.info("解压完成... 本地读取解压后的文件...");
			for (File tmpFile : fileList) {
				tmpins = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(tmpFile)));
				entry = null;
				while ((entry = tmpins.getNextTarEntry()) != null) {
					System.out.println(entry.getName());
					IOUtils.copy(tmpins, bos);
					bos.write("\r\n".getBytes());
				}
				tmpins.close();
				LOGGER.info("读取完成, 删除临时文件 " + tmpFile.getPath());
				tmpFile.deleteOnExit();
			}
			value.set(bos.toByteArray());
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// TODO: handle finally clause
			if(bos!=null){
				bos.close();
			}
		}
	}
	
	
}
