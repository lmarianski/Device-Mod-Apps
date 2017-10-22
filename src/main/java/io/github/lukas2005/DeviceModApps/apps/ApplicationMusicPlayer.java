package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.ProgressBar;
import com.mrcrayfish.device.api.app.listener.ClickListener;

import io.github.lukas2005.DeviceModApps.ReflectionManager;
import io.github.lukas2005.DeviceModApps.objects.ListedSong;
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;

public class ApplicationMusicPlayer extends ApplicationBase {
	
	ItemList<ListedSong> playList;
	
	public static ArrayList<ListedSong> defaultRecords = new ArrayList<>();
	
	boolean isPlaying = false;
	
	public SoundPlayingThread soundThread;
	
	@Override
	public void init() {
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
		
		final Button play = new Button("Play", 100, 10, 20, 20);
		
		main.addComponent(play);
		
		final Button pause = new Button("Pause", 130, 10, 20, 20);
		pause.setEnabled(false);
		main.addComponent(pause);
		
		final Button stop = new Button("Stop", 160, 10, 20, 20);
		stop.setEnabled(false);
		main.addComponent(stop);
		
		final ProgressBar progress = new ProgressBar(100, 50, 80, 10);
		main.addComponent(progress);
		
		play.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				if (playList.getSelectedItem() != null) {
					if (soundThread == null) { 
						soundThread = new SoundPlayingThread(playList.getSelectedItem(), progress);
						soundThread.addEndedListener(new Runnable() {
							@Override
							public void run() {
								if (soundThread != null) {
									soundThread.close();
									soundThread = null;
									isPlaying = false;
									pause.setEnabled(isPlaying);
									stop.setEnabled(false);
									play.setEnabled(!isPlaying);
								}
							}
						});
						try {
							soundThread.play();
						} catch (InterruptedException e) {}
					} else if (soundThread != null) {
						try {
							soundThread.play();
						} catch (InterruptedException e) {}
					}
					isPlaying = true;
					pause.setEnabled(isPlaying);
					stop.setEnabled(true);
					play.setEnabled(!isPlaying);
				}
			}
		});
		
		pause.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				if (soundThread != null) {
					soundThread.pause();
					isPlaying = false;
					pause.setEnabled(isPlaying);
					stop.setEnabled(true);
					play.setEnabled(!isPlaying);
				}
			}
		});
		
		stop.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				if (soundThread != null) {
					soundThread.close();
					soundThread = null;
					isPlaying = false;
					pause.setEnabled(isPlaying);
					stop.setEnabled(false);
					play.setEnabled(!isPlaying);
				}
			}
		});
		
	}
	
	@Override
	public void load(NBTTagCompound nbt) {
		NBTTagCompound songList = nbt.getCompoundTag("songList");
		//playList.removeAll();
		for (String key : songList.getKeySet()) {
			//playList.addItem(new ListedSong(key, new File(songList.getString(key))));
		}
	}

	@Override
	public void save(NBTTagCompound nbt) {
		NBTTagCompound songList = new NBTTagCompound();
		for (ListedSong s : playList.getItems()) {
			//songList.setString(s.name, s.file.getAbsolutePath());
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
					if(audioFile.getName().endsWith(".ogg") || audioFile.getName().endsWith(".mp3")) {
						audioInputStream = createFromOgg(audioFile);
					}
					else { // wav
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
		
		public void play() throws InterruptedException {
			if (clip != null) {
				if (progress != null) {
					progress.setMax((int) clip.getMicrosecondLength());
					
					if (progressUpdateThread == null) {
						progressUpdateThread = new Thread("Progressbar Update Thread") {
							@Override
							public void run() {
								while (!Thread.interrupted()) {
									progress.setProgress((int) clip.getMicrosecondPosition()); // /1000000
								}
							}
						};
						
						progressUpdateThread.start();
					}
				}
				
				float volume = 0;
				try {
					volume = (float) ReflectionManager.getVolumeMethod.invoke(ReflectionManager.sndManager, SoundCategory.RECORDS);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
				
				if (isAlive()) {
					clip.start();
				} else {
					start();
				}
			} else {
				Minecraft mc = Minecraft.getMinecraft();
				//EntityPlayer player = mc.player;
				//BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
				//mc.world.playSound(playerPos, listedSong.sound, SoundCategory.RECORDS, 1.0F, 1.0F, false);
				mc.getSoundHandler().playSound(listedSong.ps);

				start();
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
			if (progressUpdateThread != null) progressUpdateThread.interrupt();
			this.interrupt();
		}
		
		 AudioInputStream createFromOgg(File fileIn) throws IOException, Exception {
			    AudioInputStream audioInputStream=null;
			    AudioFormat targetFormat=null;
			    try {
			      AudioInputStream in=null;
			      if(fileIn.getName().endsWith(".ogg")) {
			        VorbisAudioFileReader vb=new VorbisAudioFileReader();
			        in=vb.getAudioInputStream(fileIn);
			      }
			      AudioFormat baseFormat=in.getFormat();
			      targetFormat=new AudioFormat(
			              AudioFormat.Encoding.PCM_SIGNED,
			              baseFormat.getSampleRate(),
			              16,
			              baseFormat.getChannels(),
			              baseFormat.getChannels() * 2,
			              baseFormat.getSampleRate(),
			              false);
			      audioInputStream=AudioSystem.getAudioInputStream(targetFormat, in);
			    }
			    catch(UnsupportedAudioFileException ue) { System.out.println("\nUnsupported Audio"); }
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
					Thread.sleep((clip.getMicrosecondLength()-time)/1000);
				} catch (InterruptedException e) {}
				for (Runnable run : listeners) {
					new Thread(run).start();
				}
			} else {
				if (Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(listedSong.ps)) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					time += 1000000;
					progress.setProgress((int) time);
				}
			}
		}
	}
}
