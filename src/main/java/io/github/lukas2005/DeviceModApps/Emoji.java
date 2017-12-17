package io.github.lukas2005.DeviceModApps;

import com.mrcrayfish.device.api.app.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * 
 * @author lukas2005
 *
 */
public enum Emoji implements IIcon
{
	CHROME('ﻼ'),
	SMILE('ﻻ'),
	DERPFISH('ﻺ'),
	POOP('ﻹ');
	
    public static final ResourceLocation ICON_ASSET = new ResourceLocation(Reference.MOD_ID,"textures/gui/emoji.png");

    public static final int ICON_SIZE = 10;
    public static final int GRID_SIZE = 4;

    public static HashMap<Character, Emoji> emojiMapping = new HashMap<>(); 

    public char assignedChar;
    
    Emoji(char assignedChar) {
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
	public int getGridWidth() {
		return GRID_SIZE;
	}

	@Override
	public int getGridHeight() {
		return GRID_SIZE;
	}
}