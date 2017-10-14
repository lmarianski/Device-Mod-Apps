package io.github.lukas2005.WebBrowserApp.apps;

import java.io.File;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Layout;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import io.github.lukas2005.WebBrowserApp.Reference;
import io.github.lukas2005.WebBrowserApp.components.WebBrowserComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationWebBrowser extends Application {

	File dataDir = new File(Minecraft.getMinecraft().mcDataDir.getPath()+"\\mods\\WebBrowserAppData");	
	
	Browser b;
	BrowserView view;
	BrowserContextParams bcp;
	BrowserContext bc;
	
	public ApplicationWebBrowser() {
		super(Reference.MOD_ID, "Mineium Web Browser");
	}

	@Override
	public void init() {
		
		dataDir.mkdirs();
		BrowserContextParams bcp = new BrowserContextParams(dataDir.getAbsolutePath());
		BrowserContext bc = new BrowserContext(bcp);
		b = new Browser(BrowserType.LIGHTWEIGHT, bc);
		
		Layout main = new Layout();
		this.setCurrentLayout(main);
		
		main.addComponent(new WebBrowserComponent(0, 0, main.width, main.height, b));
		
		b.loadURL("https://google.com");
	}
	
	@Override
	public void onClose() {
		super.onClose();
		
		b.dispose();
	}
	
	@Override
	public void load(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub

	}

}
