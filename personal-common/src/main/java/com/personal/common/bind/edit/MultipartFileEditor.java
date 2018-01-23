
package com.personal.common.bind.edit;

import java.beans.PropertyEditorSupport;


public class MultipartFileEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(null);
	}
}
