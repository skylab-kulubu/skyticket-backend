package com.skylab.skyticket.core.utilities.excel;

import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ExcelExportHelper {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<String> columns;
    private CellStyle dateCellStyle;
    private Event event;
    private String sheetName;

    public ExcelExportHelper(List<String> columns, Event event) {
        this.columns = columns;
        this.workbook = new XSSFWorkbook();
        this.event = event;
        this.sheetName = WorkbookUtil.createSafeSheetName(this.event.getName());

        CreationHelper createHelper = workbook.getCreationHelper();
        this.dateCellStyle = workbook.createCellStyle();
        this.dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy HH:mm:ss"));
    }

    public byte[] exportDataToExcel() throws IOException {
        createHeaderRow();
        writeAttendeesData();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
        return outputStream.toByteArray();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            CellStyle customDateStyle = workbook.createCellStyle();
            customDateStyle.cloneStyleFrom(style);
            customDateStyle.setDataFormat(dateCellStyle.getDataFormat());
            cell.setCellStyle(customDateStyle);
            return;
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).toString());
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).toString());
        }
        else {
            cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(style);
    }

    private void createHeaderRow() {
        sheet = workbook.createSheet(sheetName);

        Row titleRow = sheet.createRow(0);
        CellStyle titleStyle = workbook.createCellStyle();
        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 20);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        createCell(titleRow, 0, sheetName + " etkinliği katılımcı listesi", titleStyle);
        if (columns != null && !columns.isEmpty()) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.size() - 1));
        } else {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
        }


        if (columns != null && !columns.isEmpty()) {
            Row headerRow = sheet.createRow(1);
            CellStyle headerStyle = workbook.createCellStyle();
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 16);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < columns.size(); i++) {
                createCell(headerRow, i, columns.get(i), headerStyle);
            }
        }
    }

    private void writeAttendeesData() {
        if (event.getParticipants() == null || columns == null || columns.isEmpty()) {
            System.err.println("Katılımcı listesi veya sütun tanımları boş/null. Veri yazılamıyor.");
            return;
        }

        int rowCount = 2;

        CellStyle dataStyle = workbook.createCellStyle();
        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);
        dataStyle.setFont(dataFont);

        for (User attendee : event.getParticipants()) {
            if (attendee == null) {
                System.err.println("Null bir katılımcı nesnesiyle karşılaşıldı, atlanıyor.");
                continue;
            }
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (String columnName : columns) {
                Object value = null;
                try {
                    Field field = attendee.getClass().getDeclaredField(columnName);
                    field.setAccessible(true);
                    value = field.get(attendee);
                } catch (NoSuchFieldException e) {
                    System.err.println("Hata: " + columnName + " alanı User sınıfında bulunamadı. Hücre 'ALAN YOK' olarak ayarlandı.");
                    value = "ALAN YOK";
                } catch (IllegalAccessException e) {
                    System.err.println("Hata: " + columnName + " alanına User sınıfında erişilemedi. Hücre 'ERİŞİM YOK' olarak ayarlandı.");
                    value = "ERİŞİM YOK";
                } catch (Exception e) {
                    System.err.println("Hata: " + columnName + " alanı alınırken bilinmeyen bir hata oluştu: " + e.getMessage());
                    value = "HATA";
                }

                if (value instanceof Date || value instanceof LocalDate || value instanceof LocalDateTime) {
                    CellStyle fieldSpecificStyle = workbook.createCellStyle();
                    fieldSpecificStyle.cloneStyleFrom(dateCellStyle);
                    createCell(row, columnCount++, value, fieldSpecificStyle);
                } else {
                    createCell(row, columnCount++, value, dataStyle);
                }
            }
        }
    }
}