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
		this(cb, null);
	}
	
	public NameTransformer(NameTransformerCallback cb, Set<String> seen) {
		this(cb, seen, " ");
	}
	
	public NameTransformer(NameTransformerCallback cb, Set<String> seen, String separator) {
		this.cb = cb;
		this.separator = separator;
		if (seen != null) {
			for (final String s : seen) {
				this.seen.add(s.toLowerCase());
			}
		}
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
			String lc = result.toLowerCase();
			if (!seen.contains(lc)) {
				seen.add(lc);
				return result;
			}
		}
	}

	public interface NameTransformerCallback {
		public String transform(String str);
	}
}