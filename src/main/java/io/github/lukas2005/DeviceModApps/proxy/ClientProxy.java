package io.github.lukas2005.DeviceModApps.proxy;

import com.mrcrayfish.device.core.Laptop;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.MyFontRenderer;
import io.github.lukas2005.DeviceModApps.Reference;
import io.github.lukas2005.DeviceModApps.Utils;
import net.minecraft.client.Minecraft;
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
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
	}

}
