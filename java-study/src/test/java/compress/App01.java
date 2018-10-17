package compress;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.junit.Test;

public class App01 {

	@Test
	public void test1() {

		try {
			String gzFile = "D:\\tmp\\tmp.zip";
			ZipFile zipFile = new ZipFile(gzFile, Charset.forName("GBK"));
			BufferedReader reader = null;
			String line = null;
			for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();) {
				ZipEntry entry = entries.nextElement();
				System.out.println(entry.getName());
				InputStream ins = zipFile.getInputStream(entry);
				reader = new BufferedReader(new InputStreamReader(ins));
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}
			zipFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test2() {
		try {
			String dest = "D:\\tmp\\uncompress";
			String gzFile = "D:\\BONC\\Shaanxi\\LTE_MRGZ_HUAWEI_6.53.64.165_201809280000_201809280015_001.tar.gz";
			GzipCompressorInputStream gzipCompressorInputStream = new GzipCompressorInputStream(
					new FileInputStream(gzFile), true);
			TarArchiveInputStream tin = new TarArchiveInputStream(gzipCompressorInputStream);
			TarArchiveEntry entry = null;
			OutputStream out = null;
			while ((entry = tin.getNextTarEntry()) != null) {
				File destPath = new File(dest, entry.getName());
				if (entry.isDirectory()) {
					destPath.mkdirs();
					continue;
				}
				System.out.println(entry.getName());
				destPath.createNewFile();
				out = new FileOutputStream(destPath);
				IOUtils.copy(tin, out);
				out.close();
			}
			gzipCompressorInputStream.close();
			tin.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test03() {
		String dest = "D:\\tmp\\uncompress";
		File root = new File(dest);
		String tarGzFile = "D:\\BONC\\Shaanxi\\LTE_MRGZ_HUAWEI_6.53.64.165_201809280000_201809280015_001.tar.gz";
		TarArchiveInputStream taris = null;
		GZIPInputStream gis = null;
		FileInputStream fis = null; // 建立输入流，用于从压缩文件中读出文件
		OutputStream out = null;
		TarArchiveEntry entry = null;
		TarArchiveEntry[] subEntries = null;
		ByteArrayOutputStream bos = null;
		byte[] buf = new byte[1024 * 1024];
		int len = 0;
		GZIPInputStream tmp = null;
		try {
			fis = new FileInputStream(tarGzFile);
			gis = new GZIPInputStream(fis);
			taris = new TarArchiveInputStream(gis);

			while ((entry = taris.getNextTarEntry()) != null) {
				File destPath = new File(dest, entry.getName());
				if (entry.isDirectory()) {
					if (!destPath.exists()) {
						destPath.mkdir();
					}
					continue;
				} else {
					// System.out.println(entry.getName());
					destPath.createNewFile();
					out = new FileOutputStream(destPath);
					IOUtils.copy(taris, out);
					out.close();
					out = null;
					tmp = new GZIPInputStream(new FileInputStream(destPath));
					bos = new ByteArrayOutputStream();
					while ((len = tmp.read(buf)) != -1) {
						bos.write(buf, 0, len);
					}
					// System.out.println(new String(bos.toByteArray()));
					bos.close();
					tmp.close();
					tmp = null;
					destPath.delete();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (taris != null) {
				try {
					taris.close();
				} catch (Exception ce) {
					taris = null;
				}
			}
			if (gis != null) {
				try {
					gis.close();
				} catch (Exception ce) {
					gis = null;
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception ce) {
					fis = null;
				}
			}
		}
	}

	@Test
	public void tarGz() {
		TarInputStream tarInputStream = null;
		String tarGzFile = "D:\\BONC\\Shaanxi\\LTE_MRGZ_HUAWEI_6.53.64.165_201809280000_201809280015_001.tar.gz";
		try {
			File file = new File(tarGzFile);
			tarInputStream = new TarInputStream(new GZIPInputStream(new FileInputStream(file)));
			
			TarEntry entry = null;
			while( (entry = tarInputStream.getNextEntry()) != null ){  
				if(entry.isDirectory()){//是目录
					System.out.println(entry.getName());
				}else{//是文件
					System.out.println(entry.getName());
					System.out.println(entry.getFile()== null);
				}
			}
			
			

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// TODO: handle finally clause
		}
	}
}
