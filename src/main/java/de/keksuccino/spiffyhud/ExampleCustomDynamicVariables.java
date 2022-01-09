package de.keksuccino.spiffyhud;

import de.keksuccino.spiffyhud.api.DynamicValueRegistry;

public class ExampleCustomDynamicVariables {
	
	public static void registerDynamicValues() {
		
		DynamicValueRegistry r = DynamicValueRegistry.getInstance();
		
		//The unique value key. Should be as short as possible, but still unique.
		//It's not possible to register two values with the same key.
		String valueKey1 = "uniqueid";
		
		//This display name is shown in the layout editor to describe the value.
		String valueDisplayName1 = "Some Custom Value";
		
		//The category of this value. Values with the same category string will be in the same category (no shit Sherlock)
		String valueCategory = "My Custom Values";
		
		//Registering the value to the DynamicValueRegistry
		r.registerValue(valueKey1, valueDisplayName1, valueCategory, (v) -> {
			
			//This is the actual content of your value. This is called every render tick to update the value.
			//In this example, the value will always show the system's current millisecond time.
			return "MS" + System.currentTimeMillis();
			
		});

		//Registering another value.
		r.registerValue("anotherid", "Another Cool Value", valueCategory, (v) -> {

			return "BETTER_MS" + System.currentTimeMillis();
			
		});
		
	}

}
