package de.keksuccino.fancyhud;

import de.keksuccino.fancyhud.api.item.CustomizationItem;
import de.keksuccino.fancyhud.api.item.CustomizationItemContainer;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ExampleCustomizationItemContainer extends CustomizationItemContainer {

	public ExampleCustomizationItemContainer() {
		super("keksuccino.some.example.item", "Example Item");
	}

	@Override
	public CustomizationItem createNew() {
		
		PropertiesSection emptyProps = new PropertiesSection("customization");
		ExampleCustomizationItem i = new ExampleCustomizationItem(emptyProps, this);
		i.width = 60;
		i.height = 60;
		i.label = "Default Label";
		
		return i;
		
	}

	@Override
	public CustomizationItem constructWithProperties(PropertiesSection properties) {
		
		return new ExampleCustomizationItem(properties, this);
		
	}

}
