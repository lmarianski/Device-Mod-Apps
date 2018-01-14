package io.github.lukas2005.DeviceModApps.objects;

import java.awt.image.BufferedImage;

public class CheeseDelivery {

	public String targetPlayer;
	public BufferedImage bi;

	public final CheeseType type;

	public CheeseDelivery(String targetPlayer, BufferedImage bi, CheeseType type) {
		this.targetPlayer = targetPlayer;
		this.bi = bi;
		this.type = type;
	}

	public enum CheeseType {
		GOUDA,
		CHEDAR,
		STINKY
	}

}
