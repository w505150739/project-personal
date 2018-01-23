package com.personal.common.bind;
import java.util.Date;

import com.personal.common.bind.edit.DateEditor;
import com.personal.common.bind.edit.MultipartFileEditor;
import com.personal.common.bind.edit.NumberEditor;
import com.personal.common.bind.edit.StringTrimmerEditor;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.web.multipart.MultipartFile;

/**
 *@ClassName: DataEditorRegistrar
 * @Description: 全局的类型转换
 * @author xiaopeng
 * @date: 2014 下午4:46:04
 * @version JDK 1.6
 */
public class DataEditorRegistrar implements PropertyEditorRegistrar {

	public void registerCustomEditors(PropertyEditorRegistry registry) {
		registry.registerCustomEditor(Long.class, new NumberEditor(Long.class, true));
		registry.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		registry.registerCustomEditor(Date.class, new DateEditor(Date.class));
		registry.registerCustomEditor(java.sql.Date.class, new DateEditor(java.sql.Date.class));
		registry.registerCustomEditor(java.sql.Time.class, new DateEditor(java.sql.Time.class));
		registry.registerCustomEditor(java.sql.Timestamp.class, new DateEditor(java.sql.Timestamp.class));
		registry.registerCustomEditor(MultipartFile.class, new MultipartFileEditor());
	}
}
