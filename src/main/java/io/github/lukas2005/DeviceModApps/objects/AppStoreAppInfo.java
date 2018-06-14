package io.github.lukas2005.DeviceModApps.objects;

import com.google.gson.annotations.SerializedName;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.ModConfig;
import io.github.lukas2005.DeviceModApps.utils.Utils;
import net.minecraft.nbt.NBTTagString;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class AppStoreAppInfo {

	public String name;
	@SerializedName("short_description")
	public String shortDescription;
	public String id;
	public String description;
	public AppCategory category;
	public boolean needsDataDir;

	public ArrayList<URL> urls;
	public ArrayList<URL> libs;

	private LinkedHashSet<Class> classes;

	public AppStoreAppInfo(String name, String id, String shortDescription, String description, AppCategory category, ArrayList<URL> urls, ArrayList<String> jars, boolean needsDataDir) {
		this.name = name;
		this.id = id;
		this.shortDescription = shortDescription;
		this.description = description;
		this.category = category;
		this.urls = urls;
		this.needsDataDir = needsDataDir;
	}

	public AppStoreAppInfo(String name, String id, String shortDescription, String description, AppCategory category, ArrayList<URL> urls, ArrayList<String> jars) {
		this(name, id, shortDescription, description, category, urls, jars, false);
	}

	public void loadClasses() throws ClassNotFoundException {
		if (classes == null) classes = new LinkedHashSet<>();
		for (URL url : urls) {
			Main.classLoader.addClassLoaderForApp(this).prefix = Utils.buildStringWithoutLast('.', url.getPath().substring(75).split("/")).replace("/", ".");
			try {
				classes.add(Main.classLoader.loadClass(this, url.toString()));
			} catch (Exception e) {
				System.err.println("Error loading class from url: " + url.toString() + " Message: " + e.getMessage());
				if (ModConfig.DEBUG_MODE) e.printStackTrace();
			}
		}
	}

	public void reloadClasses() throws ClassNotFoundException {
		classes.clear();
		unloadClasses();
		for (URL url : urls) {
			classes.add(Main.classLoader.loadClass(url.toString()));
		}
	}

	public void unloadClasses() {
		Main.classLoader.unloadApp(this);
	}

	public ArrayList<Class> getClasses() {
		return new ArrayList<>(classes);
	}

	public NBTTagString saveToNBT() {
		return new NBTTagString(Main.gson.toJson(this));
	}

	public static AppStoreAppInfo readFromNBT(String s) {
		return Main.gson.fromJson(s, AppStoreAppInfo.class);
	}

	@Override
	public String toString() {
		return name + ":" + shortDescription + ":" + category;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return hashCode() == obj.hashCode();
	}
}
