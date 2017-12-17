package io.github.lukas2005.DeviceModApps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.SoundCategory;

public class ReflectionManager {

	public static Method getVolumeMethod = null;
	public static SoundManager sndManager = null;
	
	public static Field playingSoundsField;
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public static void preInit() {
	
	}
	
	public static void init() {
		
		
		try {
			
	    	Class<? extends Minecraft> mcClass = mc.getClass();
			
	        if (mc.gameSettings.language != null)
	        {
	        	Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(mc.isUnicode());
					
				Field mcLanguageManagerField = mcClass.getDeclaredField("mcLanguageManager");
	
				mcLanguageManagerField.setAccessible(true);
	        	
				Minecraft.getMinecraft().fontRenderer.setBidiFlag(((LanguageManager) mcLanguageManagerField.get(mc)).isCurrentLanguageBidirectional());
				
	        }
			
			Field mcResourceManagerField = mcClass.getDeclaredField("mcResourceManager");
	
			mcResourceManagerField.setAccessible(true);
			
			((IReloadableResourceManager) mcResourceManagerField.get(mc)).registerReloadListener(Minecraft.getMinecraft().fontRenderer);
	        
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		//Sound reflection
		SoundHandler sndHandler = Minecraft.getMinecraft().getSoundHandler();
		Class<? extends SoundHandler> sndHandlerClass = sndHandler.getClass();
		Field sndManagerField;
		
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
