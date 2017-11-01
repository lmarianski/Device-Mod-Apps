package io.github.lukas2005.DeviceModApps.apps;

import java.awt.AWTException;
import java.awt.Robot;

import com.mrcrayfish.device.api.app.Layout;

import io.github.lukas2005.DeviceModApps.Emoji;
import io.github.lukas2005.DeviceModApps.Utils;
import io.github.lukas2005.DeviceModApps.components.EmojiButtonComponent;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationEmojiKeyboard extends ApplicationBase {
	
	@Override
	public void init() {
		Layout main = new Layout();
		setCurrentLayout(main);
		for (Emoji e : Emoji.values()) {
			EmojiButtonComponent emojiButton = new EmojiButtonComponent(5+(e.ordinal()*17), 5, 15, 15, e);
			emojiButton.setClickListener((c, mouseButton) -> {
                EmojiButtonComponent button = (EmojiButtonComponent) c;
                Emoji emoji = (Emoji) button.icon;
                try {
                    Utils.pressUnicode(new Robot(), emoji.assignedChar);
                } catch (AWTException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });
			main.addComponent(emojiButton);
		}
	}
	
	@Override
	public void handleKeyTyped(char character, int code) {
		super.handleKeyTyped(character, code);
		System.out.println((char)code);
	}
	
	@Override
	public void load(NBTTagCompound arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(NBTTagCompound arg0) {
		// TODO Auto-generated method stub

	}

}
