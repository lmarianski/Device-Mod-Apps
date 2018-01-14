package io.github.lukas2005.DeviceModApps;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ModConfig {

    public static Configuration config;

    public static boolean DEBUG_MODE = false;

    public static void initConfig(File configFile) { // Gets called from preInit
        try {

            // Ensure that the config file exists
            if (!configFile.exists()) configFile.createNewFile();

            // Create the config object
            config = new Configuration(configFile);

            // Load config
            config.load();

            // Read props from config
            Property debugModeProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "debug_mode", // Property name
                    "false", // Default value
                    "Enable the debug mode (useful for reporting issues)"); // Comment

            DEBUG_MODE = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") || debugModeProp.getBoolean();
        } catch (Exception e) {
            // Failed reading/writing, just continue
        } finally {
            // Save props to config IF config changed
            if (config.hasChanged()) config.save();
        }
    }

}
