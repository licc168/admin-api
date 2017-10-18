package com.lccf.util.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface ExcelVOAttribute {

    /**
     * 导出到Excel中的名字.
     */
     String name();

    /**
     * 配置列的名称,对应A,B,C,D....
     */
     String column();

    /**
     * 提示信息
     */
     String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容.
     */
     String[] combo() default {};

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写.
     */
     boolean isExport() default true;

    /**
     * 日期导出格式
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 数值转String时所保留小树点数
     */
    int decimalLength() default 0;
}
