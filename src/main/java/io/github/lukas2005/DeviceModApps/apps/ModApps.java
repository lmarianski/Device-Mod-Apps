package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.app.Application;
import com.teamdev.jxbrowser.chromium.Browser;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.Reference;
import io.github.lukas2005.DeviceModApps.Utils;
import io.github.lukas2005.DeviceModApps.components.WebBrowserComponent;
import io.github.lukas2005.DeviceModApps.swing.SwingWrapper;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

public class ModApps {

	public static HashMap<ResourceLocation, ApplicationBase> APPS = new HashMap<>();

	public static ApplicationWebBrowser webBrowser;

	public static void init() {
		webBrowser = (ApplicationWebBrowser) registerApp(new ResourceLocation(Reference.MOD_ID, "mwb"), ApplicationWebBrowser.class, true);

		registerApp(new ResourceLocation(Reference.MOD_ID, "mta"), ApplicationMusicPlayer.class, true);

		//registerApp(new ResourceLocation(Reference.MOD_ID, "eka"), ApplicationEmojiKeyboard.class, false);
		registerApp(new ResourceLocation(Reference.MOD_ID, "dlsc"), ApplicationDerpfishLiveSubCount.class, false);
		registerApp(new ResourceLocation(Reference.MOD_ID, "unas"), ApplicationUnofficialAppStore.class, false);
		//registerApp(new ResourceLocation(Reference.MOD_ID, "hpa"), ApplicationHackPrinters.class, false);
		//registerApp(new ResourceLocation(Reference.MOD_ID, "cdabcff"), ApplicationCheeseDesigner.class, false);

	}

	public static ApplicationBase registerApp(ResourceLocation identifier, Class<? extends Application> clazz, boolean needsDataDir) {
		ApplicationBase app = (ApplicationBase) ApplicationManager.registerApplication(identifier, clazz);
		APPS.put(identifier, app);
		if (needsDataDir) {
			File appDataDir = Paths.get(Main.modDataDir.getAbsolutePath(), identifier.getResourcePath()).toFile();
			if (!appDataDir.exists()) appDataDir.mkdirs();
			app.appDataDir = appDataDir;
		}
		return app;
	}
}
