package com.lccf.util.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.TYPE })
public @interface ExcelVO {

	int skipLine() default 1;	// 默认跳过表头
	String[] tips() default {};	//导出excel时 添加表头顶部的说明
}
