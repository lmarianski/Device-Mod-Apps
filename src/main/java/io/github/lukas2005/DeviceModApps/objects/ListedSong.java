package io.github.lukas2005.DeviceModApps.objects;

import java.io.File;
import java.lang.reflect.Constructor;

import io.github.lukas2005.DeviceModApps.ModConfig;
import net.minecraft.block.BlockJukebox;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ListedSong {

	public String name;
	public File file;
	public SoundEvent sound;
	public PositionedSound ps;
	public long length;

	public ListedSong(String name, File file) {
		super();
		this.name = name;
		this.file = file;
	}

	public ListedSong(String name, SoundEvent sound, long length) {
		super();
		this.name = name;
		this.sound = sound;

		try {
			Constructor<PositionedSoundRecord> constructor = PositionedSoundRecord.class.getDeclaredConstructor(SoundEvent.class,
					SoundCategory.class,
					float.class,
					float.class,
					boolean.class,
					int.class,
					ISound.AttenuationType.class,
					float.class,
					float.class,
					float.class);
			constructor.setAccessible(true);

			this.ps = constructor.newInstance(sound, SoundCategory.RECORDS, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
		} catch (Exception e) {
			if (ModConfig.DEBUG_MODE) e.printStackTrace();
			this.ps = PositionedSoundRecord.getMusicRecord(sound);
		}
		this.length = length;
	}

	@Override
	public String toString() {
		return name;
	}

}
