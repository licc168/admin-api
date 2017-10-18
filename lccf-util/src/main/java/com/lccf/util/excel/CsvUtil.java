package com.lccf.util.excel;

import com.lccf.util.Reflections;
import com.lccf.util.TimeUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;



public class CsvUtil {

	/**
     * 对list数据源将其里面的数据导入到excel表单
     *
     *            源POJO
     * @param output
     *            java输出流
     */
    public static boolean exportCsv(List<?> list, OutputStream output, String encoding) {
        Class clazz = Reflections.getUserClass(list.get(0));
        List<Field> fieldList = ExcelUtil.getMappedFiled(clazz, null);
        Field[] fields = new Field[fieldList.size()];
        for(Field f : fieldList) {
        	ExcelVOAttribute attr = f
                    .getAnnotation(ExcelVOAttribute.class);
        	int col = ExcelUtil.getExcelCol(attr.column());
        	fields[col] = f;
        }

        //写入表头
        ExcelVO excelVo = (ExcelVO) clazz.getAnnotation(ExcelVO.class);
        String[] tips = excelVo.tips();
        StringBuilder sb = new StringBuilder();
        if(tips.length > 0) {
	        String tip = StringUtils.join(tips, ",");
	        sb.append(tip).append("\r\n");
        }

        //append header
        for(Field f : fields) {
        	ExcelVOAttribute attr = f.getAnnotation(ExcelVOAttribute.class);
        	sb.append(attr.name()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append("\r\n");

        //写入所有行
        for(Object o : list) {
        	for(Field f : fields) {
            	ExcelVOAttribute attr = f.getAnnotation(ExcelVOAttribute.class);
            	String appendValue = "";

            	if(attr.isExport()) {
            		//这里只判定了Date 类型  数值类型的 未作判定
            		Object value = Reflections.getFieldValue(o, f.getName());
            		if(f.getType() == Date.class) {
            			appendValue = TimeUtil.format((Date)value, attr.dateFormat());
                	} else {
                		appendValue = value == null ? "" : String.valueOf(value);// 如果数据存在就填入,不存在填入空格.
                    }
            	}
            	sb.append(appendValue).append(",");
            }
        	sb.deleteCharAt(sb.length() - 1).append("\r\n");
        }

        try {
        	output.write(sb.toString().getBytes(encoding));
            output.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
        	IOUtils.closeQuietly(output);
        }

    }
}
