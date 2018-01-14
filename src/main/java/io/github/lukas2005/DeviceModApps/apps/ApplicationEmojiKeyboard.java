package io.github.lukas2005.DeviceModApps.apps;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.mrcrayfish.device.api.app.IIcon;
import com.mrcrayfish.device.api.app.Layout;

import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.TextArea;
import io.github.lukas2005.DeviceModApps.Emoji;
import io.github.lukas2005.DeviceModApps.Main;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationEmojiKeyboard extends ApplicationBase {

	HashMap<Button, IIcon> buttonEmojiHashMap = new HashMap<>();

	@Override
	public void init() {
		System.out.println(Main.textAreas.size());
		Layout main = new Layout();
		setCurrentLayout(main);
		for (Emoji e : Emoji.values()) {
			main.addComponent(addEmojiButton(e));
		}
		for (IIcon e : Emoji.externalEmojiMapping.values()) {
			main.addComponent(addEmojiButton(e));
		}
	}

	private Button addEmojiButton(IIcon e) {
		Button emojiButton;
		if (e instanceof Emoji) {
			emojiButton = new Button(5+(((Emoji)e).ordinal()*17), 5, 15, 15, e);
		} else {
			emojiButton = new Button(5+((Emoji.externalValues.indexOf(e)+Emoji.values().length-1)*17), 5, 15, 15, e);
		}
		buttonEmojiHashMap.put(emojiButton, e);
		emojiButton.setClickListener((mouseX, mouseY, mouseButton) -> {
			IIcon emoji = /*buttonEmojiHashMap.get(emojiButton)*/e;
			Class<TextArea> textAreaClass = TextArea.class;
			Field isFocusedField = null;
			try {
				isFocusedField = textAreaClass.getDeclaredField("isFocused");
				isFocusedField.setAccessible(true);
				for (WeakReference<TextArea> areaReference : Main.textAreas) {
					TextArea area = areaReference.get();
					if (isFocusedField != null && (boolean)isFocusedField.get(area)) {
						if (emoji instanceof Emoji) {
							area.writeText(Character.toString(((Emoji) emoji).assignedChar));
						} else {
							area.writeText(Character.toString((Character) Emoji.externalEmojiMapping.keySet().toArray()[Emoji.externalValues.indexOf(emoji)]));
						}
					}
					area = null;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		return emojiButton;
	}

	@Override
	public void handleKeyTyped(char character, int code) {
		super.handleKeyTyped(character, code);
		System.out.println((char)code);
	}
	
	@Override
	public void load(NBTTagCompound arg0) {
	}

	@Override
	public void save(NBTTagCompound arg0) {
	}

}
