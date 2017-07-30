package com.mwb.util;


import com.mwb.dao.modle.Bool;
import com.mwb.dao.modle.CrudType;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

public class ExcelUtility {
	private static final String CS_BLANK = "CS_BLANK";
	private static final String CS_LOCKED_TRUE_VERTICAL_CENTER_FONT_GREY_50_PERCENT = "CS_LOCKED_TRUE_VERTICAL_CENTER_FONT_GREY_50_PERCENT";
	private static final String CS_LOCKED_FALSE_VERTICAL_CENTER = "CS_LOCKED_FALSE_VERTICAL_CENTER";
	private static final String CS_FILL_SOLID_RED = "CS_FILL_SOLID_RED";
	private static final String CS_FILL_SOLID_YELLOW = "CS_FILL_SOLID_YELLOW";
	private static final String CS_FILL_SOLID_GREY_25_PERCENT = "CS_FILL_SOLID_GREY_25_PERCENT";
	private static final String CS_DATE_FORMAT_TEXT = "CS_DATE_FORMAT_TEXT";
	private static final String CS_DATE_FORMAT_TEXT_FONT_GREY_50_PERCENT = "CS_DATE_FORMAT_TEXT_FONT_GREY_50_PERCENT";


	private static final Map<HSSFWorkbook, Map<String, CellStyle>> WORKBOOK_CELL_STYLES = new WeakHashMap<HSSFWorkbook, Map<String, CellStyle>>();

	/**
	 * 给List<ExcelCell>排序
	 * @param list
	 */
	public static void sortExcelCellList(List<ExcelCell> list) {
		Collections.sort(list, new Comparator<ExcelCell>() {
			@Override
			public int compare(ExcelCell o1, ExcelCell o2) {
				return o1.getSequence() - o2.getSequence();
			}
		});
	}
	
	/**
	 * 和其他createCell的区别: cellValue类型为<code>Object</code>
	 * @param sheet
	 * @param rowNum
	 * @param cellNum
	 * @param cellValue
	 * @param style
	 */
	public static void createCell(HSSFSheet sheet, int rowNum, int cellNum, Object cellValue, CellStyle style) {
		createCell(sheet, rowNum, cellNum, 1, cellValue, style);
	}
	
	private static boolean isNormalType(Class<?> type) {
		return type.isPrimitive()
				|| String.class.isAssignableFrom(type) 
				|| BigDecimal.class.isAssignableFrom(type)
				|| Integer.class.isAssignableFrom(type)
				|| Long.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type);
	}
	
	public static void createCell(HSSFSheet sheet, int rowNum, int cellNum, int rowSpan, Object cellValue, CellStyle style) {
		if (cellValue != null && cellValue.getClass().isPrimitive()) {
			createCell(sheet, rowNum, cellNum, rowSpan, cellValue.toString(), style);
		}else if (cellValue instanceof String) {
			createCell(sheet, rowNum, cellNum, rowSpan, cellValue.toString(), style);
		} else if (cellValue instanceof BigDecimal) {
			createCell(sheet, rowNum, cellNum, rowSpan, BigDecimal.valueOf(Double.valueOf(cellValue.toString())), style);
		} else if (cellValue instanceof Integer) {
			createCell(sheet, rowNum, cellNum, rowSpan, Integer.parseInt(cellValue.toString()), style);
		} else if (cellValue instanceof Long) {
			createCell(sheet, rowNum, cellNum, rowSpan, cellValue.toString(), style);
		} else if (cellValue instanceof Boolean) {
			createCell(sheet, rowNum, cellNum, rowSpan, ((Boolean)cellValue) ? "是" : "否", style);
		} else if (cellValue instanceof List) {
			@SuppressWarnings("unchecked")
			List<ExcelCell> cellValueList = (List<ExcelCell>)cellValue;
			for (ExcelCell subCell: cellValueList) {
				createCell(sheet, rowNum, cellNum++, subCell.getRowSpan(), subCell.getValue(), style);
			}
		}
    }
	
	public static void main(String[] args) {
		List<ExcelCell> list = new ArrayList<ExcelCell>();
		list.add(new ExcelCell("b",2));
		list.add(new ExcelCell(123.125,1));
		int i = 35;
		list.add(new ExcelCell(i,3));
		
		Collections.sort(list, new Comparator<ExcelCell>() {
			@Override
			public int compare(ExcelCell o1, ExcelCell o2) {
				return o1.getSequence() - o2.getSequence();
			}
		});
		
		for (ExcelCell c : list) {
			if (c.getValue() instanceof String) {
				System.out.println("String: "+c.getValue());
			} else if (c.getValue() instanceof Double) {
				System.out.println("Double: "+c.getValue());
			} else if (c.getValue() instanceof Integer) {
				System.out.println("int: "+c.getValue());
			}
			System.out.println(c.getSequence() + "\n");
		}
		
	}
	
    public static HSSFSheet createSheet(HSSFWorkbook workbook, String sheetName) {
        HSSFSheet sheet = workbook.getSheet(sheetName);
        if (null == sheet) {
            sheet = workbook.createSheet(sheetName);
            // sheet.protectSheet("");
        }
        return sheet;
    }

    public static HSSFRow createRow(HSSFSheet sheet, int rowNum) {
        HSSFRow row = sheet.getRow(rowNum);
        if (null == row) {
            row = sheet.createRow(rowNum);
        }
        return row;
    }

    public static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style) {
        HSSFCell cell = row.getCell(cellNum);
        if (null == cell) {
            cell = row.createCell(cellNum);
        }
        cell.setCellStyle(style);
        return cell;
    }

    public static HSSFSheet getSheet(HSSFWorkbook workbook, String sheetName) {
        HSSFSheet sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    public static HSSFRow getRow(HSSFSheet sheet, int rowNum) {
        if (sheet == null) {
            return null;
        }
        HSSFRow row = sheet.getRow(rowNum);
        return row;
    }

    public static HSSFCell getCell(HSSFRow row, int cellNum) {
        HSSFCell cell = row.getCell(cellNum);
        return cell;
    }

    public static HSSFCell getCell(HSSFWorkbook workbook, String sheetName, int rowNum, int cellNum) {
        HSSFSheet sheet = getSheet(workbook, sheetName);
        HSSFRow row = getRow(sheet, rowNum);
        HSSFCell cell = getCell(row, cellNum);
        return cell;
    }

    public static HSSFCell getCell(HSSFSheet sheet, int rowNum, int cellNum) {
        if (sheet == null) {
            return null;
        }
        HSSFRow row = getRow(sheet, rowNum);
        if (row == null) {
            return null;
        }

        HSSFCell cell = getCell(row, cellNum);
        return cell;
    }

    public static Font createFont(HSSFWorkbook workbook, short boldweight, short color, short size) {
        Font font = workbook.createFont();
        font.setBoldweight(boldweight);
        font.setColor(color);
        font.setFontHeightInPoints(size);
        return font;
    }

    public static CellStyle getCellStyle(HSSFWorkbook workbook, boolean isGrey) {
    	if (isGrey) {
    		return getCellStyle(workbook, CS_LOCKED_TRUE_VERTICAL_CENTER_FONT_GREY_50_PERCENT);
    	} else {
    		return getCellStyle(workbook, CS_LOCKED_FALSE_VERTICAL_CENTER);
    	}
    }
    
    public static CellStyle getCellStyle(HSSFWorkbook workbook, CrudType crudType, Bool available) {
        
        if (CrudType.CREATE.equals(crudType) ) {
        	return getCellStyle(workbook, CS_FILL_SOLID_RED);
        } else if (crudType == null
                        || CrudType.UPDATE.equals(crudType)
                        || CrudType.READ.equals(crudType) ) {
            if (available.getValue()) {
            	return getCellStyle(workbook, CS_FILL_SOLID_YELLOW);
            } else {
            	return getCellStyle(workbook, CS_FILL_SOLID_GREY_25_PERCENT);
            }
        }
        
        return getCellStyle(workbook, CS_BLANK);
    }

    public static CellStyle getCellStyle(HSSFWorkbook workbook, CrudType crudType, int index) {
        
        if (CrudType.CREATE.equals(crudType) ) {
        	return getCellStyle(workbook, CS_FILL_SOLID_RED);
        } else if (crudType == null
                        || CrudType.UPDATE.equals(crudType)
                        || CrudType.READ.equals(crudType) ) {
            if (index % 2 == 0) {
            	return getCellStyle(workbook, CS_FILL_SOLID_YELLOW);
            } else {
            	return getCellStyle(workbook, CS_FILL_SOLID_GREY_25_PERCENT);
            }
        }
        
        return getCellStyle(workbook, CS_BLANK);
    }
    
    public static CellStyle getTextCellStyle(HSSFWorkbook workbook, boolean locked) {
        if (locked) {
        	return getCellStyle(workbook, CS_DATE_FORMAT_TEXT_FONT_GREY_50_PERCENT);
        } else {
        	return getCellStyle(workbook, CS_DATE_FORMAT_TEXT);
        }
    }
    
    private synchronized static CellStyle getCellStyle(HSSFWorkbook workbook, String key) {
    	Map<String, CellStyle> cellStyles = WORKBOOK_CELL_STYLES.get(workbook);
    	
    	if (cellStyles == null) {
    		cellStyles = new HashMap<String, CellStyle>();
    		
    		WORKBOOK_CELL_STYLES.put(workbook, cellStyles);
    	}
    	
    	CellStyle cs = cellStyles.get(key);
    	
    	if (cs == null) {
    		if (CS_BLANK.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			// do nothing
    		} else if (CS_LOCKED_TRUE_VERTICAL_CENTER_FONT_GREY_50_PERCENT.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			cs.setLocked(true);
    			cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

    			Font font = workbook.createFont();
    			font.setColor(HSSFColor.GREY_50_PERCENT.index);
    			cs.setFont(font);    	        
    		} else if (CS_LOCKED_FALSE_VERTICAL_CENTER.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			cs.setLocked(false);
    			cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    		} else if (CS_FILL_SOLID_RED.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
    			cs.setFillForegroundColor(HSSFColor.RED.index);
    		} else if (CS_FILL_SOLID_YELLOW.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
                cs.setFillForegroundColor(HSSFColor.YELLOW.index);
    		} else if (CS_FILL_SOLID_GREY_25_PERCENT.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
                cs.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    		} else if (CS_DATE_FORMAT_TEXT.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			HSSFDataFormat format = workbook.createDataFormat();
    			
    			// http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/BuiltinFormats.html
    			// 0x31, "@" - This is text format.
    	        cs.setDataFormat(format.getFormat("@"));
    		} else if (CS_DATE_FORMAT_TEXT_FONT_GREY_50_PERCENT.equals(key)) {
    			cs = workbook.createCellStyle();
    			
    			HSSFDataFormat format = workbook.createDataFormat();
    	        cs.setDataFormat(format.getFormat("@"));
    	        
    	        Font font = workbook.createFont();
                font.setColor(HSSFColor.GREY_50_PERCENT.index);
                cs.setFont(font);
    		} else {
    			return null;
    		}

    		cellStyles.put(key, cs);
    	}
    	
    	return cs;
    }

    public static void createCell(HSSFSheet sheet, int rowNum, int cellNum, int rowSpan, String cellValue, CellStyle style) {
        if (rowSpan > 1) {
        	CellRangeAddress cra = new CellRangeAddress(rowNum, rowNum + rowSpan - 1, cellNum, cellNum);
        	sheet.addMergedRegion(cra);
        }
        HSSFRow row = createRow(sheet, rowNum);
        HSSFCell cell = createCell(row, cellNum, style);
        cell.setCellValue(cellValue);
    }

    public static void createCell(HSSFSheet sheet, int rowNum, int cellNum, int rowSpan, BigDecimal cellValue, CellStyle style) {
    	if (rowSpan > 1) {
        	CellRangeAddress cra = new CellRangeAddress(rowNum, rowNum + rowSpan - 1, cellNum, cellNum);
        	sheet.addMergedRegion(cra);
        }
        HSSFRow row = createRow(sheet, rowNum);
        HSSFCell cell = createCell(row, cellNum, style);
        cell.setCellValue(cellValue.doubleValue());
    }

    public static void createCell(HSSFSheet sheet, int rowNum, int cellNum, int rowSpan, int cellValue, CellStyle style) {
    	if (rowSpan > 1) {
        	CellRangeAddress cra = new CellRangeAddress(rowNum, rowNum + rowSpan - 1, cellNum, cellNum);
        	sheet.addMergedRegion(cra);
        }
        HSSFRow row = createRow(sheet, rowNum);
        HSSFCell cell = createCell(row, cellNum, style);
        cell.setCellValue(cellValue);
    }

    public static void createBatchColumn(HSSFSheet sheet, int rowNum, int cellNum, String cellValue, CellStyle style) {
        for (int i = rowNum; i < 1000; i++) {
            createCell(sheet, i, cellNum, cellValue, style);
        }
    }
    
    public static void createGenericDropdownColumn(HSSFSheet sheet, int firstRow, int lastRow, int cellNum, String cellValue, String sheetName, CellStyle style) {
    	for(int i = firstRow; i < lastRow; i++){
			createCell(sheet, i, cellNum, cellValue, style);
			addValidationData(sheet, sheetName, i, i, cellNum, cellNum);
		}
    }

    public static void addValidationData(HSSFSheet sheet, String[] list, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidation);
    }
    
    public static void addValidationData(HSSFSheet sheet, String list, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(list);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidation);
    }

    public static String encodeFileName(String fileName, HttpServletRequest request) {
    	String agent = request.getHeader("USER-AGENT");
    	return encodeFileName(fileName, agent);
    }
    
    public static String encodeFileName(String fileName, String agent) {        
        try {
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(fileName, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla"))) {
                // return MimeUtility.encodeText(fileName, "UTF-8", "B");
                return URLEncoder.encode(fileName, "UTF-8");
            }
            return fileName;
        } catch (Exception ex) {
            return fileName;
        }
    }

    public static class ExcelCell {
		private Object value;
		private int sequence;
		private int rowSpan;
		
		public ExcelCell(Object value, int sequence) {
			super();
			this.value = value;
			this.sequence = sequence;
			this.rowSpan = 1;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public int getSequence() {
			return sequence;
		}

		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

		public int getRowSpan() {
			return rowSpan;
		}

		public void setRowSpan(int rowSpan) {
			this.rowSpan = rowSpan;
		}
		
	}
}
