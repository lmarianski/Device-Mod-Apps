package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Layout;
import com.soundcloud.api.ApiWrapper;

import net.minecraft.nbt.NBTTagCompound;

public class ApplicationMusicPlayer extends ApplicationBase {
	
	ApiWrapper api = new ApiWrapper("", "", null, null);
	
	@Override
	public void init() {
		Layout main = new Layout();
		setCurrentLayout(main);
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
