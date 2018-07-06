package io.github.lukas2005.DeviceModApps.utils;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sun.net.www.protocol.jar.JarURLConnection;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Utils {

	final static double EPSILON = 1e-12;

	@SideOnly(Side.CLIENT)
	public static void pressUnicode(Robot r, int key_code) {
		r.keyPress(KeyEvent.VK_ALT);
		for (int i = 3; i >= 0; --i) {
			int numpad_kc = key_code / (int) (Math.pow(10, i)) % 10 + KeyEvent.VK_NUMPAD0;
			r.keyPress(numpad_kc);
			r.keyRelease(numpad_kc);
		}
		r.keyRelease(KeyEvent.VK_ALT);
	}

	public static void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}

	@SideOnly(Side.CLIENT)
	public static void registerFontRenderer(Minecraft mc, FontRenderer renderer) {
		if (mc.gameSettings.language != null) {
			renderer.setUnicodeFlag(mc.isUnicode());
			renderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
		}

		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(renderer);
	}

	public static File getResourceAsFile(String resource) throws IOException {
		String[] splitRes = resource.split("\\.");
		return saveStreamToFile(getResourceAsStream(resource), Files.createTempFile("tmp", "." + splitRes[splitRes.length - 1]).toFile());
	}

	public static File getResourceAsFile(ResourceLocation resource) throws IOException {
		String[] splitRes = resource.getResourcePath().split("\\.");
		return saveStreamToFile(getResourceAsStream(resource), Files.createTempFile("tmp", "." + splitRes[splitRes.length - 1]).toFile());
	}

	public static InputStream getResourceAsStream(String resource) {
		return Utils.class.getClassLoader().getResourceAsStream(resource);
	}

	public static InputStream getResourceAsStream(ResourceLocation resource) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
	}

	public static String buildStringWithoutLast(String... parts) {
		return buildStringWithoutLast(' ', parts);
	}

	public static String buildStringWithoutLast(char separator, String... parts) {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (String part : parts) {
			if (!(i >= parts.length - 1)) builder.append(part).append(separator);
			i++;
		}
		return builder.toString();
	}

	public static File saveStreamToFile(InputStream inputStream, File out) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(out);

		outputStream.getChannel().transferFrom(Channels.newChannel(inputStream), 0, inputStream.available());

		outputStream.close();
		return out;
	}

	public static PipedInputStream readFromStreamToNewStream(InputStream in) throws IOException {
		PipedInputStream pipeOut = new PipedInputStream();
		PipedOutputStream pipeIn = new PipedOutputStream(pipeOut);
		byte[] buffer = new byte[1024];

		int numBytes;
		do {
			numBytes = in.read(buffer);
			if (numBytes == -1) {
				break;
			}

			pipeIn.write(buffer, 0, numBytes);
		} while(numBytes >= 1024);

		return pipeOut;
	}

	public static ArrayList<Class> loadAllClassesFromRemoteJar(String path) {
		if (path == null || path.equals("null")) return new ArrayList<>();
		JarFile jarFile = null;
		ArrayList<Class> classes = new ArrayList<>();
		URL jarUrl;
		try {
			jarUrl = new URL("jar:" + path + "!/");
			JarURLConnection conn = new JarURLConnection(jarUrl, null);

			jarFile = conn.getJarFile();
			Enumeration<JarEntry> e = jarFile.entries();

			URL[] urls = {jarUrl};
			URLClassLoader cl = URLClassLoader.newInstance(urls);

			while (e.hasMoreElements()) {
				JarEntry je = e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}
				// -6 because of .class
				String className = je.getName().substring(0, je.getName().length() - 6);
				className = className.replace('/', '.');
				classes.add(cl.loadClass(className));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				jarFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return classes;
	}

	public static String formatNumber(Object num) {
		DecimalFormat f = new DecimalFormat("###,###");
		f.setRoundingMode(RoundingMode.HALF_UP);
		return f.format(num);
	}

	public static String formatNumberString(String s) {
		return formatNumber(Integer.parseInt(s));
	}

	/**
	 * Used for loading in some js scripts on-the-fly (mainly for making the website work better with the browser
	 * @param url
	 * @param integrity
	 */
	public static void loadScriptInJXBrowser(Browser b, String url, @Nullable String integrity) {
		DOMDocument document = b.getDocument();
		DOMElement head = document.getDocumentElement().findElement(By.tagName("head"));

		DOMElement script = document.createElement("script");
		script.setAttribute("type", "text/javascript");
		script.setAttribute("src", url);

		if (integrity != null && !integrity.isEmpty()) {
			script.setAttribute("integrity", integrity);
			script.setAttribute("crossorigin", "anonymous");
		}

		head.appendChild(script);
	}

	/**
	 * Used for loading in some js scripts on-the-fly (mainly for making the website work better with the browser
	 * @param url
	 */
	public static void loadScriptInJXBrowser(Browser b, String url) {
		loadScriptInJXBrowser(b, url, null);
	}

	@SideOnly(Side.CLIENT)
	public static InputStream getInputStreamForSoundEvent(SoundEvent soundEvent) throws IOException {
		return getResourceAsStream(Minecraft.getMinecraft().getSoundHandler().getAccessor(soundEvent.getSoundName()).cloneEntry().getSoundAsOggLocation());
	}

	public static byte[] readBytesFromStream(InputStream inputStream, boolean rewindStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		if (rewindStream && !inputStream.markSupported()) inputStream = new BufferedInputStream(inputStream);

		if (rewindStream) inputStream.mark(inputStream.available());

		int numBytes;
		do {
			numBytes = inputStream.read(buffer);
			if (numBytes == -1) {
				break;
			}

			outputStream.write(buffer, 0, numBytes);
		} while(numBytes >= 1024);

		if (rewindStream) inputStream.reset();

		return outputStream.toByteArray();
	}


	public static double map(double valueCoord1,
							 double startCoord1, double endCoord1,
							 double startCoord2, double endCoord2) {

		if (Math.abs(endCoord1 - startCoord1) < EPSILON) {
			throw new ArithmeticException("/ 0");
		}

		double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
		return ratio * (valueCoord1 - startCoord1) + startCoord2;
	}

	public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2) + Math.pow(z1-z2, 2));
	}

	public static double distance(BlockPos pos1, BlockPos pos2) {
		return distance(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
	}
}
