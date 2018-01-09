package io.github.lukas2005.DeviceModApps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sun.net.www.protocol.jar.JarURLConnection;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Utils {

    @SideOnly(Side.CLIENT)
    public static void pressUnicode(Robot r, int key_code) {
        r.keyPress(KeyEvent.VK_ALT);
        for(int i = 3; i >= 0; --i) {
            int numpad_kc = key_code / (int) (Math.pow(10, i)) % 10 + KeyEvent.VK_NUMPAD0;
            r.keyPress(numpad_kc);
            r.keyRelease(numpad_kc);
        }
        r.keyRelease(KeyEvent.VK_ALT);
    }

    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    @SideOnly(Side.CLIENT)
    public static void registerFontRenderer(Minecraft mc, FontRenderer renderer) throws Exception {
        if (mc.gameSettings.language != null) {
            renderer.setUnicodeFlag(mc.isUnicode());
            renderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }

        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(renderer);
    }

    public static File getResourceAsFile(String resource) throws IOException {
        String[] splitRes = resource.split("[.]");
        return streamToFile(getResourceAsStream(resource), Files.createTempFile("tmp", "."+splitRes[splitRes.length-1]).toFile());
    }

    public static InputStream getResourceAsStream(String resource) {
        return Utils.class.getClassLoader().getResourceAsStream(resource);
    }

    public static InputStream getResourceAsStream(ResourceLocation resource) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
    }

    public static File streamToFile(InputStream initialStream, File out) throws IOException {
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        OutputStream outStream = new FileOutputStream(out);
        outStream.write(buffer);

        outStream.close();
        out.deleteOnExit();

        return out;
    }

    public static ArrayList<Class> loadAllClassesFromRemoteJar(String path) {
        if (path == null || path.equals("null")) return new ArrayList<>();
        JarFile jarFile = null;
        ArrayList<Class> classes = new ArrayList<>();
        URL jarUrl;
        try {
            jarUrl = new URL("jar:" + path + "!/");
            JarURLConnection conn = new JarURLConnection(jarUrl, null);

            jarFile = conn.getJarFile();
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {jarUrl};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                classes.add(cl.loadClass(className));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                jarFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  classes;
    }

}
