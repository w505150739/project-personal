
package com.personal.common.bind.edit;

import com.personal.common.util.LogUtil;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Constructor;

/**
 * @author dranson on 2012-11-22
 */
public class SerializableEditor extends PropertyEditorSupport {

	private final LogUtil log = LogUtil.getLogger(SerializableEditor.class);
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || text.length() == 0) {
			setValue(null);
		} else {
			try {
				Constructor constructor = getSource().getClass().getConstructor(String.class);
				setValue(constructor.newInstance(text));
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
	}
}
