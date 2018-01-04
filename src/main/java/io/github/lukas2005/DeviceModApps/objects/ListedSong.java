package io.github.lukas2005.DeviceModApps.objects;

import java.io.File;

import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundEvent;

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
	
	public ListedSong(String name, SoundEvent sound) {
		super();
		this.name = name;
		this.sound = sound;
		this.ps = PositionedSoundRecord.getMusicRecord(sound);
	}

	@Override
	public String toString() {
		return name;
	}
	
}
