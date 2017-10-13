package io.github.lukas2005.WebBrowserApp;

import com.mrcrayfish.device.api.ApplicationManager;

import io.github.lukas2005.WebBrowserApp.apps.ApplicationWebBrowser;
import io.github.lukas2005.WebBrowserApp.components.SwingUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.0.3,]")
public class Main {


	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		SwingUtils.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		ApplicationManager.registerApplication(new ApplicationWebBrowser());
	}
}
