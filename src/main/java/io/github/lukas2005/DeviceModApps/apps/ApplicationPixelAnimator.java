package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Image;
import com.mrcrayfish.device.object.Canvas;
import com.mrcrayfish.device.object.Picture;
import io.github.lukas2005.device_essentials.Utils;
import io.github.lukas2005.device_essentials.ui.UIUtils;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public class ApplicationPixelAnimator extends ApplicationBase {

	Canvas canvas;

	ArrayList<Picture> frames = new ArrayList<>();
	Picture currentFrame;

	Layout layoutAnimationPreview;

	@Override
	public void init() {
		Layout layoutLoadImages = new Layout(50, 100);

		Button loadImage = UIUtils.createCenteredButton(layoutLoadImages, true, false, 10, 5, 0, 50 - 5 / 2);
		loadImage.setText("Load Image");

		loadImage.setClickListener((mouseX, mouseY, mouseButton) -> {
			Dialog.OpenFile openFile = new Dialog.OpenFile(this);
			openFile.setResponseHandler((success, file) -> {
				if (success) {
					frames.add(Picture.fromFile(file));
					return true;
				}
				return false;
			});
		});
		layoutLoadImages.addComponent(loadImage);

		Button next = UIUtils.createCenteredButton(layoutLoadImages, true, false, 10, 5, 0, 60 - 5 / 2);
		next.setText("Next");

		loadImage.setClickListener((mouseX, mouseY, mouseButton) -> setCurrentLayout(layoutAnimationPreview));
		layoutLoadImages.addComponent(next);

		setCurrentLayout(layoutLoadImages);

		final Image[] image = new Image[1];
		layoutAnimationPreview = new Layout() {
			@Override
			protected void handleOnLoad() {
				super.handleOnLoad();
				currentFrame = frames.get(0);

				Image im = new Image(10, 10, 0, 0, currentFrame.getWidth(), currentFrame.getHeight(), Utils.pictureToResourceLocation(currentFrame));
				layoutLoadImages.addComponent(im);
				image[0] = im;
			}
		};

		canvas = new Canvas(5, 5);
		//ColorGrid grid = new ColorGrid(5, 5, 64, canvas, );


		layoutAnimationPreview.addComponent(canvas);
	}

	@Override
	public void load(NBTTagCompound nbtTagCompound) {

	}

	@Override
	public void save(NBTTagCompound nbtTagCompound) {

	}
}
