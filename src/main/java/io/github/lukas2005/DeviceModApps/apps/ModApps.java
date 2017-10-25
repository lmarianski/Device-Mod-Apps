package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.app.Application;

import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.Reference;
import net.minecraft.util.ResourceLocation;

public class ModApps {

	public static final HashMap<ResourceLocation, File> MOD_APP_DATADIR = new HashMap<>();
	
	public static void init() {
		registerApp(new ResourceLocation(Reference.MOD_ID, "mwb"), ApplicationWebBrowser.class, true);
		registerApp(new ResourceLocation(Reference.MOD_ID, "mta"), ApplicationMusicPlayer.class, true);
		
		registerApp(new ResourceLocation(Reference.MOD_ID, "eka"), ApplicationEmojiKeyboard.class, false);
		registerApp(new ResourceLocation(Reference.MOD_ID, "dlsc"), ApplicationDerpfishLiveSubCount.class, false);
	}
	
	public static void registerApp(ResourceLocation identifier, Class<? extends Application> clazz, boolean needsDataDir) {
		ApplicationManager.registerApplication(identifier, clazz);
		if (needsDataDir) {
			File appDataDir = Paths.get(Main.modDataDir.getAbsolutePath(), identifier.getResourcePath()).toFile();
			if (!appDataDir.exists()) appDataDir.mkdirs();
			MOD_APP_DATADIR.put(identifier, appDataDir);
		}
	}
}
