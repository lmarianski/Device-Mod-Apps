package io.github.lukas2005.DeviceModApps.proxy;

import com.mrcrayfish.device.core.Laptop;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.MyFontRenderer;
import io.github.lukas2005.DeviceModApps.Reference;
import io.github.lukas2005.DeviceModApps.Utils;
import io.github.lukas2005.DeviceModApps.apps.ApplicationMusicPlayer;
import io.github.lukas2005.DeviceModApps.objects.ListedSong;
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

		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Cat", SoundEvents.RECORD_CAT, 183000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Blocks", SoundEvents.RECORD_BLOCKS, 327000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Chirp", SoundEvents.RECORD_CHIRP, 183000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Far", SoundEvents.RECORD_FAR, 152400000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mall", SoundEvents.RECORD_MALL, 190200000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mellohi", SoundEvents.RECORD_MELLOHI, 81600000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Stal", SoundEvents.RECORD_STAL, 138000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Strad", SoundEvents.RECORD_STRAD, 184800000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Wait", SoundEvents.RECORD_WAIT, 214800000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Ward", SoundEvents.RECORD_WARD, 246600000));
		//ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - 11",    SoundEvents.RECORD_11,        66600000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - 13", SoundEvents.RECORD_13, 154800000));
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
	}

}
