package io.github.lukas2005.DeviceModApps.utils.sound;

import io.github.lukas2005.DeviceModApps.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.sound.sampled.*;
import java.io.Closeable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SideOnly(Side.CLIENT)
public class SoundPlayer implements Closeable {

	Sound sound;

	Clip clip;

	long timeToResumeAt = 0;
	long length;
	boolean paused = false;

	FloatControl gain;

	ExecutorService executor = Executors.newFixedThreadPool(10);
	Thread setupThread;

	private ArrayList<Runnable> endListeners = new ArrayList<>();
	private ArrayList<Runnable> playListeners = new ArrayList<>();
	private ArrayList<Runnable> resumeListeners = new ArrayList<>();
	private ArrayList<Runnable> startListeners = new ArrayList<>();
	private ArrayList<Runnable> pauseListeners = new ArrayList<>();

	public SoundPlayer(Sound sound, @Nullable BlockPos pos) {
		this.sound = sound;

		setupThread = new Thread(() -> {
			try {
				clip = AudioSystem.getClip();

				//long time = System.currentTimeMillis();
				clip.open(getAudioInputStream(this.sound.createNewStream()));
				//System.out.println(System.currentTimeMillis()-time);
				gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

				this.length = clip.getMicrosecondLength();


			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		setupThread.setDaemon(true);
		setupThread.start();

		executor.execute(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					float volume = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.RECORDS);
					double mult = 1;
					if (pos != null) {
						BlockPos playerPos = Minecraft.getMinecraft().player.getPosition();
						double distance = Utils.distance(playerPos, pos);
						if (distance <= 10) {
							mult = Utils.map(distance, 0, 10, 1, 0);
						} else {
							mult = 0;
						}
					}
					if (gain != null) gain.setValue(20f * (float) Math.log10(volume*mult));
					Thread.sleep(700);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});

	}

	public SoundPlayer(Sound sound) {
		this(sound, null);
	}

	public long getTime() {
		return !isPaused() ? clip.getMicrosecondPosition() : timeToResumeAt;
	}

	public long getLength() {
		return length;
	}

	public boolean isPaused() {
		return paused;
	}

	public void play() {
		executor.execute(() -> {
			try {
				setupThread.join();
			} catch (InterruptedException ignored) { }

			Minecraft.getMinecraft().getSoundHandler().stopSounds();
			if (paused) {
				paused = false;
				clip.start();
				for (Runnable run : resumeListeners) {
					executor.execute(run);
				}
			} else {
				start();
				for (Runnable run : startListeners) {
					executor.execute(run);
				}
			}

			for (Runnable run : playListeners) {
				executor.execute(run);
			}
		});
	}

	public void pause() {
		paused = true;
		timeToResumeAt = clip.getMicrosecondPosition();
		clip.stop();

		for (Runnable run : pauseListeners) {
			executor.execute(run);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}

	/**
	 * Same as SoundPlayer#close
	 */
	public void stop() {
		close();
	}

	@Override
	public void close() {
		executor.execute(() -> {
			clip.stop();
			clip.close();

			for (Runnable run : endListeners) {
				executor.execute(run);
			}

			executor.shutdown();
			try {
				executor.awaitTermination(10, TimeUnit.SECONDS);
				if (!executor.isShutdown()) {
					executor.shutdownNow();
				}
			} catch (InterruptedException ignored) { }
		});
	}

	public void addEndListener(Runnable run) {
		endListeners.add(run);
	}

	public void addPlayListener(Runnable run) {
		playListeners.add(run);
	}

	public void addStartListener(Runnable run) {
		startListeners.add(run);
	}

	public void addResumeListener(Runnable run) {
		resumeListeners.add(run);
	}

	public void addPauseListener(Runnable run) {
		pauseListeners.add(run);
	}

	private void start() {
		executor.execute(this::run);
	}

	private static AudioInputStream getAudioInputStream(InputStream stream) throws Exception {
		AudioInputStream audioInputStream = null;
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(stream);

			AudioFormat baseFormat = in.getFormat();
			AudioFormat format = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false
			);

			audioInputStream = AudioSystem.getAudioInputStream(format, in);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return audioInputStream;
	}

	private void run() {
		clip.start();

		try {
			Thread.sleep((clip.getMicrosecondLength() - timeToResumeAt) / 1000);
		} catch (InterruptedException ignored) {}
		for (Runnable run : endListeners) {
			executor.execute(run);
		}
	}

}
