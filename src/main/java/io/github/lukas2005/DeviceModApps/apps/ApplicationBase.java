package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;

import com.mrcrayfish.device.api.app.Application;

public abstract class ApplicationBase extends Application {
	
	public File getAppDataDir() {
		return ModApps.MOD_APP_DATADIR.get(getInfo().getId());
	}
	
}
