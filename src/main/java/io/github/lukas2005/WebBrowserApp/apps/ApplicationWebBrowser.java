package io.github.lukas2005.WebBrowserApp.apps;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Layout;

import io.github.lukas2005.WebBrowserApp.Reference;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationWebBrowser extends Application {

	public ApplicationWebBrowser() {
		super(Reference.MOD_ID, "Mineium Web Browser");
	}

	@Override
	public void init() {
		Layout main = new Layout();
		this.setCurrentLayout(main);
		
		
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
