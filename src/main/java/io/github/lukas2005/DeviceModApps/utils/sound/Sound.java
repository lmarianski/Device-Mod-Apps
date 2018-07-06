package io.github.lukas2005.DeviceModApps.utils.sound;

import io.github.lukas2005.DeviceModApps.utils.Utils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class Sound {

	public String name;
	public String dataString;

	public Sound(String name, String dataString) throws IOException {
		this.name = name;
		this.dataString = dataString;
	}

//	public Sound(String name, InputStream stream) {
//		this.name = name;
//		try {
//			this.dataString = "Base64|"+ Base64.encodeBase64String(Utils.readBytesFromStream(stream, false));
//			stream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public Sound(String name, URL url) throws IOException {
		this(name, "URL|"+url.toString());
	}

	public Sound(String name, File file) throws IOException {
		this(name, "FilePath|"+file.getAbsolutePath());
	}

	public Sound(String name, SoundEvent sound) throws IOException {
		this(name, 	"SoundEvent|"+sound.getSoundName().toString());
	}

	public InputStream createNewStream() throws IOException {
		InputStream stream = null;
		String[] dataS = dataString.split("\\|");
		String data = dataS[1];
		String dataType = dataS[0];

		switch (dataType) {
//			case "Base64":
//				stream = new ByteArrayInputStream(Base64.decodeBase64(data));
//				break;
			case "FilePath":
				stream = new FileInputStream(new File(data));
				break;
			case "SoundEvent":
				stream = Utils.getInputStreamForSoundEvent(Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation(data))));
				break;
			case "URL":
				URL url = new URL(data);
				URLConnection conn = url.openConnection();
				conn.connect();

				stream = conn.getInputStream();
				break;
		}

		if (stream != null) stream = new BufferedInputStream(stream);
		return stream;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sound) {
			return this.dataString.equals(((Sound) obj).dataString);
		}
		return false;
	}

	//	public PositionedSound getPositionedSound(BlockPos pos) {
//		if (type == Type.MC_SOUND) {
//			return new PositionedSoundRecord(this.sound, SoundCategory.RECORDS, 1.0F, 1.0F, pos);
//		} else {
//			throw new IllegalStateException("This is a audio file! It can't be positioned!");
//		}
//	}
//
//	public PositionedSound getSound() {
//		if (type == Type.MC_SOUND) {
//			try {
//				return soundRecordConstructor.newInstance(sound, SoundCategory.RECORDS, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
//			} catch (Exception e) {
//				if (ModConfig.DEBUG_MODE) e.printStackTrace();
//				return PositionedSoundRecord.getMusicRecord(sound);
//			}
//		} else {
//			throw new IllegalStateException("This is a audio file! It can't be positioned!");
//		}
//	}

}
