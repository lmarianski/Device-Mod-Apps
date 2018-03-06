package io.github.lukas2005.DeviceModApps;

import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import sun.net.www.protocol.jar.JarURLConnection;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Utils {

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
	public static void registerFontRenderer(Minecraft mc, FontRenderer renderer) throws Exception {
		if (mc.gameSettings.language != null) {
			renderer.setUnicodeFlag(mc.isUnicode());
			renderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
		}

		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(renderer);
	}

	public static File getResourceAsFile(String resource) throws IOException {
		String[] splitRes = resource.split("[.]");
		return streamToFile(getResourceAsStream(resource), Files.createTempFile("tmp", "." + splitRes[splitRes.length - 1]).toFile());
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
			if (!(i >= parts.length - 1)) builder.append(part + separator);
			i++;
		}
		return builder.toString();
	}

	public static File streamToFile(InputStream initialStream, File out) throws IOException {
		byte[] buffer = new byte[initialStream.available()];
		initialStream.read(buffer);

		OutputStream outStream = new FileOutputStream(out);
		outStream.write(buffer);

		outStream.close();
		out.deleteOnExit();

		return out;
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
	 * Browser init code
	 * @param dataDir
	 * @return
	 */
	public static Browser initJXBrowser(File dataDir) {
		BrowserPreferences.setChromiumSwitches("--remote-debugging-port=9222");

		BrowserContextParams bcp = new BrowserContextParams(dataDir.getAbsolutePath(), "en-us");
		BrowserContext bc = new BrowserContext(bcp);
		Browser b = new Browser(BrowserType.LIGHTWEIGHT, bc);

//		b.addLoadListener(new LoadAdapter() {
//			@Override
//			public void onFinishLoadingFrame(FinishLoadingEvent e) {
//				if (e.isMainFrame()) {
//					DOMDocument document = e.getBrowser().getDocument();
//				}
//			}
//		});

		return b;
	}

	/**
	 * Used for loading in some js scripts on-the-fly (mainly for making the website work better with the browser
	 * @param url
	 * @param integrity
	 */
	public static void loadScriptInJXBrowser(Browser b, String url, String integrity) {
		b.executeJavaScript("function loadScript(url, integrity, callback, callbackParams) {\n" +
				"    // Adding the script tag to the head as suggested before\n" +
				"    let head = document.getElementsByTagName('head')[0];\n" +
				"    let script = document.createElement('script');\n" +
				"    script.type = 'text/javascript';\n" +
				"    script.src = url;\n" +
				"if (integrity != null && integrity != \"null\") {\n" +
				"script.integrity = integrity;\n" +
				"script.crossorigin = \"anonymous\";\n" +
				"}\n" +
				"    if (callback != null) {\n" +
				"let func = function() {\n" +
				"if (callbackParams != null) {\n" +
				"callback(callbackParams[0], callbackParams[1], callbackParams[2]);\n" +
				"} else {\n" +
				"callback();\n" +
				"}\n" +
				"}\n" +
				"script.onreadystatechange = func;\n" +
				"script.onload = func;\n" +
				"} else {\n" +
				"lock = true;\n" +
				"let call = function() {\n" +
				"lock = false;\n" +
				"}\n" +
				"script.onreadystatechange = call;\n" +
				"script.onload = call;\n" +
				"while (lock) {}\n" +
				"}\n" +
				"\n" +
				"loaded.push(url);\n" +
				"\n" +
				"    // Fire the loading\n" +
				"    head.appendChild(script);\n" +
				"} loadScript(\""+url+"\", \""+integrity+"\")");
	}

	/**
	 * Used for loading in some js scripts on-the-fly (mainly for making the website work better with the browser
	 * @param url
	 */
	public static void loadScriptInJXBrowser(Browser b, String url) {
		loadScriptInJXBrowser(b, url, null);
	}

}
