package io.github.lukas2005.DeviceModApps.components;

import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.core.Laptop;

import io.github.lukas2005.DeviceModApps.IIcon;
import net.minecraft.client.Minecraft;

public class EmojiButtonComponent extends Button {

	public IIcon icon;
	
	public EmojiButtonComponent(int left, int top, int buttonWidth, int buttonHeight, IIcon icon) {
		super(left, top, buttonWidth, buttonHeight, "");
		this.icon = icon;
	}
	
	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive,
			float partialTicks) {
		super.render(laptop, mc, x, y, mouseX, mouseY, windowActive, partialTicks);
		icon.draw(mc, (x-5)+15/2, (y-5)+15/2);
	}

}
