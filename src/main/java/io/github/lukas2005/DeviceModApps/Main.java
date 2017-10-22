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
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
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
		
		ReflectionManager.preInit();
		
		SwingUtils.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		ReflectionManager.init();	
		
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Cat",     SoundEvents.RECORD_CAT));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Blocks",  SoundEvents.RECORD_BLOCKS));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Chirp",   SoundEvents.RECORD_CHIRP));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Far",     SoundEvents.RECORD_FAR));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mall",    SoundEvents.RECORD_MALL));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mellohi", SoundEvents.RECORD_MELLOHI));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Stal",    SoundEvents.RECORD_STAL));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Strad",   SoundEvents.RECORD_STRAD));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Wait",    SoundEvents.RECORD_WAIT));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Ward",    SoundEvents.RECORD_WARD));
		//ApplicationMusicPlayer.registerSong(new ListedSong("C418 - 11",    SoundEvents.RECORD_11));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - 13",      SoundEvents.RECORD_13));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		ReflectionManager.postInit();
		
		ModApps.init();
	}
	
	public File getResourceAsFile(String resource) throws IOException {
		String[] splitRes = resource.split("[.]");
		return streamToFile(getResourceAsStream(resource), Files.createTempFile("tmp", "."+splitRes[splitRes.length-1]).toFile());
	}
	
	public InputStream getResourceAsStream(String resource) {
		return getClass().getClassLoader().getResourceAsStream(resource);
	}
	
	public InputStream getResourceAsStream(ResourceLocation resource) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
	}
	
	public File streamToFile(InputStream initialStream, File out) throws IOException {
		byte[] buffer = new byte[initialStream.available()];
		initialStream.read(buffer);
		
		OutputStream outStream = new FileOutputStream(out);
		outStream.write(buffer);
		
		outStream.close();
		out.deleteOnExit();
		
		return out;
	}
}
