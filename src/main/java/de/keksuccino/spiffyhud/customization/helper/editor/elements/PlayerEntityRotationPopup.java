package de.keksuccino.spiffyhud.customization.helper.editor.elements;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.helper.ui.popup.FHPopup;
import de.keksuccino.spiffyhud.customization.items.MirroredPlayerCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.KeyboardData;
import de.keksuccino.konkrete.input.KeyboardHandler;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import java.awt.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class PlayerEntityRotationPopup extends FHPopup {

	private LayoutEditorScreen handler;
	private LayoutMirroredPlayer object;
	
	private AdvancedTextField bodyX;
	private AdvancedTextField bodyY;
	private AdvancedTextField headX;
	private AdvancedTextField headY;
	
	private AdvancedButton doneButton;
	private AdvancedButton applyButton;
	
	public PlayerEntityRotationPopup(LayoutEditorScreen handler, LayoutMirroredPlayer object) {
		super(240);
		this.handler = handler;
		this.object = object;
		
		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		
		this.bodyX = new AdvancedTextField(font, 0, 0, 150, 20, true, CharacterFilter.getDoubleCharacterFiler());
		this.bodyX.setText("" + object.getObject().bodyRotationX);
		
		this.bodyY = new AdvancedTextField(font, 0, 0, 150, 20, true, CharacterFilter.getDoubleCharacterFiler());
		this.bodyY.setText("" + object.getObject().bodyRotationY);
		
		this.headX = new AdvancedTextField(font, 0, 0, 150, 20, true, CharacterFilter.getDoubleCharacterFiler());
		this.headX.setText("" + object.getObject().headRotationX);
		
		this.headY = new AdvancedTextField(font, 0, 0, 150, 20, true, CharacterFilter.getDoubleCharacterFiler());
		this.headY.setText("" + object.getObject().headRotationY);
		
		this.doneButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.done"), true, (call) -> {
			this.updateValues();
			this.setDisplayed(false);
		});
		this.colorizePopupButton(doneButton);
		this.addButton(doneButton);
		
		this.applyButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("spiffyhud.popup.apply"), true, (call) -> {
			this.updateValues();
		});
		this.colorizePopupButton(applyButton);
		this.addButton(applyButton);
		
		KeyboardHandler.addKeyPressedListener(this::onEscapePressed);
		KeyboardHandler.addKeyPressedListener(this::onEnterPressed);
		
	}
	
	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, Screen renderIn) {
		super.render(matrix, mouseX, mouseY, renderIn);
		
		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		int midX = renderIn.width / 2;
		int midY = renderIn.height / 2;
		float partial = MinecraftClient.getInstance().getTickDelta();
		
		drawCenteredText(matrix, font, Locals.localize("spiffyhud.helper.creator.items.mirroredplayer.rotation.custom.bodyx"), midX, midY - 83, Color.WHITE.getRGB());
		
		this.bodyX.x = midX - (this.bodyX.getWidth() / 2);
		this.bodyX.y = midY - 70;
		this.bodyX.render(matrix, mouseX, mouseY, partial);
		
		drawCenteredText(matrix, font, Locals.localize("spiffyhud.helper.creator.items.mirroredplayer.rotation.custom.bodyy"), midX, midY - 43, Color.WHITE.getRGB());
		
		this.bodyY.x = midX - (this.bodyY.getWidth() / 2);
		this.bodyY.y = midY - 30;
		this.bodyY.render(matrix, mouseX, mouseY, partial);

		drawCenteredText(matrix, font, Locals.localize("spiffyhud.helper.creator.items.mirroredplayer.rotation.custom.headx"), midX, midY - 3, Color.WHITE.getRGB());
		
		this.headX.x = midX - (this.headX.getWidth() / 2);
		this.headX.y = midY + 10;
		this.headX.render(matrix, mouseX, mouseY, partial);

		drawCenteredText(matrix, font, Locals.localize("spiffyhud.helper.creator.items.mirroredplayer.rotation.custom.heady"), midX, midY + 37, Color.WHITE.getRGB());
		
		this.headY.x = midX - (this.headY.getWidth() / 2);
		this.headY.y = midY + 50;
		this.headY.render(matrix, mouseX, mouseY, partial);
		
		this.doneButton.x = midX - this.doneButton.getWidth() - 5;
		this.doneButton.y = midY + 80;
		
		this.applyButton.x = midX + 5;
		this.applyButton.y = midY + 80;
		
		this.renderButtons(matrix, mouseX, mouseY);

		MirroredPlayerCustomizationItem i = this.object.getObject();
		String ori = i.orientation;
		int x = i.posX;
		int y = i.posY;
		float scale = i.scale;
		boolean rot = i.autoRotatePlayer;
		i.orientation = "top-left";
		i.scale = 60;
		i.posX = midX - 150;
		i.posY = midY - ((int)(2 * i.scale) / 2);
		i.autoRotatePlayer = false;

		i.render(matrix);

		i.autoRotatePlayer = rot;
		i.orientation = ori;
		i.posX = x;
		i.posY = y;
		i.scale = scale;

	}
	
	protected void updateValues() {
		this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
		
		this.object.getObject().autoRotatePlayer = false;
		
		if ((this.bodyX.getText() != null) && MathUtils.isFloat(this.bodyX.getText())) {
			this.object.getObject().bodyRotationX = Float.parseFloat(this.bodyX.getText());
		} else {
			this.object.getObject().bodyRotationX = 0;
		}
		
		if ((this.bodyY.getText() != null) && MathUtils.isFloat(this.bodyY.getText())) {
			this.object.getObject().bodyRotationY = Float.parseFloat(this.bodyY.getText());
		} else {
			this.object.getObject().bodyRotationY = 0;
		}
		
		if ((this.headX.getText() != null) && MathUtils.isFloat(this.headX.getText())) {
			this.object.getObject().headRotationX = Float.parseFloat(this.headX.getText());
		} else {
			this.object.getObject().headRotationX = 0;
		}
		
		if ((this.headY.getText() != null) && MathUtils.isFloat(this.headY.getText())) {
			this.object.getObject().headRotationY = Float.parseFloat(this.headY.getText());
		} else {
			this.object.getObject().headRotationY = 0;
		}
	}
	
	protected void onEnterPressed(KeyboardData d) {
		if ((d.keycode == 257) && this.isDisplayed()) {
			this.updateValues();
			this.setDisplayed(false);
		}
	}
	
	protected void onEscapePressed(KeyboardData d) {
		if ((d.keycode == 256) && this.isDisplayed()) {
			this.setDisplayed(false);
		}
	}

}
