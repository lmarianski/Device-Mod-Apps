package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;

import com.mrcrayfish.device.api.app.Application;

public abstract class ApplicationBase extends Application {
	
	protected File appDataDir;
	
	public File getAppDataDir() {
		return appDataDir;
	}
	
}
