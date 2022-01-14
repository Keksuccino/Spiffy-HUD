package de.keksuccino.spiffyhud;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.item.CustomizationItem;
import de.keksuccino.spiffyhud.api.item.CustomizationItemContainer;
import de.keksuccino.spiffyhud.api.item.CustomizationItemLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.ui.content.FHContextMenu;
import de.keksuccino.spiffyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ExampleCustomizationItem extends CustomizationItem {

	public boolean isRedColor = false;
	public String label = null;
	
	public ExampleCustomizationItem(PropertiesSection props, CustomizationItemContainer container) {
		super(props, container);
		
		//Check for the "redcolor" property to set the background color
		String redC = props.getEntryValue("redcolor");
		if ((redC != null) && redC.equalsIgnoreCase("true")) {
			this.isRedColor = true;
		}
		
		//Check for the "label" property to set our label string
		this.label = props.getEntryValue("label");
		
	}

	@Override
	public void onInitEditor(CustomizationItemLayoutElement layoutElement, FHContextMenu rightclickContextMenu) {
		
		//Will add a separator at this position in the context menu (small line between entries)
		rightclickContextMenu.addSeparator();
		
		//Adding a simple boolean switch button to the item's context menu to change the isRedColor variable
		String buttonLabelString = "Red Color: §aOn";
		if (!this.isRedColor) {
			buttonLabelString = "Red Color: §cOff";
		}
		AdvancedButton toggleColorButton = new AdvancedButton(0, 0, 0, 0, buttonLabelString, (press) -> {
			if (this.isRedColor) {
				this.isRedColor = false;
				((AdvancedButton)press).setMessage("Red Color: §cOff");
			} else {
				this.isRedColor = true;
				((AdvancedButton)press).setMessage("Red Color: §aOn");
			}
		});
		toggleColorButton.setDescription("This is a button tooltip.");
		rightclickContextMenu.addContent(toggleColorButton);
		
		//Adding a button to change the value of the label variable.
		//The button will open a popup to input the label.
		AdvancedButton setLabelButton = new AdvancedButton(0, 0, 0, 0, "Set Label", (press) -> {
			FHTextInputPopup p = new FHTextInputPopup(new Color(0, 0, 0, 0), "Set Label", null, 240, (callback) -> {
				if (callback != null) {
					this.label = callback;
				}
			});
			if (this.label != null) {
				p.setText(this.label);
			}
			PopupHandler.displayPopup(p);
		});
		setLabelButton.setDescription("This is another button tooltip.");
		rightclickContextMenu.addContent(setLabelButton);
		
	}

	@Override
	public Map<String, String> getProperties() {
		
		Map<String, String> m = new HashMap<String, String>();
		
		//Save the isRedColor variable to the "redcolor" property
		if (this.isRedColor) {
			m.put("redcolor", "true");
		}
		
		//Save the "label" variable to the "label" property
		if (this.label != null) {
			m.put("label", this.label);
		}
		
		return m;
		
	}

	//Here the item gets rendered to the HUD
	@Override
	public void render(MatrixStack matrix) {
		
		RenderSystem.enableBlend();
		
		//Setting the correct render color depending on the custom property variable
		Color c = new Color(61, 235, 52, 100);
		if (this.isRedColor) {
			c = new Color(255, 0, 34, 100);
		}
		
		//Rendering the item (simple colored rectangle)
		fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, c.getRGB());
		
		//Rendering the label string if it's not null
		if (this.label != null) {
			drawStringWithShadow(matrix, MinecraftClient.getInstance().textRenderer, this.label, this.getPosX() + 5, this.getPosY() + 5, Color.WHITE.getRGB());
		}
		
		RenderSystem.disableBlend();
		
	}

}
