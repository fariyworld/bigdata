package mapreduce.job.examples;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestKey implements Serializable, WritableComparable<TestKey> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestKey.class);

	private static final long serialVersionUID = -2802652694207495638L;
	
	private Text imsi;
	private Text eci;
	private Text mmeS1apid;
	private Text timestamp;	
	
	public TestKey() {
		this.imsi = new Text(StringUtils.EMPTY);
		this.eci = new Text(StringUtils.EMPTY);
		this.mmeS1apid = new Text(StringUtils.EMPTY);
		this.timestamp = new Text(StringUtils.EMPTY);
	}

	public TestKey(Text imsi, Text eci, Text mmeS1apid, Text timestamp) {
		this.imsi = imsi;
		this.eci = eci;
		this.mmeS1apid = mmeS1apid;
		this.timestamp = timestamp;
	}

	/**
	 * 序列化，将自定义Key转化成使用流传送的二进制
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		this.imsi.write(out);
		this.eci.write(out);
		this.mmeS1apid.write(out);
		this.timestamp.write(out);
		
	}

	/**
	 * 反序列化，从流中的二进制转换成自定义Key
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		this.imsi.readFields(in);
		this.eci.readFields(in);
		this.mmeS1apid.readFields(in);
		this.timestamp.readFields(in);
	}

	
	@Override
	public int compareTo(TestKey o) {
		LOGGER.info("调用了TestKey.compareTo()....................");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!this.imsi.equals(o.imsi)) {
			return this.imsi.compareTo(o.imsi);
		} else if (!this.eci.equals(o.eci)) {
			return this.eci.compareTo(o.eci);
		} else if (!this.mmeS1apid.equals(o.mmeS1apid)) {
			return this.mmeS1apid.compareTo(o.mmeS1apid);
		} else if (!this.timestamp.equals(o.timestamp)) {
			return this.timestamp.compareTo(o.timestamp);
		}
		return 0;
	}
	
	

	@Override
	public int hashCode() {
		LOGGER.info("调用了TestKey.hashCode()....................");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eci == null) ? 0 : eci.hashCode());
//		result = prime * result + ((imsi == null) ? 0 : imsi.hashCode());
		result = prime * result + ((mmeS1apid == null) ? 0 : mmeS1apid.hashCode());
//		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		LOGGER.info("调用了TestKey.equals()....................");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestKey other = (TestKey) obj;
		if (eci == null) {
			if (other.eci != null)
				return false;
		} else if (!eci.equals(other.eci))
			return false;
		if (imsi == null) {
			if (other.imsi != null)
				return false;
		} else if (!imsi.equals(other.imsi))
			return false;
		if (mmeS1apid == null) {
			if (other.mmeS1apid != null)
				return false;
		} else if (!mmeS1apid.equals(other.mmeS1apid))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	
	
	public Text getImsi() {
		return imsi;
	}

	public void setImsi(Text imsi) {
		this.imsi = imsi;
	}

	public Text getEci() {
		return eci;
	}

	public void setEci(Text eci) {
		this.eci = eci;
	}

	public Text getMmeS1apid() {
		return mmeS1apid;
	}

	public void setMmeS1apid(Text mmeS1apid) {
		this.mmeS1apid = mmeS1apid;
	}

	public Text getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Text timestamp) {
		this.timestamp = timestamp;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("imsi=");
		builder.append(imsi);
		builder.append(", eci=");
		builder.append(eci);
		builder.append(", mmeS1apid=");
		builder.append(mmeS1apid);
		builder.append(", timestamp=");
		builder.append(timestamp);
		return builder.toString();
	}

	
}
