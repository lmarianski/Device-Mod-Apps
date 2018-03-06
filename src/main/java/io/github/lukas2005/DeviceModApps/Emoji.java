package io.github.lukas2005.DeviceModApps;

import com.mrcrayfish.device.api.app.IIcon;
import com.mrcrayfish.device.api.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author lukas2005
 */
public enum Emoji implements IIcon {
	CHROME('ﻼ'),
	SMILE('ﻻ'),
	DERPFISH('ﻺ'),
	CHEESE('ﻹ');

	public static final ResourceLocation ICON_ASSET = new ResourceLocation(Reference.MOD_ID, "textures/gui/emoji.png");

	public static final int ICON_SIZE = 64;
	public static final int GRID_SIZE = 20;


	public static final UnmodifiableList<Emoji> values = new UnmodifiableList<>(Arrays.asList(values()));

	public static char nextFree = 'ﻼ';

	public static HashMap<Character, Emoji> emojiMapping = new HashMap<>();

	public static HashMap<Character, IIcon> externalEmojiMapping = new HashMap<>();
	public static ArrayList<IIcon> externalValues = new ArrayList<>();

	public char assignedChar;

	Emoji(char assignedChar) {
		this.assignedChar = assignedChar;
	}

	static {
		for (Emoji e : values()) {
			emojiMapping.put(e.assignedChar, e);
			if (values.indexOf(e) == values.size() - 1) nextFree = (char) (((int) e.assignedChar) - 1);
		}
	}

	public static void registerExternalEmoji(IIcon icon) {
		externalEmojiMapping.put(nextFree, icon);
		externalValues = new ArrayList<>(externalEmojiMapping.values());
		nextFree = (char) (((int) nextFree) - 1);
	}

	@Override
	public ResourceLocation getIconAsset() {
		return ICON_ASSET;
	}

	@Override
	public int getIconSize() {
		return ICON_SIZE;
	}


	// TODO: REMOVE THIS AFTER MRCRAYFISH WILL MERGE NEW IIcon
	public static int getDrawSize() {
		return 10;
	}

	@Override
	public int getGridWidth() {
		return GRID_SIZE;
	}

	@Override
	public int getGridHeight() {
		return GRID_SIZE;
	}

	// TODO: REMOVE THIS AFTER MRCRAYFISH WILL MERGE NEW IIcon
	@Override
	public int getU() {
		return (ordinal() % GRID_SIZE) * ICON_SIZE;
	}

	@Override
	public int getV() {
		return (ordinal() / GRID_SIZE) * ICON_SIZE;
	}

	@Override
	public void draw(Minecraft mc, int x, int y) {
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(getIconAsset());
		int size = getIconSize();
		int assetWidth = getGridWidth() * size;
		int assetHeight = getGridHeight() * size;
		RenderUtil.drawRectWithTexture(x, y, getU(), getV(), getDrawSize(), getDrawSize(), size, size, assetWidth, assetHeight);
	}
}