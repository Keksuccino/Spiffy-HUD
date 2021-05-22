package de.keksuccino.fancyhud.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class DynamicValueRegistry {
	
	protected Map<String, DynamicValue> values = new TreeMap<String, DynamicValue>();
	protected List<String> categories = new ArrayList<String>();
	
	private static DynamicValueRegistry instance;
	
	//  registerValue("", "", (value) -> {
	//     return null;
    //  });
	
	public void registerValue(String valueKey, String valueDisplayName, @Nullable String valueCategory, IDynamicValueContent valueContent) {
		values.put(valueKey, new DynamicValue(valueKey, valueDisplayName, valueCategory, valueContent));
		if (!categories.contains(valueCategory)) {
			categories.add(valueCategory);
		}
	}
	
	public Map<String, DynamicValue> getValues() {
		return values;
	}
	
	public List<DynamicValue> getValuesAsList() {
		List<DynamicValue> l = new ArrayList<DynamicValue>();
		l.addAll(values.values());
		return l;
	}
	
	public DynamicValue getValue(String valueKey) {
		return values.get(valueKey);
	}
	
	public List<String> getCategories() {
		return categories;
	}
	
	public List<DynamicValue> getValuesForCategory(String category) {
		List<DynamicValue> l = new ArrayList<DynamicValue>();
		for (DynamicValue v : getValuesAsList()) {
			if (v.valueCategory.equals(category)) {
				l.add(v);
			}
		}
		return l;
	}
	
	public static interface IDynamicValueContent {
		
		public String getContent(DynamicValue value);
		
	}
	
	public static class DynamicValue {
		
		public final String valueKey;
		public final String valueDisplayName;
		public final String valueCategory;
		public final IDynamicValueContent valueContent;
		
		public DynamicValue(String valueKey, String valueDisplayName, String valueCategory, IDynamicValueContent valueContent) {
			this.valueKey = valueKey;
			this.valueDisplayName = valueDisplayName;
			this.valueContent = valueContent;
			this.valueCategory = valueCategory;
		}
		
		public String get() {
			return valueContent.getContent(this);
		}
		
		public String getPlaceholder() {
			return "%" + this.valueKey + "%";
		}
		
	}
	
	public static DynamicValueRegistry getInstance() {
		if (instance == null) {
			instance = new DynamicValueRegistry();
		}
		return instance;
	}

}
