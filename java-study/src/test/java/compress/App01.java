package compress;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.omg.CORBA.portable.ValueBase;

public class App01 {

	@Test
	public void test1() {

		try {
			String gzFile = "D:\\tmp\\uncompress\\multicompress.zip";
			ZipFile zipFile = new ZipFile(gzFile/* , Charset.forName("GBK") */);
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
			String gzFile = "D:\\BONC\\Shaanxi\\data\\hw\\LTE_MRGZ_HUAWEI_6.53.64.165_201809280000_201809280015_001.tar.gz";
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
	public void unZip() {
		String zipFile = "D:\\tmp\\uncompress\\multicompress.zip";
		byte[] contents = new byte[1024 * 4];
		String value = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				System.out.println(ze.getName());
				// int len;
				// while ((len = zin.read(contents, 0, contents.length)) != -1)
				// {
				// bos.write(contents, 0, len);
				// }
				IOUtils.copy(zin, bos);
				bos.write("\n\r".getBytes());
				// System.out.println(bos.size());
				// System.out.println(bos.toString());
			}
			value = bos.toString();
			// System.out.println(value);
			bos.close();
			zin.close();
			String[] split = value.split("\n\r", -1);
			System.out.println(split.length);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// TODO: handle finally clause
		}
	}

	@Test
	public void unMultiZip() {
		String path = "D:\\tmp\\uncompress\\multicompress.zip";
		String dest = "D:\\tmp\\uncompress";
		byte[] contents = new byte[1024 * 4];
		String value = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ZipFile zipFile = new ZipFile(path);
			for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();) {
				ZipEntry entry = entries.nextElement();
				System.out.println("解压 - " + entry.getName());
				File destPath = new File(dest, entry.getName());
				if (entry.isDirectory()) {
					destPath.mkdirs();
				} else {
					destPath.createNewFile();
					FileOutputStream fos = new FileOutputStream(destPath);
					InputStream ins = zipFile.getInputStream(entry);
					int len = -1;
					while ((len = ins.read(contents)) != -1) {
						fos.write(contents, 0, len);
					}
					ins.close();
					fos.close();
				}
			}
			value = bos.toString();
			System.out.println(value);
			bos.close();
			String[] split = value.split("\n\r", -1);
			System.out.println(split.length);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// TODO: handle finally clause
		}
	}

	@Test
	public void unMultiZip2() {
		String path = "D:\\tmp\\uncompress\\multicompressGz.tar.gz";
		String dest = "D:\\tmp\\uncompress\\test";
		String value = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		TarArchiveInputStream taris = null;
		TarArchiveEntry entry = null;
		FileOutputStream out = null;
		List<File> fileList = new ArrayList<>();
		TarArchiveInputStream tmpins = null;
		try {
			taris = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(path)));
			while ((entry = taris.getNextTarEntry()) != null) {
				File destPath = new File(dest, entry.getName());
				if (entry.isDirectory()) {
					if (!destPath.exists()) {
						destPath.mkdir();
					}
					continue;
				} else {
					System.out.println(entry.getName());
					System.out.println(entry.getFile() == null);
					fileList.add(destPath);
					destPath.createNewFile();
					out = new FileOutputStream(destPath);
					IOUtils.copy(taris, out);
					out.close();
				}
			}
			taris.close();
			for (File file : fileList) {
				tmpins = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(file)));
				entry = null;
				while ((entry = tmpins.getNextTarEntry()) != null) {
					System.out.println(entry.getName());
					IOUtils.copy(tmpins, bos);
					bos.write("\r\n".getBytes());
				}
				tmpins.close();
			}
			value = bos.toString();
			String[] split = value.split("\r\n", -1);
			System.out.println(split.length);
			for (int i = 0; i < split.length - 1; i++) {
				System.out.println(split[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// TODO: handle finally clause
		}
	}
}
