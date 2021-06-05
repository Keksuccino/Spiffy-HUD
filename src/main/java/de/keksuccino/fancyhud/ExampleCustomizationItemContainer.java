package de.keksuccino.fancyhud;

import de.keksuccino.fancyhud.api.item.CustomizationItem;
import de.keksuccino.fancyhud.api.item.CustomizationItemContainer;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ExampleCustomizationItemContainer extends CustomizationItemContainer {

	public ExampleCustomizationItemContainer() {
		//First argument is the unique item identifier and the second one is the display name you see in the editor later.
		super("keksuccino.some.example.item", "Example Item");
	}

	//Called when the button to create a new instance of this item is pressed in the editor.
	//This is a blank/not customized instance.
	@Override
	public CustomizationItem createNew() {
		
		//Here you need to create and return a default instance of your item with every important value set, so it can be rendered.
		PropertiesSection emptyProps = new PropertiesSection("customization");
		ExampleCustomizationItem i = new ExampleCustomizationItem(emptyProps, this);
		i.width = 60;
		i.height = 60;
		i.label = "Default Label";
		
		return i;
		
	}

	//This is used to create the customized instances of items you see in the real/final HUD.
	@Override
	public CustomizationItem constructWithProperties(PropertiesSection properties) {
		
		//Here you're returning a new instance of your item that you created earlier.
		return new ExampleCustomizationItem(properties, this);
		
	}

}
