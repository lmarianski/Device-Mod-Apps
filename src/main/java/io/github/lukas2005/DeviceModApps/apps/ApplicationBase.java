package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Application;

import java.io.File;

public abstract class ApplicationBase extends Application {

	protected File appDataDir;

	public File getAppDataDir() {
		return appDataDir;
	}

}
