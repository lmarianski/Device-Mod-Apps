package io.github.lukas2005.DeviceModApps.proxy;

import com.mrcrayfish.device.api.app.component.TextArea;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.core.network.Router;
import com.mrcrayfish.device.network.PacketHandler;
import com.mrcrayfish.device.network.task.MessageSyncApplications;
import com.mrcrayfish.device.network.task.MessageSyncBlock;
import com.mrcrayfish.device.tileentity.TileEntityRouter;
import io.github.lukas2005.DeviceModApps.*;
import io.github.lukas2005.DeviceModApps.apps.ApplicationHackPrinters;
import io.github.lukas2005.DeviceModApps.swing.SwingUtils;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.lang.instrument.ClassDefinition;
import java.lang.ref.WeakReference;
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

        if (Minecraft.getMinecraft().getSession().getUsername().equalsIgnoreCase("lukas20056")) {
            try {
                // Get the CtClass of the Router
                CtClass cc = ReflectionManager.pool.get(Router.class.getName());

                // Get the first (and only) constructor
                CtConstructor[] consts = cc.getDeclaredConstructors();
                CtConstructor constructor = consts[0];

                // Inject this line of code
                constructor.insertAfter("ApplicationHackPrinters.routers.add(new WeakReference(this));");

                // Get the new bytecode
                byte[] bytecode = cc.toBytecode();

                // Redefine the Router class (replace it with the new bytecode)
                ReflectionManager.instrumentationInstance.redefineClasses(new ClassDefinition(Router.class, bytecode));
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                // Get the CtClass
                CtClass cc = ReflectionManager.pool.get(TileEntityRouter.class.getName());

                // Get the first (and only) constructor
                CtConstructor[] consts = cc.getDeclaredConstructors();
                CtConstructor constructor = consts[0];

                // Inject this line of code
                constructor.insertAfter("ApplicationHackPrinters.tileEntityRouters.add(new WeakReference(this));");

//                CtMethod readFromNbt = cc.getDeclaredMethod("readFromNBT", new CtClass[] {ReflectionManager.pool.get(ResourceLocation.class.toString()) });
//
//                readFromNbt.insertAt(85, "PacketHandler.INSTANCE.sendToServer(new MessageSyncBlock(pos));");

                // Get the new bytecode
                byte[] bytecode = cc.toBytecode();

                // Redefine the class (replace it with the new bytecode)
                ReflectionManager.instrumentationInstance.redefineClasses(new ClassDefinition(TileEntityRouter.class, bytecode));
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                // Get the CtClass of the TextArea
                CtClass cc = ReflectionManager.pool.get(MessageSyncApplications.class.getName());

                // Get the method to inject
                CtMethod onMessage = cc.getDeclaredMethod("onMessage", new CtClass[] {cc, ReflectionManager.pool.get(MessageContext.class.getName())});

                // Replace the method body with my own one
                StringBuilder builder = new StringBuilder();

                builder.append("{ \n");
                builder.append("ArrayList apps = new ArrayList($1.allowedApps); \n");
                builder.append("apps.add(ApplicationManager.getApplication(new ResourceLocation(\""+Reference.MOD_ID+"\", \"hpa\").toString())); \n");
                builder.append("ReflectionHelper.setPrivateValue(CommonProxy.class, MrCrayfishDeviceMod.proxy, apps, new String[]{\"allowedApps\"}); \n");
                builder.append("return null; \n");
                builder.append("}");

                onMessage.setBody(builder.toString());

                // Get the new bytecode
                byte[] bytecode = cc.toBytecode();

                // Redefine the TextArea class (replace it with the new bytecode)
                ReflectionManager.instrumentationInstance.redefineClasses(new ClassDefinition(MessageSyncApplications.class, bytecode));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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
