package com.lccf.util.excel;

import com.lccf.util.TimeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/*
 * ExcelUtil工具类实现功能: 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
 * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
 * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能: 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
 * 2.列名称可以通过注解配置. 3.导出到哪一列可以通过注解配置. 4.鼠标移动到该列时提示信息可以通过注解配置.
 * 5.用注解设置只能下拉选择不能随意填写功能. 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
 * 本工具类以后可能还会加功能,请关注我的博客: http://blog.csdn.net/lk_blog
 *
 * TODO: 增加JSR303 验证数据准确性 返回Map<key, List<T>>
 */
public class ExcelUtil<T> {
    Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static Workbook create(InputStream in) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
    }

    /**
     * 导入excel we don't command big data import using excel
     * TODO：类型设定改用common-utils
     *
     * @throws IOException
     */
    public Map<Boolean, List> importExcel(String sheetName, InputStream input) throws Exception {
        int maxCol = 0;
        Map<Boolean, List> importMap = new HashMap<>();
        List<T> successList = new ArrayList<T>();
        List<T> failList = new ArrayList<T>();
        Workbook workbook = null;
        try {
            workbook = create(input);
            Sheet sheet = workbook.getSheet(sheetName);
            if (!sheetName.trim().equals("")) {
                sheet = workbook.getSheet(sheetName);// 如果指定sheet名,则取指定sheet中的内容.
            }
            if (sheet == null) {
                sheet = workbook.getSheetAt(0); // 如果传入的sheet名不存在则默认指向第1个sheet.
            }
            int rows = sheet.getPhysicalNumberOfRows();
            ExcelVO anno = clazz.getAnnotation(ExcelVO.class);
            int minRows = anno == null ? 0 : anno.skipLine();
            if (rows > minRows) {// 有数据时才处理
                // Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
                List<Field> allFields = getMappedFiled(clazz, null);

                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();// 定义一个map用于存放列的序号和field.
                for (Field field : allFields) {
                    // 将有注解的field存放到map中.
                    if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                        ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                        int col = getExcelCol(attr.column());// 获得列号
                        maxCol = Math.max(col, maxCol);
                        // System.out.println(col + "====" + field.getName());
                        field.setAccessible(true);// 设置类的私有字段属性可访问.
                        fieldsMap.put(col, field);
                    }
                }
                for (int i = minRows; i < rows; i++) {
                    Row row = sheet.getRow(i);
                    // int cellNum = row.getPhysicalNumberOfCells();
                    // int cellNum = row.getLastCellNum();
                    int cellNum = maxCol;
                    T entity = null;
                    for (int j = 0; j <= cellNum; j++) {
                        Cell cell = row.getCell(j);

                        if (cell == null) {
                            continue;
                        }
                        Field field = fieldsMap.get(j);// 从map中得到对应列的field.
                        if (field == null) {
                            continue;
                        }
                        ExcelVOAttribute voAttrAnn = field.getAnnotation(ExcelVOAttribute.class);
                        String c = parseExcel(cell, voAttrAnn.decimalLength());

                        if (c == null || c.equals("")) {
                            continue;
                        }
                        entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.

                        // 取得类型,并根据对象类型设置值.
                        Class<?> fieldType = field.getType();
                        if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                            field.set(entity, Integer.parseInt(c));
                        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Character.TYPE == fieldType) {
                            if ((c != null) && (c.length() > 0)) {
                                field.set(entity, Character.valueOf(c.charAt(0)));
                            }
                        } else if (Date.class == fieldType) {
                            if ((c != null) && (c.length() > 0)) {
                                field.set(entity, TimeUtil.parse(c));
                            }
                        }

                        ResJson resJson = validField(field, c);
                        // 如果验证不成功则设置错误信息
                        if (!resJson.isSuccess()) {
                            BeanUtils.copyProperty(entity, "errorMessage", resJson.getMessage());
                        }
                    }

                    if (entity != null) {
                        String errorMessage = BeanUtils.getProperty(entity, "errorMessage");

                        if (StringUtils.isEmpty(errorMessage)) {
                            successList.add(entity);

                        } else {
                            failList.add(entity);
                        }
                    }
                }
            }
            importMap.put(true, successList);
            importMap.put(false, failList);
        } finally {
            IOUtils.closeQuietly(workbook);
        }

        return importMap;
    }

    /**
     * 处理cell中特殊的数据类型 特别是日期类型
     *
     * @param cell
     * @return
     */
    private String parseExcel(Cell cell, int decimalLength) {
        String result = new String();
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General") || temp.contains("@")) {
                        String pattern = decidePattern(decimalLength);
                        format.applyPattern(pattern);
                    }
                    result = format.format(value);
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                result = "";
            default:
                result = "";
                break;
        }
        return result == null?null:result.trim();
    }

    private String decidePattern(int decimalLength) {
        StringBuilder sb = new StringBuilder(decimalLength > 0 ? "#." : "#");
        int length = decimalLength;
        while (length-- > 0) {
            sb.append("#");
        }

        return sb.toString();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @param output java输出流
     */
    public boolean exportExcel(List<T> lists[], String sheetNames[], OutputStream output) {
        if (lists.length != sheetNames.length) {
            System.out.println("数组长度不一致");
            return false;
        }

        SXSSFWorkbook workbook = new SXSSFWorkbook(500);// 产生工作薄对象

        for (int ii = 0; ii < lists.length; ii++) {
            List<T> list = lists[ii];
            String sheetName = sheetNames[ii];

            List<Field> fields = getMappedFiled(clazz, null);

            SXSSFSheet sheet = workbook.createSheet();// 产生工作表对象

            workbook.setSheetName(ii, sheetName);

            Row row;
            Cell cell;// 产生单元格
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

            // 判定表头是否需要需要增加额外的提示
            int startNo = 0;

            ExcelVO excelVo = clazz.getAnnotation(ExcelVO.class);
            String[] tips = excelVo.tips();
            if (tips.length > 0) {
                row = sheet.createRow(startNo++);
                for (int i = 0; i < tips.length; i++) {
                    cell = row.createCell(i);// 创建列
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(tips[i]);// 写入列名
                }
            }

            row = sheet.createRow(startNo++);// 产生一行
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                int col = getExcelCol(attr.column());// 获得列号
                cell = row.createCell(col);// 创建列
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置列中写入内容为String类型
                cell.setCellValue(attr.name());// 写入列名

                // 如果设置了提示信息则鼠标放上去提示.
                // if (!attr.prompt().trim().equals("")) {
                // setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);//
                // 这里默认设了2-101列提示.
                // }
                // // 如果设置了combo属性则本列只能选择不能输入
                // if (attr.combo().length > 0) {
                // setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);//
                // 这里默认设了2-101列只能选择不能输入.
                // }
                cell.setCellStyle(style);
            }

            int endNo = list.size();
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = 0; i < endNo; i++) {
                row = sheet.createRow(i + startNo);
                T vo = list.get(i); // 得到导出对象.
                for (int j = 0; j < fields.size(); j++) {
                    Field field = fields.get(j);// 获得field.
                    field.setAccessible(true);// 设置实体类私有属性可访问
                    ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            Object o = field.get(vo);
                            if (o == null) {
                                cell.setCellValue("");
                            } else {
                                if (field.getType() == Date.class) {
                                    cell.setCellValue(TimeUtil.format((Date) o, attr.dateFormat()));
                                } else {
                                    cell.setCellValue(String.valueOf(o));// 如果数据存在就填入,不存在填入空格.
                                }
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            output.flush();
            workbook.write(output);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(output);
        }

    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @param sheetSize 每个sheet中数据的行数,此数值必须小于65536
     * @param output java输出流
     */
    public boolean exportExcel(List<T> list, String sheetName, OutputStream output) {
        List<T>[] lists = new ArrayList[1];
        lists[0] = list;

        String[] sheetNames = new String[1];
        sheetNames[0] = sheetName;

        return exportExcel(lists, sheetNames, output);
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }
    /**
     * 设置单元格上提示
     *
     * @param sheet 要设置的sheet.
     * @param promptTitle 标题
     * @param promptContent 内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    // public static Sheet setHSSFPrompt(SXSSFSheet sheet, String promptTitle,
    // String promptContent, int firstRow, int endRow, int firstCol,
    // int endCol) {
    // // 构造constraint对象
    // DVConstraint constraint = DVConstraint
    // .createCustomFormulaConstraint("DD1");
    // // 四个参数分别是：起始行、终止行、起始列、终止列
    // CellRangeAddressList regions = new CellRangeAddressList(firstRow,
    // endRow, firstCol, endCol);
    // // 数据有效性对象
    // HSSFDataValidation data_validation_list = new HSSFDataValidation(
    // regions, constraint);
    // sheet.addValidationData(data_validation_list);
    //
    // return sheet;
    // }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    // public static Sheet setHSSFValidation(Sheet sheet,
    // String[] textlist, int firstRow, int endRow, int firstCol,
    // int endCol) {
    // // 加载下拉列表内容
    // DVConstraint constraint = DVConstraint
    // .createExplicitListConstraint(textlist);
    // // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
    // CellRangeAddressList regions = new CellRangeAddressList(firstRow,
    // endRow, firstCol, endCol);
    // // 数据有效性对象
    // HSSFDataValidation data_validation_list = new HSSFDataValidation(
    // regions, constraint);
    // sheet.addValidationData(data_validation_list);
    // return sheet;
    // }

    /**
     * 得到实体类所有通过注解映射了数据表的字段
     *
     * @param map
     * @return
     */
    public static List<Field> getMappedFiled(Class clazz, List<Field> fields) {
        if (fields == null) {
            fields = new ArrayList<Field>();
        }

        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                fields.add(field);
            }
        }
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            getMappedFiled(clazz.getSuperclass(), fields);
        }

        return fields;
    }

    // 表头生成
    public static void createTableHeader(HSSFSheet sheet, String[] tableHeader, String sheetname) {
        HSSFRow row = null;
        HSSFHeader header = sheet.getHeader();
        header.setCenter(sheetname);
        row = sheet.createRow(0);
        for (int i = 0; i < tableHeader.length; i++) {
            setCell(row, i, tableHeader[i]);
        }
    }

    // 写入单元格
    @SuppressWarnings("deprecation")
    public static void setCell(HSSFRow row, int index, String value) {
        HSSFCell cell = row.createCell((short) index);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(value);
    }

    // 验证单元格数据

    public static ResJson validField(Field field, String value) {
        boolean b = true;
        String filed = value;
        int length = value.length();
        ExcelValidAttribute excelValidAttribute = field.getAnnotation(ExcelValidAttribute.class);
        if (excelValidAttribute != null) {
            int min = excelValidAttribute.min();
            int max = excelValidAttribute.max();
            boolean required = excelValidAttribute.required();
            String regx = excelValidAttribute.regexp();
            String message = excelValidAttribute.message();
            // 验证最大长度和最小长度
            if (!(length >= min && length <= max)) {
                b = false;
            }
            // 验证是否必填
            if (required && StringUtils.isEmpty(value)) {
                b = false;
            }
            if (!StringUtils.isEmpty(value)) {
                // 验证是否符合正则规则
                Pattern r = Pattern.compile(regx);
                Matcher m = r.matcher(value);
                if (!m.find()) {
                    b = false;
                }
            }
            if (!b) {
                filed = message;
            }
        }
        return new ResJson(b, filed);
    }

    public static class ResJson {
        boolean isSuccess;
        String message;

        public ResJson(boolean isSuccess, String message) {
            this.message = message;
            this.isSuccess = isSuccess;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
