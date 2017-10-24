package io.github.lukas2005.DeviceModApps;

import java.util.HashMap;

import com.mrcrayfish.device.api.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author lukas2005
 *
 */
public enum Emoji
{
	CHROME;
	
    public static final ResourceLocation EMOJI_ASSET = new ResourceLocation(Reference.MOD_ID,"textures/gui/emoji.png");

    public static final int ICON_SIZE = 14;
    public static final int GRID_SIZE = 1;

    public static HashMap<Character, Emoji> emojiMapping = new HashMap<>(); 
    
    public static void init() {
    	emojiMapping.put('ﻼ', CHROME);
    }
    
    public int getU()
    {
        return (ordinal() % GRID_SIZE) * ICON_SIZE;
    }

    public int getV()
    {
        return (ordinal() / GRID_SIZE) * ICON_SIZE;
    }

    public void draw(Minecraft mc, float x, float y)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(EMOJI_ASSET);
        RenderUtil.drawRectWithTexture(x, y, getU(), getV(), ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE, 14, 14);
    }
}