package io.github.lukas2005.DeviceModApps.apps;

import java.lang.ref.WeakReference;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.TextArea;
import com.mrcrayfish.device.api.app.listener.ClickListener;

import io.github.lukas2005.DeviceModApps.Emoji;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.components.EmojiButtonComponent;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationEmojiKeyboard extends ApplicationBase {
	
	@Override
	public void init() {
		Layout main = new Layout();
		setCurrentLayout(main);
		for (Emoji e : Emoji.values()) {
			EmojiButtonComponent emojiButton = new EmojiButtonComponent(5+(e.ordinal()*17), 5, 15, 15, e);
			emojiButton.setClickListener(new ClickListener() {
				@Override
				public void onClick(Component c, int mouseButton) {
					EmojiButtonComponent button = (EmojiButtonComponent) c;
					for (WeakReference<TextArea> textAreaRef : Main.textAreas) {
						TextArea textArea = textAreaRef.get();
						if (textArea != null && textArea.getFocused()) {
							textArea.writeText(button.icon.assignedChar+"");
						}
					}
				}
			});
			main.addComponent(emojiButton);
		}
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
