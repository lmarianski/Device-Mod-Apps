package io.github.lukas2005.DeviceModApps.objects;

import io.github.lukas2005.DeviceModApps.Main;

import java.net.URL;
import java.util.ArrayList;

public class AppStoreAppInfo {

    public String name;
    public String shortDescription;
    public String description;
    public AppCategory category;

    public ArrayList<URL> urls;
    private transient ArrayList<Class> classes = new ArrayList<>();

    public AppStoreAppInfo(String name, String shortDescription, String description, AppCategory category, ArrayList<URL> urls) {
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

    public ArrayList<Class> getClasses() {
        return classes;
    }

    @Override
    public String toString() {
        return name + ":" + shortDescription + ":" + category;
    }
}
