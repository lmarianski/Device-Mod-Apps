package io.github.lukas2005.DeviceModApps.objects;

import com.google.gson.annotations.SerializedName;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class AppStoreAppInfo {

    public String name;
    @SerializedName("short_description")
    public String shortDescription;
    public String description;
    public AppCategory category;

    public ArrayList<URL> urls;
    public ArrayList<URL> libs;

    private transient LinkedHashSet<Class> classes = new LinkedHashSet<>();

    public AppStoreAppInfo(String name, String shortDescription, String description, AppCategory category, ArrayList<URL> urls, ArrayList<String> jars) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.category = category;
        this.urls = urls;
    }

    public void loadClasses() throws ClassNotFoundException {
        for (URL url : urls) {
            classes.add(Main.classLoader.loadClass(url.toString()));
        }
    }

    public void reloadClasses() throws ClassNotFoundException {
        classes.clear();
        for (URL url : urls) {
            Main.classLoader.removeFromCache(url.toString());
            classes.add(Main.classLoader.loadClass(url.toString()));
        }
    }

    public LinkedHashSet<Class> getClasses() {
        return classes;
    }

    @Override
    public String toString() {
        return name + ":" + shortDescription + ":" + category;
    }
}
