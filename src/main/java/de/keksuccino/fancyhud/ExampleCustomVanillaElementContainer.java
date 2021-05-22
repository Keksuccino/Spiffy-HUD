package de.keksuccino.fancyhud;

import java.util.HashMap;
import java.util.Map;

import de.keksuccino.fancyhud.api.hud.CustomVanillaCustomizationItem;
import de.keksuccino.fancyhud.api.hud.CustomVanillaLayoutElement;
import de.keksuccino.fancyhud.api.hud.HudElementContainer;
import de.keksuccino.fancyhud.customization.helper.ui.content.FHContextMenu;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ExampleCustomVanillaElementContainer extends HudElementContainer {

	//A property cache variable to change the color of the element.
	//Don't confuse this with the isRedColor variable of our IngameHudElement.
	//This is the cached value. The actual value of the element is set to this cached value later.
	public boolean isRedColor = false;
	
	public ExampleCustomVanillaElementContainer() {
		super("fancyhud.test.cusvanillaelement", new ExampleCustomVanillaElement(), "Test Element", 50, 50, "bottom-left", 10, -10);
	}

	@Override
	public void onResetElement() {
		
		//Resetting the cached custom property value (Don't reset values of the actual element here!)
		this.isRedColor = false;
		
	}
	
	@Override
	public void onUpdateElement(PropertiesSection properties) {
		
		//Applying customizations made to our cached isRedColor property variable here.
		//Don't reset the value here! Only update it if the input value is not the default value!
		String redColor = properties.getEntryValue("redcolor");
		if ((redColor != null) && redColor.equalsIgnoreCase("true")) {
			this.isRedColor = true;
		}
		
	}

	@Override
	public void onInitEditor(CustomVanillaLayoutElement layoutElement, FHContextMenu rightclickContextMenu) {
		
		//Will add a separator at this position (small line between entries)
		rightclickContextMenu.addSeparator();
		
		//Adding a simple boolean switch button to the element's context menu to change the isRedColor property cache variable
		String buttonLabelString = "Red Color: §aOn";
		if (!this.isRedColor) {
			buttonLabelString = "Red Color: §cOff";
		}
		AdvancedButton contextEntryButton = new AdvancedButton(0, 0, 0, 0, buttonLabelString, (press) -> {
			if (this.isRedColor) {
				this.isRedColor = false;
				((AdvancedButton)press).setMessage("Red Color: §cOff");
			} else {
				this.isRedColor = true;
				((AdvancedButton)press).setMessage("Red Color: §aOn");
			}
		});
		contextEntryButton.setDescription("This is a button tooltip.");
		rightclickContextMenu.addContent(contextEntryButton);
		
	}
	
	@Override
	public void onTick(CustomVanillaCustomizationItem item) {
		
		//Don't override this completely! The parent method contains important things to handle the position of the element.
		super.onTick(item);
		
		//Applying the cached isRedColor property variable to the actual element
		((ExampleCustomVanillaElement)this.element).isRedColor = this.isRedColor;
		
	}

	@Override
	public Map<String, String> getProperties() {
		
		Map<String, String> m = new HashMap<String, String>();
		
		//It's really important to only save customized variables (variables that don't have the default value),
		//otherwise they could override real customizations
		if (this.isRedColor) {
			m.put("redcolor", "true");
		}
		
		return m;
		
	}

}
