package phantom.mybatis.generator;

import lombok.experimental.UtilityClass;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * 生成器工具类
 * @author Frodez
 */
@UtilityClass
class Util {

	/**
	 * 添加依赖
	 * @author Frodez
	 */
	public void addImport(CompilationUnit unit, Class<?> importClass) {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(importClass.getCanonicalName());
		boolean exist = unit.getImportedTypes().contains(type);
		if (!exist) {
			unit.addImportedType(type);
		}
	}

	/**
	 * 添加依赖
	 * @author Frodez
	 */
	public void addImport(CompilationUnit unit, String typeName) {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(typeName);
		boolean exist = unit.getImportedTypes().contains(type);
		if (!exist) {
			unit.addImportedType(type);
		}
	}

	/**
	 * 添加依赖
	 * @author Frodez
	 */
	public void addImport(CompilationUnit unit, FullyQualifiedJavaType type) {
		boolean exist = unit.getImportedTypes().contains(type);
		if (!exist) {
			unit.addImportedType(type);
		}
	}

}
