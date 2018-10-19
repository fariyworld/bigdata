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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XmlCompressedCombineFileWritable other = (XmlCompressedCombineFileWritable) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (offset != other.offset)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + (int) (offset ^ (offset >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return this.fileName + "-" + this.offset;
	}

}
