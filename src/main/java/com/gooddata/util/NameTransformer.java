/**
 * 
 */
package com.gooddata.util;

import java.util.HashSet;
import java.util.Set;

public class NameTransformer {
	private final NameTransformerCallback cb;
	private final Set<String> seen = new HashSet<String>();
	private final String separator;
	
	public NameTransformer(NameTransformerCallback cb) {
		this(cb, " ");
	}
	
	public NameTransformer(NameTransformerCallback cb, String separator) {
		this.cb = cb;
		this.separator = separator;
	}
	
	public String transform(String str) {
		int index = 0;
		String transformed = cb.transform(str);
		while (true) {
			String result = transformed;
			if (index > 0) {
				result += separator + index;
			}
			index++;
			if (!seen.contains(result)) {
				seen.add(result);
				return result;
			}
		}
	}

	public interface NameTransformerCallback {
		public String transform(String str);
	}
}