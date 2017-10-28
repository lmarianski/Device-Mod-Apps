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
public enum Emoji implements IIcon
{
	CHROME('ﻼ'),
	SMILE('ﻻ'),
	DERPFISH('ﻺ');
	
    public static final ResourceLocation ICON_ASSET = new ResourceLocation(Reference.MOD_ID,"textures/gui/emoji.png");

    public static final int ICON_SIZE = 10;
    public static final int GRID_SIZE = 3;

    public static HashMap<Character, Emoji> emojiMapping = new HashMap<>(); 

    public char assignedChar;
    
    private Emoji(char assignedChar) {
    	this.assignedChar = assignedChar;
	}
    
    public static void init() {
    	for (Emoji e : values()) {
    		emojiMapping.put(e.assignedChar, e);
    	}
    }
    
	@Override
	public ResourceLocation getIconAsset() {
		return ICON_ASSET;
	}

	@Override
	public int getIconSize() {
		return ICON_SIZE;
	}

	@Override
	public int getGridSize() {
		return GRID_SIZE;
	}
    
	@Override
    public int getU()
    {
        return (ordinal() % GRID_SIZE) * ICON_SIZE;
    }

	@Override
    public int getV()
    {
        return (ordinal() / GRID_SIZE) * ICON_SIZE;
    }

	@Override
    public void draw(Minecraft mc, float x, float y)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ICON_ASSET);
        RenderUtil.drawRectWithTexture(x, y, getU(), getV(), ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE, 200, 200);
    }
    
}