package topology.kafkademo;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;


/**
 * Spout如何从Kafka消息流中解析所需数据
 */
public class SpoutMessageScheme implements Scheme {

	private static final long serialVersionUID = 1284560688269135543L;

	
	/**
	 * 反序列化
	 */
	@Override
	public List<Object> deserialize(ByteBuffer ser) {
		return new Values(byteBufferToString(ser));
	}

	
	/**
	 * 输出的字段声明
	 */
	@Override
	public Fields getOutputFields() {
		return new Fields("msg");
	}

	public static String byteBufferToString(ByteBuffer buffer) {
		CharBuffer charBuffer = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer);
			buffer.flip();
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
