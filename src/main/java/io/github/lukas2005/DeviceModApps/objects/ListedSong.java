package io.github.lukas2005.DeviceModApps.objects;

import java.io.File;
import java.util.Map;

import io.github.lukas2005.DeviceModApps.ReflectionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ListedSong {

	public String name;
	public File file;
	public SoundEvent sound;
	public PositionedSound ps;
	
	public ListedSong(String name, File file) {
		super();
		this.name = name;
		this.file = file;
	}
	
	@SuppressWarnings("unchecked")
	public ListedSong(String name, SoundEvent sound) {
		super();
		this.name = name;
		this.sound = sound;
		
		// Get the positioned sound using reflection
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
		mc.world.playSound(playerPos, sound, SoundCategory.RECORDS, 1.0F, 1.0F, false);
		
		Map<String, ISound> playingSounds = null;
		
		try {
			playingSounds = (Map<String, ISound>) ReflectionManager.playingSoundsField.get(ReflectionManager.sndManager);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (ISound k : playingSounds.values()) {
			if (k.getSoundLocation() == sound.getSoundName()) {
				ps = (PositionedSound) k;
				break;
			}
		}
		
		Minecraft.getMinecraft().getSoundHandler().stopSound(ps);
	}

	@Override
	public String toString() {
		return name;
	}
	
}
