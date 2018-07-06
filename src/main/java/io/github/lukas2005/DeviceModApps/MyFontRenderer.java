package io.github.lukas2005.DeviceModApps;

import com.mrcrayfish.device.api.app.IIcon;
import com.mrcrayfish.device.core.client.LaptopFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MyFontRenderer extends LaptopFontRenderer {

	Minecraft mc;

	public MyFontRenderer(Minecraft mc) {
		//super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		super(mc);
		this.mc = mc;
	}

	@Override
	protected float renderDefaultChar(int ch, boolean italic) {
		if (!Emoji.emojiMapping.containsKey((char) ch) && !Emoji.externalEmojiMapping.containsKey((char) ch)) {
			return super.renderDefaultChar(ch, italic);
		} else {
			return renderEmoji(ch);
		}
	}

	@Override
	protected float renderUnicodeChar(char ch, boolean italic) {
		if (!Emoji.emojiMapping.containsKey(ch) && !Emoji.externalEmojiMapping.containsKey(ch)) {
			return super.renderUnicodeChar(ch, italic);
		} else {
			return renderEmoji(ch);
		}
	}

	@Override
	public int getCharWidth(char ch) {
		if (!Emoji.emojiMapping.containsKey(ch) && !Emoji.externalEmojiMapping.containsKey(ch)) {
			return super.getCharWidth(ch);
		} else {
			return Emoji.emojiMapping.containsKey(ch) ? Emoji.ICON_SIZE : Emoji.externalEmojiMapping.get(ch).getIconSize();
		}
	}

	public float renderEmoji(int ch) {
		int returnVal = 0;
		IIcon emojiToDraw;

		if (Emoji.emojiMapping.containsKey((char) ch)) {
			emojiToDraw = Emoji.emojiMapping.get((char) ch);
		} else {
			emojiToDraw = Emoji.externalEmojiMapping.get((char) ch);
		}

		if (emojiToDraw != null) {
			emojiToDraw.draw(mc, (int) this.posX, (int) this.posY);
			returnVal = emojiToDraw instanceof Emoji ? Emoji.ICON_SIZE : emojiToDraw.getIconSize();
		}
		return returnVal;
	}

}
