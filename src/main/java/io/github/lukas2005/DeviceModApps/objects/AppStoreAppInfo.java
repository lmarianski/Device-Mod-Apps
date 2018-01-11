package io.github.lukas2005.DeviceModApps.objects;

import com.google.gson.annotations.SerializedName;
import com.mrcrayfish.device.MrCrayfishDeviceMod;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.ModConfig;
import io.github.lukas2005.DeviceModApps.Utils;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

    private LinkedHashSet<Class> classes;

    public AppStoreAppInfo(String name, String shortDescription, String description, AppCategory category, ArrayList<URL> urls, ArrayList<String> jars) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.category = category;
        this.urls = urls;
    }

    public void loadClasses() throws ClassNotFoundException {
        if (classes == null) classes = new LinkedHashSet<>();
        for (URL url : urls) {
            Main.classLoader.prefix = Utils.buildStringWithoutLast('.', url.getPath().substring(75).split("/")).replace("/", ".");
            try {
                classes.add(Main.classLoader.loadClass(url.toString()));
            } catch (Exception e) {
                System.err.println("Error loading class from url: " + url.toString() + " Message: "+e.getMessage());
                if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") || ModConfig.DEBUG_MODE) e.printStackTrace();
            }
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
        return new ArrayList<>(classes);
    }

    @Override
    public String toString() {
        return name + ":" + shortDescription + ":" + category;
    }
}
