package io.github.lukas2005.DeviceModApps.proxy;

import com.mrcrayfish.device.core.Laptop;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.MyFontRenderer;
import io.github.lukas2005.DeviceModApps.Reference;
import io.github.lukas2005.DeviceModApps.utils.Utils;
import io.github.lukas2005.DeviceModApps.apps.ApplicationMusicPlayer;
import io.github.lukas2005.DeviceModApps.utils.sound.Sound;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.nio.file.Paths;

public class ClientProxy implements IProxy {

	public static MyFontRenderer myFontRenderer;

	static Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		Main.modDataDir = Paths.get(mc.mcDataDir.getAbsolutePath(), "mods", Reference.MOD_ID).toFile();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		myFontRenderer = new MyFontRenderer(mc);

		// Font renderer stuff
		try {
			// Register the renderer
			Utils.registerFontRenderer(mc, ClientProxy.myFontRenderer);
			// Swap the laptop renderer to my own one
			Utils.setFinalStatic(Laptop.class.getField("fontRenderer"), ClientProxy.myFontRenderer);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Cat", SoundEvents.RECORD_CAT));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Blocks", SoundEvents.RECORD_BLOCKS));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Chirp", SoundEvents.RECORD_CHIRP));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Far", SoundEvents.RECORD_FAR));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Mall", SoundEvents.RECORD_MALL));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Mellohi", SoundEvents.RECORD_MELLOHI));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Stal", SoundEvents.RECORD_STAL));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Strad", SoundEvents.RECORD_STRAD));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Wait", SoundEvents.RECORD_WAIT));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - Ward", SoundEvents.RECORD_WARD));
			//ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - 11"     ,    SoundEvents.RECORD_11));
			ApplicationMusicPlayer.registerDefaultSong(new Sound("C418 - 13", SoundEvents.RECORD_13));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
	}

}
