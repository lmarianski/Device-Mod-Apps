package io.github.lukas2005.DeviceModApps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;

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

        Class<? extends Minecraft> mcClass = mc.getClass();

        if (mc.gameSettings.language != null) {
            renderer.setUnicodeFlag(mc.isUnicode());

            Field mcLanguageManagerField = mcClass.getDeclaredField("mcLanguageManager");

            mcLanguageManagerField.setAccessible(true);

            renderer.setBidiFlag(((LanguageManager) mcLanguageManagerField.get(mc)).isCurrentLanguageBidirectional());
        }

        Field mcResourceManagerField = mcClass.getDeclaredField("mcResourceManager");

        mcResourceManagerField.setAccessible(true);

        ((IReloadableResourceManager) mcResourceManagerField.get(mc)).registerReloadListener(renderer);
    }

    public File getResourceAsFile(String resource) throws IOException {
        String[] splitRes = resource.split("[.]");
        return streamToFile(getResourceAsStream(resource), Files.createTempFile("tmp", "."+splitRes[splitRes.length-1]).toFile());
    }

    public InputStream getResourceAsStream(String resource) {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    public InputStream getResourceAsStream(ResourceLocation resource) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
    }

    public File streamToFile(InputStream initialStream, File out) throws IOException {
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        OutputStream outStream = new FileOutputStream(out);
        outStream.write(buffer);

        outStream.close();
        out.deleteOnExit();

        return out;
    }

}
