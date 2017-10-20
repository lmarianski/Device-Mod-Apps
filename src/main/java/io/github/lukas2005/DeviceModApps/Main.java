package io.github.lukas2005.DeviceModApps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.github.lukas2005.DeviceModApps.apps.ApplicationMusicPlayer;
import io.github.lukas2005.DeviceModApps.apps.ModApps;
import io.github.lukas2005.DeviceModApps.objects.ListedSong;
import io.github.lukas2005.DeviceModApps.swing.SwingUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.1.0,]")
public class Main {

	public static File modDataDir = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "mods", Reference.MOD_ID).toFile();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		if (!modDataDir.exists()) modDataDir.mkdirs();
		
		SwingUtils.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		try {
			ApplicationMusicPlayer.defaultRecords.add(new ListedSong("C418 - Cat", getResourceAsFile("assets/"+Reference.MOD_ID+"/music/c418/cat.ogg")));
			ApplicationMusicPlayer.defaultRecords.add(new ListedSong("C418 - Blocks", getResourceAsFile("assets/"+Reference.MOD_ID+"/music/c418/blocks.ogg")));
			ApplicationMusicPlayer.defaultRecords.add(new ListedSong("C418 - 11", getResourceAsFile("assets/"+Reference.MOD_ID+"/music/c418/11.ogg")));
			ApplicationMusicPlayer.defaultRecords.add(new ListedSong("C418 - 13", getResourceAsFile("assets/"+Reference.MOD_ID+"/music/c418/13.ogg")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		ModApps.init();
	}
	
	public File getResourceAsFile(String resource) throws IOException {
		return streamToFile(getResourceAsStream(resource), Files.createTempFile("tmp", "").toFile());
	}
	
	public InputStream getResourceAsStream(String resource) {
		return getClass().getClassLoader().getResourceAsStream(resource);
	}
	
	public File streamToFile(InputStream initialStream, File out) throws IOException {
		byte[] buffer = new byte[initialStream.available()];
		initialStream.read(buffer);
		
		OutputStream outStream = new FileOutputStream(out);
		outStream.write(buffer);
		
		outStream.close();
		return out;
	}
}
