package io.github.lukas2005.DeviceModApps;

import java.io.File;
import java.nio.file.Paths;

import com.mrcrayfish.device.api.ApplicationManager;

import io.github.lukas2005.DeviceModApps.apps.ApplicationMusicPlayer;
import io.github.lukas2005.DeviceModApps.apps.ApplicationWebBrowser;
import io.github.lukas2005.DeviceModApps.swing.SwingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.0.3,]")
public class Main {

	public static File modDataDir = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "mods", Reference.MOD_ID).toFile();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		if (!modDataDir.exists()) modDataDir.mkdirs();
		
		SwingUtils.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "mwb"), ApplicationWebBrowser.class);
		ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "mta"), ApplicationMusicPlayer.class);
	}
}
