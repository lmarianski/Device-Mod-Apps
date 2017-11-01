package io.github.lukas2005.DeviceModApps;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public interface IIcon {

	ResourceLocation getIconAsset();
	
	int getIconSize();
	
	int getGridSize();
	
	int getU();

    int getV();

    void draw(Minecraft mc, float x, float y);
	
}
