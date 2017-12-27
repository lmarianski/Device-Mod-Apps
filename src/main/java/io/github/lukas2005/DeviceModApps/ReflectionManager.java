package io.github.lukas2005.DeviceModApps;

import javassist.ClassPool;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.openjpa.enhance.InstrumentationFactory;
import org.apache.openjpa.lib.log.NoneLogFactory;

import java.lang.instrument.Instrumentation;

public class ReflectionManager {

	public static final Instrumentation instrumentationInstance = InstrumentationFactory.getInstrumentation(new CustomVerboseLogger());

    public static ClassPool pool = ClassPool.getDefault();

	public static void preInit(FMLPreInitializationEvent e) {
        Main.proxy.preInitReflect(e);
	}

	public static void init(FMLInitializationEvent e) {
        Main.proxy.initReflect(e);
	}

	public static void postInit(FMLPostInitializationEvent e) {
        Main.proxy.postInitReflect(e);
	}

}
