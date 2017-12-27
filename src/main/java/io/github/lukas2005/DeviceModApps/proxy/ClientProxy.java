package io.github.lukas2005.DeviceModApps.proxy;

import com.mrcrayfish.device.api.app.component.TextArea;
import com.mrcrayfish.device.core.Laptop;
import io.github.lukas2005.DeviceModApps.*;
import io.github.lukas2005.DeviceModApps.swing.SwingUtils;
import javassist.CtClass;
import javassist.CtConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.instrument.ClassDefinition;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Paths;

public class ClientProxy implements IProxy {

    public static MyFontRenderer myFontRenderer;

    static Minecraft mc = Minecraft.getMinecraft();

    public static Method getVolumeMethod = null;
    public static SoundManager sndManager = null;
    public static Field playingSoundsField;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        Main.modDataDir = Paths.get(mc.mcDataDir.getAbsolutePath(), "mods", Reference.MOD_ID).toFile();
        SwingUtils.init();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        myFontRenderer = new MyFontRenderer(mc);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
    }



    @Override
    public void preInitReflect(FMLPreInitializationEvent e) {
        try {
            // Get the CtClass of the TextArea
            CtClass cc = ReflectionManager.pool.get(TextArea.class.getName());

            // Import 2 things
            ReflectionManager.pool.importPackage("io.github.lukas2005.DeviceModApps");
            ReflectionManager.pool.importPackage("java.lang.ref");

            // Get the first (and only) constructor
            CtConstructor[] consts = cc.getDeclaredConstructors();
            CtConstructor constructor = consts[0];

            // Inject this line of code
            constructor.insertAfter("Main.textAreas.add(new WeakReference(this));");

            // Get the new bytecode
            byte[] bytecode = cc.toBytecode();

            // Redefine the TextArea class (replace it with the new bytecode)
            ReflectionManager.instrumentationInstance.redefineClasses(new ClassDefinition(TextArea.class, bytecode));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void initReflect(FMLInitializationEvent e) {
        // Font renderer stuff
        try {
            // Register the renderer
            Utils.registerFontRenderer(mc, ClientProxy.myFontRenderer);
            // Swap the laptop renderer to my own one
            Utils.setFinalStatic(Laptop.class.getField("fontRenderer"), ClientProxy.myFontRenderer);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //Sound reflection

        // Declaring fields and classes
        SoundHandler sndHandler = mc.getSoundHandler();
        Class<? extends SoundHandler> sndHandlerClass = sndHandler.getClass();
        Field sndManagerField;

        // Get the soundManager (not the sound handler)
        try {
            sndManagerField = sndHandlerClass.getDeclaredField("sndManager");
            sndManagerField.setAccessible(true);

            sndManager = (SoundManager) sndManagerField.get(sndHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Get the sound manager class
        Class<? extends SoundManager> sndManagerClass = sndManager.getClass();

        // Get access to the methods
        try {
            getVolumeMethod = sndManagerClass.getDeclaredMethod("getVolume", SoundCategory.class);
            getVolumeMethod.setAccessible(true);

            playingSoundsField = sndManagerClass.getDeclaredField("playingSounds");
            playingSoundsField.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void postInitReflect(FMLPostInitializationEvent e) {

    }
}
