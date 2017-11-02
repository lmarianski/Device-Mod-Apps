package io.github.lukas2005.DeviceModApps.apps;

import java.awt.AWTException;
import java.awt.Robot;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import com.mrcrayfish.device.api.app.Layout;

import com.mrcrayfish.device.api.app.component.Text;
import com.mrcrayfish.device.api.app.component.TextArea;
import io.github.lukas2005.DeviceModApps.Emoji;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.Utils;
import io.github.lukas2005.DeviceModApps.components.EmojiButtonComponent;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationEmojiKeyboard extends ApplicationBase {
	
	@Override
	public void init() {
		System.out.println(Main.textAreas.size());
		Layout main = new Layout();
		setCurrentLayout(main);
		for (Emoji e : Emoji.values()) {
			EmojiButtonComponent emojiButton = new EmojiButtonComponent(5+(e.ordinal()*17), 5, 15, 15, e);
			emojiButton.setClickListener((c, mouseButton) -> {
                EmojiButtonComponent button = (EmojiButtonComponent) c;
                Emoji emoji = (Emoji) button.icon;
                Class<TextArea> textAreaClass = TextArea.class;
				Field isFocusedField = null;
				try {
					isFocusedField = textAreaClass.getDeclaredField("isFocused");
					for (WeakReference<TextArea> areaReference : Main.textAreas) {
						TextArea area = areaReference.get();
						if (isFocusedField != null && (boolean)isFocusedField.get(area)) {
							area.writeText(emoji.assignedChar+"");
						}
						area = null;
					}
				} catch (Exception e1) {
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
