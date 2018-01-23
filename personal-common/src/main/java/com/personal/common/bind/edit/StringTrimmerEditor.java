package com.personal.common.bind.edit;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class StringTrimmerEditor extends PropertyEditorSupport {

	private final String charsToDelete;

	private final boolean emptyAsNull;

	/**
	 * Create a new StringTrimmerEditor.
	 * @param emptyAsNull {@code true} if an empty String is to be transformed into {@code null}
	 */
	public StringTrimmerEditor(boolean emptyAsNull) {
		this.charsToDelete = null;
		this.emptyAsNull = emptyAsNull;
	}

	/**
	 * Create a new StringTrimmerEditor.
	 * @param charsToDelete a set of characters to delete, in addition to trimming an input String. Useful for deleting unwanted line breaks: e.g. "\r\n\f" will delete all new
	 *            lines and line feeds in a String.
	 * @param emptyAsNull {@code true} if an empty String is to be transformed into {@code null}
	 */
	public StringTrimmerEditor(String charsToDelete, boolean emptyAsNull) {
		this.charsToDelete = charsToDelete;
		this.emptyAsNull = emptyAsNull;
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String value = text.trim();
			if (this.charsToDelete != null) {
				value = StringUtils.deleteAny(value, this.charsToDelete);
			}
			if ((this.emptyAsNull && "".equals(value)) || "null".equals(value)) {
				setValue(null);
			} else {
				setValue(value);
			}
		}
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}
}
