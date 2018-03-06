package io.github.lukas2005.DeviceModApps.classloader;

import io.github.lukas2005.DeviceModApps.objects.AppStoreAppInfo;

import java.util.HashMap;

public class MultiClassLoader extends ClassLoader {

	public final HashMap<AppStoreAppInfo, ClassLoader> loaderMap = new HashMap<>();

	/**
	 * This constructor is used to set the parent ClassLoader
	 */
	public MultiClassLoader(ClassLoader parent) {
		super(parent);
	}

	@Override
	public Class loadClass(String name) throws ClassNotFoundException {
		return getParent().loadClass(name);
	}

	public Class loadClass(AppStoreAppInfo appInfo, String url) throws ClassNotFoundException {
		if (!loaderMap.containsKey(appInfo)) {
			RemoteClassLoader classLoader = new RemoteClassLoader(this);
			loaderMap.put(appInfo, classLoader);
			return classLoader.loadClass(url);
		} else {
			RemoteClassLoader classLoader = (RemoteClassLoader) loaderMap.get(appInfo);
			return classLoader.loadClass(url);
		}
	}

	public void unloadApp(AppStoreAppInfo appInfo) {
		loaderMap.remove(appInfo);
	}

	public RemoteClassLoader addClassLoaderForApp(AppStoreAppInfo appInfo) {
		RemoteClassLoader classLoader = new RemoteClassLoader(this);
		loaderMap.put(appInfo, classLoader);
		return classLoader;
	}

}
