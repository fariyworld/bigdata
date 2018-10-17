package mapreduce.input.format.xml;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 * This record keeps filename,offset pairs.
 */
public class XmlCompressedCombineFileWritable implements WritableComparable<XmlCompressedCombineFileWritable> {

	private long offset;
	private String fileName;//文件名

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getOffset() {
		return offset;
	}

	public String getFileName() {
		return fileName;
	}

	public XmlCompressedCombineFileWritable() {
		super();
	}

	public XmlCompressedCombineFileWritable(long offset, String fileName) {
		super();
		this.offset = offset;
		this.fileName = fileName;
	}

	public void readFields(DataInput in) throws IOException {
		this.offset = in.readLong();
		this.fileName = Text.readString(in);
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(offset);
		Text.writeString(out, fileName);
	}

	@Override
	public int compareTo(XmlCompressedCombineFileWritable o) {
		int f = this.fileName.compareTo(o.fileName);
		if (f == 0) {
			return (int) Math.signum((double) (this.offset - o.offset));
		}
		return f;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XmlCompressedCombineFileWritable)
			return this.compareTo((XmlCompressedCombineFileWritable) obj) == 0;
		return false;
	}

	@Override
	public int hashCode() {

		final int hashPrime = 47;
		int hash = 13;
		hash = hashPrime * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
		hash = hashPrime * hash + (int) (this.offset ^ (this.offset >>> 16));

		return hash;
	}

	@Override
	public String toString() {
		return this.fileName + "-" + this.offset;
	}

}
