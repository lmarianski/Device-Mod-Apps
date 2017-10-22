package io.github.lukas2005.DeviceModApps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.SoundCategory;

public class ReflectionManager {

	public static Method getVolumeMethod = null;
	public static SoundManager sndManager = null;
	
	public static Field playingSoundsField;
	
	public static void preInit() {
	
	}
	
	public static void init() {
		//Sound reflection
		SoundHandler sndHandler = Minecraft.getMinecraft().getSoundHandler();
		Class<? extends SoundHandler> sndHandlerClass = sndHandler.getClass();
		Field sndManagerField = null;
		
		try {
			sndManagerField = sndHandlerClass.getDeclaredField("sndManager");
			sndManagerField.setAccessible(true);
			
			sndManager = (SoundManager) sndManagerField.get(sndHandler);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Class<? extends SoundManager> sndManagerClass = sndManager.getClass();
		
		try {
			
			getVolumeMethod = sndManagerClass.getDeclaredMethod("getVolume", SoundCategory.class);
			getVolumeMethod.setAccessible(true);
			
			playingSoundsField = sndManagerClass.getDeclaredField("playingSounds");
			playingSoundsField.setAccessible(true);
		} catch (SecurityException | IllegalArgumentException | NoSuchMethodException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	} 
	
	public static void postInit() {
		
	}
	
}
