package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.ProgressBar;

import io.github.lukas2005.DeviceModApps.objects.ListedSong;
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;

public class ApplicationMusicPlayer extends ApplicationBase {

	public static ArrayList<ListedSong> defaultRecords = new ArrayList<>();

	boolean isPlaying = false;

	public SoundPlayingThread soundThread;

	ItemList<ListedSong> playList;

	@Override
	public void init(NBTTagCompound nbt) {
		Layout main = new Layout();
		setCurrentLayout(main);

		playList = new ItemList<>(5, 5, 75, 6);


		for (ListedSong e : defaultRecords) {
			if (!playList.getItems().contains(e)) {
				playList.addItem(e);
				markDirty();
			}
		}

		main.addComponent(playList);

		final Button play = new Button(100, 10, 20, 20, Icons.PLAY);
		play.setEnabled(false);
		main.addComponent(play);

		final Button pause = new Button(130, 10, 20, 20, Icons.PAUSE);
		pause.setEnabled(false);
		main.addComponent(pause);

		final Button stop = new Button(160, 10, 20, 20, Icons.STOP);
		stop.setEnabled(false);
		main.addComponent(stop);

		final ProgressBar progress = new ProgressBar(100, 50, 80, 10);
		main.addComponent(progress);

		play.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (playList.getSelectedItem() != null) {
				if (soundThread == null) {
					soundThread = new SoundPlayingThread(playList.getSelectedItem(), progress);
					soundThread.addEndedListener(() -> {
						if (soundThread != null) {
							soundThread = null;
							isPlaying = false;
							pause.setEnabled(isPlaying);
							stop.setEnabled(isPlaying);
							play.setEnabled(!isPlaying);
						}
					});
					soundThread.play();
				} else {
					soundThread.play();
				}
				isPlaying = true;
				if (playList.getSelectedItem().ps == null) {
					pause.setEnabled(true);
				} else {
					pause.setEnabled(false);
				}
				stop.setEnabled(isPlaying);
				play.setEnabled(!isPlaying);
			}
		});

		pause.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (soundThread != null) {
				soundThread.pause();
				isPlaying = false;
				pause.setEnabled(isPlaying);
				stop.setEnabled(!isPlaying);
				play.setEnabled(!isPlaying);
			}
		});

		stop.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (soundThread != null) {
				soundThread.close();
				soundThread = null;
				isPlaying = false;
				pause.setEnabled(isPlaying);
				stop.setEnabled(isPlaying);
				play.setEnabled(!isPlaying);
			}
		});

		playList.setItemClickListener((listedSong, index, mouseButton) -> play.setEnabled(!isPlaying));

	}

	@Override
	public void load(NBTTagCompound nbt) {
		NBTTagCompound songList = nbt.getCompoundTag("songList");
		//playList.removeAll();
		for (String key : songList.getKeySet()) {
			if (Objects.equals(songList.getString(key + "_type"), "FILE")) {
				playList.addItem(new ListedSong(key, new File(songList.getString(key))));
			} else {
				// Need to figure out how to do this
			}
		}
	}

	@Override
	public void save(NBTTagCompound nbt) {
		NBTTagCompound songList = new NBTTagCompound();
		for (ListedSong s : playList.getItems()) {
			if (s.file != null) {
				songList.setString(s.name + "_type", "FILE");
				songList.setString(s.name, s.file.getAbsolutePath());
			} else {
				songList.setString(s.name + "_type", "SOUNDEVENT");
				songList.setString(s.name, s.sound.getSoundName().toString());
			}
		}
		nbt.setTag("songList", songList);
	}

	@Override
	public void onClose() {
		if (soundThread != null) {
			soundThread.close();
			soundThread = null;
		}
	}

	public static void registerDefaultSong(ListedSong listedSong) {
		defaultRecords.add(listedSong);
	}

	public static class SoundPlayingThread extends Thread {

		ListedSong listedSong;

		File audioFile;
		Clip clip;

		public long time = 0;

		ProgressBar progress;
		Thread progressUpdateThread;


		private ArrayList<Runnable> listeners = new ArrayList<>();

		public SoundPlayingThread(ListedSong listedSong, ProgressBar progress) {
			this.listedSong = listedSong;
			this.audioFile = listedSong.file;
			this.progress = progress;

			if (audioFile != null) {
				try {
					AudioInputStream audioInputStream;
					if (audioFile.getName().endsWith(".ogg") || audioFile.getName().endsWith(".mp3")) {
						audioInputStream = createFromOgg(audioFile);
					} else { // wav
						audioInputStream = AudioSystem.getAudioInputStream(audioFile);
					}
					clip = AudioSystem.getClip();
					clip.open(audioInputStream);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public SoundPlayingThread(ListedSong listedSong) {
			this(listedSong, null);
		}

		public void play() {
			if (clip != null) {
				if (progress != null) {
					progress.setMax((int) clip.getMicrosecondLength());

					if (progressUpdateThread == null) {
						progressUpdateThread = new Thread(() -> {
							while (!Thread.interrupted()) {
								progress.setProgress((int) clip.getMicrosecondPosition()); // /1000000
							}
						}, "Progressbar Update Thread");
						progressUpdateThread.start();
					}
				}

				float volume = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.RECORDS);
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(volume));

				Minecraft.getMinecraft().getSoundHandler().stopSounds();
				if (isAlive()) {
					clip.start();
				} else {
					start();
				}
			} else {
				Minecraft mc = Minecraft.getMinecraft();

				if (progress != null) {
					progress.setMax((int) listedSong.length);
				}

				mc.getSoundHandler().stopSounds();
				if (isAlive()) {
					mc.getSoundHandler().playSound(listedSong.ps);
				} else {
					start();
					mc.getSoundHandler().playSound(listedSong.ps);
				}
			}
		}

		public void pause() {
			if (clip != null) {
				time = clip.getMicrosecondPosition();
				clip.stop();
			}
		}

		public void close() {
			if (clip != null) {
				clip.stop();
				clip.close();
			} else {
				Minecraft.getMinecraft().getSoundHandler().stopSound(listedSong.ps);
			}
			time = 0;
			if (progress != null) progress.setProgress((int) time);
			if (progressUpdateThread != null) progressUpdateThread.interrupt();
			this.interrupt();
		}

		AudioInputStream createFromOgg(File fileIn) throws Exception {
			AudioInputStream audioInputStream = null;
			AudioFormat targetFormat;
			try {
				AudioInputStream in = null;
				if (fileIn.getName().endsWith(".ogg")) {
					VorbisAudioFileReader vb = new VorbisAudioFileReader();
					in = vb.getAudioInputStream(fileIn);
				}
				AudioFormat baseFormat = in != null ? in.getFormat() : null;
				targetFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(),
						16,
						baseFormat.getChannels(),
						baseFormat.getChannels() * 2,
						baseFormat.getSampleRate(),
						false);
				audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
			} catch (UnsupportedAudioFileException ue) {
				System.out.println("Unsupported Audio");
			}
			return audioInputStream;
		}

		public void addEndedListener(Runnable run) {
			listeners.add(run);
		}

		@Override
		public void run() {
			if (clip != null) {
				clip.start();
				clip.setMicrosecondPosition(time);
				try {
					Thread.sleep((clip.getMicrosecondLength() - time) / 1000);
				} catch (InterruptedException ignored) {
				}
				close();
				for (Runnable run : listeners) {
					new Thread(run).start();
				}
			} else {
				while (Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(listedSong.ps)) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ignored) {
					}
					time += 100000;
					if (progress != null) progress.setProgress((int) time);
				}
				close();
				for (Runnable run : listeners) {
					new Thread(run).start();
				}
			}
		}
	}
}
