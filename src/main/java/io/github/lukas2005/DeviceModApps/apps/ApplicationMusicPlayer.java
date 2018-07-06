package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.api.app.component.ProgressBar;
import com.mrcrayfish.device.core.Laptop;
import io.github.lukas2005.DeviceModApps.utils.sound.Sound;
import io.github.lukas2005.DeviceModApps.utils.sound.SoundPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ApplicationMusicPlayer extends ApplicationBase {

	public static ArrayList<Sound> defaultRecords = new ArrayList<>();

	boolean isPlaying = false;

	public SoundPlayer soundThread;
	Thread progressBarUpdateThread;

	ItemList<Sound> playList;

	@Override
	public void init(NBTTagCompound nbt) {
		Layout main = new Layout();
		setCurrentLayout(main);

		if (playList == null) playList = new ItemList<>(5, 5, 75, 6);

		for (Sound e : defaultRecords) {
			if (!playList.getItems().contains(e)) {
				playList.addItem(e);
				markDirty();
			}
		}

		//try {
			//playList.addItem(new Sound("Sample Audio from URL", new URL("https://www.sample-videos.com/audio/mp3/wave.mp3")));
			//playList.addItem(new Sound("WD2 Song", new File("D:\\Im a Watch Dog (Watch Dogs 2 Song).mp3")));
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}

		main.addComponent(playList);

		Button add = new Button(85, 80, 10, 10, Icons.PLUS);
		main.addComponent(add);

		Button remove = new Button(85, 65, 10, 10, Icons.TRASH);
		main.addComponent(remove);

		Button play = new Button(100, 10, 20, 20, Icons.PLAY);
		play.setEnabled(false);
		main.addComponent(play);

		Button pause = new Button(130, 10, 20, 20, Icons.PAUSE);
		pause.setEnabled(false);
		main.addComponent(pause);

		Button stop = new Button(160, 10, 20, 20, Icons.STOP);
		stop.setEnabled(false);
		main.addComponent(stop);

		ProgressBar progress = new ProgressBar(100, 50, 80, 10);
		main.addComponent(progress);

		add.setClickListener((mouseX, mouseY, mouseButton) -> openDialog(new PickSongSource(this)));

		remove.setClickListener((mouseX, mouseY, mouseButton) -> playList.removeItem(playList.getSelectedIndex()));

		play.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (playList.getSelectedItem() != null) {
				if (soundThread == null) {

					soundThread = new SoundPlayer(playList.getSelectedItem());
					soundThread.addEndListener(() -> {
						soundThread = null;
						isPlaying = false;

						pause.setEnabled(false);
						stop.setEnabled(false);
						play.setEnabled(true);

						progress.setProgress(0);

						progressBarUpdateThread.interrupt();
					});
					soundThread.addStartListener(() -> {
						progressBarUpdateThread = new Thread(() -> {
							progress.setMax((int) soundThread.getLength());
							while (!Thread.currentThread().isInterrupted()) {
								if (soundThread != null) progress.setProgress((int) soundThread.getTime());
							}
						});
						progressBarUpdateThread.start();
					});

					soundThread.play();
				} else {
					soundThread.play();
				}
				isPlaying = true;
				pause.setEnabled(true);
				stop.setEnabled(true);
				play.setEnabled(false);
			}
		});

		pause.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (soundThread != null) {
				soundThread.pause();

				isPlaying = false;
				pause.setEnabled(false);
				stop.setEnabled(true);
				play.setEnabled(true);
			}
		});

		stop.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (soundThread != null) {
				soundThread.close();

				isPlaying = false;
				pause.setEnabled(false);
				stop.setEnabled(false);
				play.setEnabled(true);
			}
		});

		playList.setItemClickListener((listedSong, index, mouseButton) -> play.setEnabled(!isPlaying));

	}


	@Override
	public void load(NBTTagCompound nbt) {
		NBTTagCompound songList = nbt.getCompoundTag("songList");

		playList = new ItemList<>(5, 5, 75, 6);

		for (String key : songList.getKeySet()) {
			try {
				playList.addItem(new Sound(key, songList.getString(key)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void save(NBTTagCompound nbt) {
		NBTTagCompound songList = new NBTTagCompound();
		for (Sound s : playList.getItems()) {
			if (!defaultRecords.contains(s)) {
				songList.setString(s.name, s.dataString);
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

	public static void registerDefaultSong(Sound listedSong) {
		defaultRecords.add(listedSong);
	}

	public static class PickSongSource extends Dialog {

		ApplicationMusicPlayer musicPlayer;

		public PickSongSource(ApplicationMusicPlayer musicPlayer) {
			this.musicPlayer = musicPlayer;
		}

		Thread fileChooserThread;

		@Override
		public void init(@Nullable NBTTagCompound intent) {
			super.init(intent);
			Layout main = new Layout(150, 50);
			setLayout(main);

			String labelMsg = "Pick audio source:";
			int labelWidth = Laptop.fontRenderer.getStringWidth(labelMsg);
			Label label = new Label(labelMsg, main.width / 2 - labelWidth/2, 10);
			main.addComponent(label);

			int buttonWidth = 40;

			Button url = new Button(10, main.height/2, buttonWidth, 15, "From url");
			url.setToolTip("Type in a url", "");
			main.addComponent(url);

			Button file = new Button(buttonWidth + 15, main.height/2, buttonWidth, 15, "From file");
			url.setToolTip("Pick a file from your laptop", "");
			file.setEnabled(false);
			main.addComponent(file);

			Button fileFromPc = new Button(buttonWidth * 2 + 20, main.height/2, buttonWidth, 15, "From PC");
			fileFromPc.setToolTip("Pick a file from your pc (outside mc!)", "");
			main.addComponent(fileFromPc);

			url.setClickListener((mouseX, mouseY, mouseButton) -> {
				Dialog.Input input = new Dialog.Input();
				openDialog(input);
				input.setResponseHandler((success, s) -> {
					if (success) {
						try {
							URL url1 = new URL(s);

							AudioSystem.getAudioFileFormat(url1);

							String[] urlSplit = s.split("/");
							musicPlayer.playList.addItem(new Sound(urlSplit[urlSplit.length-1], url1));
						} catch (MalformedURLException e) {
							openDialog(new Dialog.Message("Error: Malformed URL!"));
						} catch (IOException e) {
							e.printStackTrace();
							openDialog(new Dialog.Message("Error: "+e.getMessage()));
						} catch (UnsupportedAudioFileException e) {
							openDialog(new Dialog.Message("Error: URL doesn't point at a valid audio file or the format is unsupported!"));
						}
					}
					this.close();
					return true;
				});
			});

			fileFromPc.setClickListener((mouseX, mouseY, mouseButton) -> {
				JFileChooser fc = new JFileChooser();

				fc.setMultiSelectionEnabled(true);
				fc.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						// For some reason compressed file formats get picked up by AudioSystem.getAudioFileFormat as .mp3 files
						if (f.isFile()) {
							if (f.toString().endsWith(".zip") || f.toString().endsWith(".jar") || f.toString().endsWith(".7z") || f.toString().endsWith(".rar")) return false;
							try {
								AudioSystem.getAudioFileFormat(f);
								return true;
							} catch (UnsupportedAudioFileException | FileNotFoundException e) {
								return false;
							} catch (IOException e) {
								e.printStackTrace();
								return false;
							}
						} else {
							return true;
						}
					}

					@Override
					public String getDescription() {
						return "Audio Files";
					}
				});

				if (fileChooserThread != null && fileChooserThread.isAlive()) fileChooserThread.interrupt();
				fileChooserThread = new Thread(() -> {
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						for (File chosenFile : fc.getSelectedFiles()) {

							String[] pSplit = chosenFile.getAbsolutePath().split(File.separator.equals("\\") ? "\\\\" : File.separator);
							try {
								musicPlayer.playList.addItem(new Sound(pSplit[pSplit.length - 1], chosenFile));
								this.close();
							} catch (IOException e) {
								openDialog(new Dialog.Message("Error: Something went wrong!"));
							}
						}
					}
				}, "File Chooser Thread");
				fileChooserThread.start();
			});
		}
	}
}
