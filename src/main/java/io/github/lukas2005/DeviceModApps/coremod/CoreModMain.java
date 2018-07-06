package io.github.lukas2005.DeviceModApps.coremod;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(Loader.MC_VERSION)
public class CoreModMain implements IFMLLoadingPlugin, IFMLCallHook {

	public static File minecraftDir;

	public static String urlBase = "http://maven.teamdev.com/repository/products/com/teamdev/jxbrowser/jxbrowser-%s/%s/jxbrowser-%1$s-%2$s.jar";

	public static final String JXBROWSER_VERSION = "@JxBrowserVersion@";

	public CoreModMain() {
		if (minecraftDir != null)
			return;

		minecraftDir = (File) FMLInjectionData.data()[6];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return CoreModMain.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ClassTransformer.class.getName()};
	}

	@Override
	public Void call() {
		if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) return null;
		try {
			LaunchClassLoader loader = (LaunchClassLoader) getClass().getClassLoader();

			String nativeArch;

			String arch = (OSValidator.is64() ? "64" : "32");
			if (OSValidator.isWindows()) {
				nativeArch = "win"   + arch;
			} else if (OSValidator.isMac()) {
				nativeArch = "mac";
			} else if (OSValidator.isUnix()) {
				nativeArch = "linux" + arch;
			} else {
				throw new Exception("ERROR UNKNOWN OS: NO NATIVE BINARY AVAILABLE");
			}

			URL nativesUrl = new URL(String.format(urlBase, nativeArch, JXBROWSER_VERSION));
			File natives = new File(Paths.get(minecraftDir.getAbsolutePath(), "mods", "lda", "natives", "jxbrowser-"+nativeArch+"-"+JXBROWSER_VERSION+".jar").toString());

			if (!natives.exists()) {
				//System.out.println("DOWNLOADING NATIVES THE GAME MAY FREEZE FOR SOME PERIOD OF TIME");

				//new Thread(() -> {
					try {
						natives.getParentFile().mkdirs();

						natives.createNewFile();


						ReadableByteChannel readableChannel = Channels.newChannel(nativesUrl.openStream());
						OutputStream os = new FileOutputStream(natives);

						((FileOutputStream) os).getChannel().transferFrom(readableChannel, 0, Long.MAX_VALUE);

						loader.addURL(natives.toURI().toURL());
					} catch (IOException e) {
						e.printStackTrace();
					}


				//}).start();
			}
		} catch (Exception e) {
			System.err.println("----------------ERROR DURING THE DOWNLOAD OF NATIVES----------------");
			e.printStackTrace();
		}
		return null;
	}

	public static class OSValidator {

		private static String OS = System.getProperty("os.name").toLowerCase();
		private static String ARCH = System.getProperty("os.arch").toLowerCase();

		public static boolean is64() {

			return (ARCH.contains("64"));

		}

		public static boolean is32() {

			return !is64();

		}

		public static boolean isWindows() {

			return (OS.contains("win"));

		}

		public static boolean isMac() {

			return (OS.contains("mac"));

		}

		public static boolean isUnix() {

			return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);

		}

		public static boolean isSolaris() {

			return (OS.contains("sunos"));

		}

	}

}