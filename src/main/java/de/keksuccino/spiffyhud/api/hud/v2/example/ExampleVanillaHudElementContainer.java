package de.keksuccino.spiffyhud.api.hud.v2.example;

import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.spiffyhud.api.hud.v2.SimpleVanillaCustomizationItem;
import de.keksuccino.spiffyhud.api.hud.v2.VanillaHudElementContainer;
import de.keksuccino.spiffyhud.api.hud.v2.VanillaLayoutEditorElement;
import de.keksuccino.spiffyhud.customization.helper.ui.content.FHContextMenu;

import java.util.HashMap;
import java.util.Map;

public class ExampleVanillaHudElementContainer extends VanillaHudElementContainer {

	//A property cache variable to change the color of the element.
	//Don't confuse this with the isRedColor variable of your IngameHudElement.
	//This is the cached value. The actual value of the element is set to this cached value later.
	public boolean isRedColor = false;
	
	public ExampleVanillaHudElementContainer() {
		//The last 5 parameters are for setting the default size, orientation and position for this element.
		//This is important, because vanilla elements are part of the HUD by default,
		//even if there are not layouts active that could set a position for said vanilla element.
		super("spiffyhud.test.cusvanillaelement", new ExampleCustomVanillaHudElement(), 50, 50, "bottom-left", 10, -10);
	}

	@Override
	public void onResetElement() {
		
		//Resetting the cached custom property value to the default value (Don't reset values of the actual element here! ONLY CACHED VALUES!)
		this.isRedColor = false;
		
	}
	
	@Override
	public void onUpdateElement(PropertiesSection properties) {

		//If you store custom values in the properties, apply them here.
		//ONLY CUSTOM PROPERTIES, not the default properties every element has!

		//The PropertiesSection param holds all properties of the vanilla element, including your custom ones.

		//Applying customizations made to the isRedColor property cache variable here.
		//Remember: You set the value to the cached property here, not the real property of the element.
		//Don't reset the value! Only update it if the input value is not the default value!
		String redColor = properties.getEntryValue("redcolor");
		if ((redColor != null) && redColor.equalsIgnoreCase("true")) {
			this.isRedColor = true;
		}
		
	}

	//Here is where you handle what happens when the elements get loaded into a layout editor instance.
	//When you need custom controls for your element in its right-click context menu, add them here.
	@Override
	public void onInitEditor(VanillaLayoutEditorElement layoutElement, FHContextMenu rightClickContextMenu) {
		
		//Will add a separator at this position (small line between entries)
		rightClickContextMenu.addSeparator();
		
		//Adding a simple boolean switch button to the context menu to change the isRedColor property cache variable
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
		rightClickContextMenu.addContent(contextEntryButton);
		
	}
	
	@Override
	public void onTick(SimpleVanillaCustomizationItem item) {
		
		//Don't override this completely! The parent method contains important things to handle the position of the element.
		super.onTick(item);
		
		//Applying/Syncing the cached isRedColor property variable to the actual element.
		//This will also reset the value if it's not customized by a layout anymore.
		((ExampleCustomVanillaHudElement)this.element).isRedColor = this.isRedColor;
		
	}

	//Here you return all your custom properties you want to store for the vanilla element.
	//These properties will later get added to a PropertiesSection.
	@Override
	public Map<String, String> getProperties() {
		
		Map<String, String> m = new HashMap<String, String>();
		
		//It's REALLY IMPORTANT to only save customized variables (variables that don't have the default value),
		//otherwise they could override real customizations.
		if (this.isRedColor) {
			m.put("redcolor", "true");
		}
		
		return m;
		
	}

	//This is the display name of your element. Used in the editor.
	@Override
	public String getDisplayName() {
		return "Example Vanilla HUD Element";
	}

}
