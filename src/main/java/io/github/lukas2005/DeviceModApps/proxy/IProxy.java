package io.github.lukas2005.DeviceModApps.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {

    void preInit(FMLPreInitializationEvent e);
    void init(FMLInitializationEvent e);
    void postInit(FMLPostInitializationEvent e);

    void preInitReflect(FMLPreInitializationEvent e);
    void initReflect(FMLInitializationEvent e);
    void postInitReflect(FMLPostInitializationEvent e);
}
