package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;
import java.nio.file.Paths;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Layout;

import io.github.lukas2005.DeviceModApps.Main;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationMusicPlayer extends Application {

	File appDataDir;
	
	public ApplicationMusicPlayer() {
		appDataDir = Paths.get(Main.modDataDir.getAbsolutePath(), getInfo().getId().getResourcePath()).toFile();
		if (!appDataDir.exists()) appDataDir.mkdirs();
	}

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
