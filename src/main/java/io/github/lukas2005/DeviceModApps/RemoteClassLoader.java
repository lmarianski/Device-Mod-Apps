package io.github.lukas2005.DeviceModApps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lukas2005
 */
public class RemoteClassLoader extends ClassLoader {

	Map<String, Class> classes = new HashMap<>();
	public String prefix = null;

	/**
	 * This constructor is used to set the parent ClassLoader
	 */
	public RemoteClassLoader(ClassLoader parent) {
		super(parent);
	}

	private Class getClass(URL url, String name) throws ClassNotFoundException {
		byte[] b = null;
		try {
			// This loads the byte code data from the file
			b = loadClassFileData(url);
			// defineClass is inherited from the ClassLoader class
			// that converts byte array into a Class. defineClass is Final
			// so we cannot override it
			Class c = defineClass(name, b, 0, b.length);
			resolveClass(c);
			return c;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Class loadClass(String urlS) throws ClassNotFoundException {
		try {
			if (classes.containsKey(urlS)) return classes.get(urlS);
			URL url = new URL(urlS);
			String[] split = url.getFile().replaceAll("[?].*", "").replace("%24", "$").split("/");
			return getClass(url, prefix == null ? url.getPath().replace("/", ".").replace(".class", "") : prefix + split[split.length - 1].replace(".class", ""));
		} catch (Exception e) {
			if (!(e instanceof MalformedURLException)) e.printStackTrace();
			return getParent().loadClass(urlS);
		}
	}

	/**
	 * makes you be able to load fresh this class again
	 */
	public void removeFromCache(String url) {
		if (classes.containsKey(url)) classes.remove(url);
	}

	private byte[] loadClassFileData(URL url) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = url.openStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;

			while ((n = is.read(byteChunk)) > 0) {
				baos.write(byteChunk, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Perform any other exception handling that's appropriate.
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return baos.toByteArray();
	}
}