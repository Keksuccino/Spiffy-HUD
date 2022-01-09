package de.keksuccino.spiffyhud.customization.helper.editor.elements.string;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.helper.ui.UIBase;
import de.keksuccino.spiffyhud.customization.items.StringCustomizationItem;
import de.keksuccino.spiffyhud.customization.rendering.RainbowText;
import de.keksuccino.spiffyhud.utils.TextComponentUtils;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.gui.screens.popup.Popup;
import de.keksuccino.konkrete.input.KeyboardData;
import de.keksuccino.konkrete.input.KeyboardHandler;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;

public class SetRainbowStringColorPopup extends Popup {
	
	protected LayoutString element;
	
	protected AdvancedTextField firstStartColorTextField;
	protected AdvancedTextField secondStartColorTextField;
	protected AdvancedTextField firstEndColorTextField;
	protected AdvancedTextField secondEndColorTextField;
	
	protected AdvancedButton doneButton;
	protected AdvancedButton cancelButton;
	
	protected int width = 250;
	protected String title = Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors");
	
	public SetRainbowStringColorPopup(LayoutString element) {
		super(240);
		this.element = element;
		this.init();
		this.setInputs();
	}
	
	protected void init() {
		
		this.firstStartColorTextField = new AdvancedTextField(Minecraft.getInstance().font, 0, 0, 100, 20, true, null);
		this.firstStartColorTextField.setCanLoseFocus(true);
		this.firstStartColorTextField.setFocus(false);
		this.firstStartColorTextField.setMaxLength(15);
		
		this.secondStartColorTextField = new AdvancedTextField(Minecraft.getInstance().font, 0, 0, 100, 20, true, null);
		this.secondStartColorTextField.setCanLoseFocus(true);
		this.secondStartColorTextField.setFocus(false);
		this.secondStartColorTextField.setMaxLength(15);
		
		this.firstEndColorTextField = new AdvancedTextField(Minecraft.getInstance().font, 0, 0, 100, 20, true, null);
		this.firstEndColorTextField.setCanLoseFocus(true);
		this.firstEndColorTextField.setFocus(false);
		this.firstEndColorTextField.setMaxLength(15);
		
		this.secondEndColorTextField = new AdvancedTextField(Minecraft.getInstance().font, 0, 0, 100, 20, true, null);
		this.secondEndColorTextField.setCanLoseFocus(true);
		this.secondEndColorTextField.setFocus(false);
		this.secondEndColorTextField.setMaxLength(15);
		
		this.doneButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.done"), true, (press) -> {
			this.onFinish();
		});
		this.addButton(this.doneButton);
		
		this.cancelButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.yesno.cancel"), true, (press) -> {
			this.onCancel();
		});
		this.addButton(this.cancelButton);
		
		KeyboardHandler.addKeyPressedListener(this::onEnterPressed);
		KeyboardHandler.addKeyPressedListener(this::onEscapePressed);
		
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, Screen renderIn) {
		super.render(matrix, mouseX, mouseY, renderIn);
		
		Font font = Minecraft.getInstance().font;
		
		if (this.isDisplayed()) {
			
			RenderSystem.enableBlend();
			
			drawCenteredString(matrix, font, new TextComponent(title), renderIn.width / 2, (renderIn.height / 2) - 90, Color.WHITE.getRGB());
			
			/** FIRST ROW **/
			this.firstStartColorTextField.setX((renderIn.width / 2) - this.firstStartColorTextField.getWidth() - 10);
			this.firstStartColorTextField.setY((renderIn.height / 2) - 50);
			this.firstStartColorTextField.renderButton(matrix, mouseX, mouseY, Minecraft.getInstance().getFrameTime());
			String firstStartString = Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors.popup.firststartcolor");
			font.drawShadow(matrix, firstStartString, this.firstStartColorTextField.getX(), this.firstStartColorTextField.getY() - 10, -1);
			
			this.firstEndColorTextField.setX((renderIn.width / 2) + 10);
			this.firstEndColorTextField.setY((renderIn.height / 2) - 50);
			this.firstEndColorTextField.renderButton(matrix, mouseX, mouseY, Minecraft.getInstance().getFrameTime());
			String firstEndString = Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors.popup.firstendcolor");
			font.drawShadow(matrix, firstEndString, this.firstEndColorTextField.getX(), this.firstEndColorTextField.getY() - 10, -1);
			
			/** DESCRIPTION **/
			String line1 = "§7"+ Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors.popup.colordesc.line1");
			int line1Width = font.width(line1);
			font.drawShadow(matrix, line1, (renderIn.width / 2) - (line1Width / 2), (renderIn.height / 2) - 17, -1);
			String line2 = "§7" + Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors.popup.colordesc.line2");
			int line2Width = font.width(line2);
			font.drawShadow(matrix, line2, (renderIn.width / 2) - (line2Width / 2), (renderIn.height / 2) - 6, -1);
			String line3 = "§7" + Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors.popup.colordesc.line3");
			int line3Width = font.width(line3);
			font.drawShadow(matrix, line3, (renderIn.width / 2) - (line3Width / 2), (renderIn.height / 2) + 5, -1);
			
			/** SECOND ROW **/
			this.secondStartColorTextField.setX((renderIn.width / 2) - this.secondStartColorTextField.getWidth() - 10);
			this.secondStartColorTextField.setY((renderIn.height / 2) + 36);
			this.secondStartColorTextField.renderButton(matrix, mouseX, mouseY, Minecraft.getInstance().getFrameTime());
			String secondStartString = Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors.popup.secondstartcolor");
			font.drawShadow(matrix, secondStartString, this.secondStartColorTextField.getX(), this.secondStartColorTextField.getY() - 10, -1);
			
			this.secondEndColorTextField.setX((renderIn.width / 2) + 10);
			this.secondEndColorTextField.setY((renderIn.height / 2) + 36);
			this.secondEndColorTextField.renderButton(matrix, mouseX, mouseY, Minecraft.getInstance().getFrameTime());
			String secondEndString = Locals.localize("spiffyhud.helper.editor.elements.string.rainbow.colors.popup.secondendcolor");
			font.drawShadow(matrix, secondEndString, this.secondEndColorTextField.getX(), this.secondEndColorTextField.getY() - 10, -1);
			
			/** THIRD ROW **/
			this.doneButton.setX((renderIn.width / 2) - this.doneButton.getWidth() - 10);
			this.doneButton.setY((renderIn.height / 2) + 80);
			
			this.cancelButton.setX((renderIn.width / 2) + 10);
			this.cancelButton.setY((renderIn.height / 2) + 80);
			
			this.renderButtons(matrix, mouseX, mouseY);
			
		}
	}
	
	public void setText(String text) {
		this.firstStartColorTextField.setValue("");
		this.firstStartColorTextField.insertText(text);
	}
	
	protected void onFinish() {
		
		String firstStart = this.firstStartColorTextField.getValue();
		String firstEnd = this.firstEndColorTextField.getValue();
		String secondStart = this.secondStartColorTextField.getValue();
		String secondEnd = this.secondEndColorTextField.getValue();
		
		if (!firstStart.replace(" ", "").equals("") && !firstEnd.replace(" ", "").equals("") && !secondStart.replace(" ", "").equals("") && !secondEnd.replace(" ", "").equals("")) {
			Color fS = RenderUtils.getColorFromHexString(firstStart);
			Color fE = RenderUtils.getColorFromHexString(firstEnd);
			Color sS = RenderUtils.getColorFromHexString(secondStart);
			Color sE = RenderUtils.getColorFromHexString(secondEnd);
			if ((fS != null) && (fE != null) && (sS != null) && (sE != null)) {
				
				this.element.handler.history.saveSnapshot(this.element.handler.history.createSnapshot());
				
				StringCustomizationItem i = this.element.getObject();
				
				i.rainbowStartColorHex1 = firstStart;
				i.rainbowEndColorHex1 = firstEnd;
				i.rainbowStartColorHex2 = secondStart;
				i.rainbowEndColorHex2 = secondEnd;
				
				i.updateRainbowColors();
				
			} else {
				//TODO error popup
			}
		} else {
			//TODO error popup
		}
		
		this.setDisplayed(false);
	}
	
	protected void onCancel() {
		this.setDisplayed(false);
	}
	
	public void onEnterPressed(KeyboardData d) {
		if ((d.keycode == 257) && this.isDisplayed()) {
			this.onFinish();
		}
	}
	
	public void onEscapePressed(KeyboardData d) {
		if ((d.keycode == 256) && this.isDisplayed()) {
			this.onCancel();
		}
	}
	
	protected void setInputs() {
		String fS = this.element.getObject().rainbowStartColorHex1;
		String fE = this.element.getObject().rainbowEndColorHex1;
		String sS = this.element.getObject().rainbowStartColorHex2;
		String sE = this.element.getObject().rainbowEndColorHex2;
		if ((fS != null) && (fE != null) && (sS != null) && (sE != null)) {
			this.firstStartColorTextField.setValue(fS);
			this.firstEndColorTextField.setValue(fE);
			this.secondStartColorTextField.setValue(sS);
			this.secondEndColorTextField.setValue(sE);
		} else {
			RainbowText rt = this.element.getObject().rainbowText;
			this.firstStartColorTextField.setValue(TextComponentUtils.toHexString(rt.startColor1.getRed(), rt.startColor1.getGreen(), rt.startColor1.getBlue()));
			this.firstEndColorTextField.setValue(TextComponentUtils.toHexString(rt.endColor1.getRed(), rt.endColor1.getGreen(), rt.endColor1.getBlue()));
			this.secondStartColorTextField.setValue(TextComponentUtils.toHexString(rt.startColor2.getRed(), rt.startColor2.getGreen(), rt.startColor2.getBlue()));
			this.secondEndColorTextField.setValue(TextComponentUtils.toHexString(rt.endColor2.getRed(), rt.endColor2.getGreen(), rt.endColor2.getBlue()));
		}
	}
	
	@Override
	protected void colorizePopupButton(AdvancedButton b) {
		UIBase.colorizeButton(b);
	}

}
