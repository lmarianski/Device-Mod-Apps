/*package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Dialog.OpenFile;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.*;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.api.utils.BankUtil;
import com.mrcrayfish.device.api.utils.RenderUtil;
import com.mrcrayfish.device.object.Picture;
import io.github.lukas2005.DeviceModApps.objects.CheeseDelivery;
import io.github.lukas2005.DeviceModApps.objects.CheeseDelivery.CheeseType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ApplicationCheeseDesigner extends ApplicationBase {

	private static final ResourceLocation villagerTextures = new ResourceLocation("textures/entity/villager/villager.png");

    private static final ModelVillager villagerModel = new ModelVillager(0.0F);
	
    Picture pictureFromFile;
    BufferedImage bi;
    
	CheeseType sType = CheeseType.GOUDA;
    
	@Override
	public void init() {
		// Startup dialog
		
		Layout layoutStart = new Layout();
		Layout layoutOptions = new Layout();
		
		setCurrentLayout(layoutStart);
		
		layoutStart.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
		{
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(x + 25, y + 33, 15);
                GlStateManager.scale((float) -2.5, (float) -2.5, (float) -2.5);
                GlStateManager.rotate(-10F, 1, 0, 0);
                GlStateManager.rotate(180F, 0, 0, 1);
                GlStateManager.rotate(-20F, 0, 1, 0);
                float scaleX = (mouseX - x - 25) / (float) width;
                float scaleY = (mouseY - y - 20) / (float) height;
                mc.getTextureManager().bindTexture(villagerTextures);
                villagerModel.render(null, 0F, 0F, 0F, -70F * scaleX + 20F, 30F * scaleY, 1F);
                GlStateManager.disableDepth();
            }
            GlStateManager.popMatrix();

            mc.getTextureManager().bindTexture(new ResourceLocation("cdm:textures/gui/bank.png"));
            RenderUtil.drawRectWithTexture(x + 46, y + 19, 0, 0, 146, 52, 146, 52);
        });
		
		Label labelTeller = new Label(TextFormatting.YELLOW + "Casey The Teller", 60, 7);
		layoutStart.addComponent(labelTeller);
		
		Text textWelcome = new Text(TextFormatting.BLACK + "Hello " + Minecraft.getMinecraft().player.getName() + ", welcome to Cheese Design App by CrayFineFoods!, start clicking the button below!", 62, 25, 125);
		layoutStart.addComponent(textWelcome);		
		
		Button btnStart = new Button(133, 74, 58, 20, "Start!");
		btnStart.setToolTip("Start!", "Submit your 32x image to start!");
		
		ApplicationCheeseDesigner app = this;
		btnStart.setClickListener((c, button) -> {

            OpenFile open = new OpenFile(app);

            System.out.println("click!");

            open.setResponseHandler((success, file) -> {

                if(success && file != null) {

                    pictureFromFile = Picture.fromFile(file);
                    if (pictureFromFile != null && pictureFromFile.size.width == 32) {
                        bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = bi.createGraphics();

                        for (int x = 0; x < pictureFromFile.pixels.length; x++) {
                            for (int y = 0; y < pictureFromFile.pixels[x].length; y++) {
                                int colorInt = pictureFromFile.pixels[x][y];
                                Color color = new Color(colorInt);
                                g2d.setColor(color);
                                g2d.drawRect(x, y, 1, 1);
                            }
                        }

                        app.setCurrentLayout(layoutOptions);

                        return true;
                    }

                }

                return true;

            });

            openDialog(open);
        });
		layoutStart.addComponent(btnStart);
		
		// Options for your cheese :P
		
		RadioGroup group = new RadioGroup();
		for (CheeseType type : CheeseType.values()) {
			CheckBox cheeseCheckBox = new CheckBox(type.toString(), 5, 5+(type.ordinal()*15));
			cheeseCheckBox.setRadioGroup(group);
			cheeseCheckBox.setClickListener((c, mouseButton) -> sType = type);
			layoutOptions.addComponent(cheeseCheckBox);
		}
		
		Text costText = new Text("$5", 133, 74, 58);
		layoutOptions.addComponent(costText);
		
		Button btnFinish = new Button(133, 74, 58, 20, "Finish!");
		btnStart.setToolTip("Finish!", "Finish and await delivery!");
		btnStart.setClickListener((c, button) -> BankUtil.remove(5, (nbt, success) -> {
            if (success) {


                new CheeseDelivery(Minecraft.getMinecraft().player.getName(), bi, sType);
            }
        }));
		layoutOptions.addComponent(btnFinish);
		
	}
	
	@Override
	public void load(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub

	}

}
*/