package io.github.lukas2005.DeviceModApps.proxy;

import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.Reference;
import io.github.lukas2005.DeviceModApps.apps.ApplicationHackPrinters;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.nio.file.Paths;

import static io.github.lukas2005.DeviceModApps.apps.ModApps.registerApp;

public class ServerProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        Main.modDataDir = Paths.get(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory().getAbsolutePath(), "mods", Reference.MOD_ID).toFile();
    }

    @Override
    public void init(FMLInitializationEvent e) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {

    }

}
