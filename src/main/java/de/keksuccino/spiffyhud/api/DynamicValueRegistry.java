package de.keksuccino.spiffyhud.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Deprecated
public class DynamicValueRegistry {
	
	protected Map<String, DynamicValue> values = new TreeMap<>();
	protected List<String> categories = new ArrayList<>();
	
	private static DynamicValueRegistry instance;

	@Deprecated
	public void registerValue(String valueKey, String valueDisplayName, String valueCategory, IDynamicValueContent valueContent) {
		values.put(valueKey, new DynamicValue(valueKey, valueDisplayName, valueCategory, valueContent));
		if (!categories.contains(valueCategory)) {
			categories.add(valueCategory);
		}
	}

	@Deprecated
	public Map<String, DynamicValue> getValues() {
		return values;
	}

	@Deprecated
	public List<DynamicValue> getValuesAsList() {
		List<DynamicValue> l = new ArrayList<>();
		l.addAll(values.values());
		return l;
	}

	@Deprecated
	public DynamicValue getValue(String valueKey) {
		return values.get(valueKey);
	}

	@Deprecated
	public List<String> getCategories() {
		return categories;
	}

	@Deprecated
	public List<DynamicValue> getValuesForCategory(String category) {
		List<DynamicValue> l = new ArrayList<>();
		for (DynamicValue v : getValuesAsList()) {
			if (v.valueCategory.equals(category)) {
				l.add(v);
			}
		}
		return l;
	}

	@Deprecated
	public static interface IDynamicValueContent {
		
		public String getContent(DynamicValue value);
		
	}

	@Deprecated
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

	@Deprecated
	public static DynamicValueRegistry getInstance() {
		if (instance == null) {
			instance = new DynamicValueRegistry();
		}
		return instance;
	}

}
