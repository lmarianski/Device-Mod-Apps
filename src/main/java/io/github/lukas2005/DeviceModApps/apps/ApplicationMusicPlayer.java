package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Layout;

import net.minecraft.nbt.NBTTagCompound;

public class ApplicationMusicPlayer extends ApplicationBase {

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
