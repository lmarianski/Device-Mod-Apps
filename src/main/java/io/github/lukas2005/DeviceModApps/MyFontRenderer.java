package io.github.lukas2005.DeviceModApps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class MyFontRenderer extends FontRenderer {

	Minecraft mc;
	
	public MyFontRenderer(Minecraft mc) {
		super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		this.mc = mc;
	}

	@Override
	protected float renderDefaultChar(int ch, boolean italic) {
		if (!Emoji.emojiMapping.containsKey((char)ch)) {
			return super.renderDefaultChar(ch, italic);
		} else {
			return renderEmoji(ch);
		}
	}
	
	@Override
	protected float renderUnicodeChar(char ch, boolean italic) {
		if (!Emoji.emojiMapping.containsKey(ch)) {
			return super.renderUnicodeChar(ch, italic);
		} else {
			return renderEmoji(ch);
		}
	}

	@Override
	public int getCharWidth(char ch) {
		if (!Emoji.emojiMapping.containsKey(ch)) {
			return super.getCharWidth(ch);
		} else {
			return Emoji.emojiMapping.get(ch).getGridWidth();
		}
	}
	
	public float renderEmoji(int ch) {
		int returnVal = 0;
		Emoji emojiToDraw = Emoji.emojiMapping.get((char)ch);
		if (emojiToDraw != null) {
			emojiToDraw.draw(mc, (int)this.posX, (int)this.posY);
			returnVal = Emoji.ICON_SIZE;
		}
		return returnVal;
	}
	
}
