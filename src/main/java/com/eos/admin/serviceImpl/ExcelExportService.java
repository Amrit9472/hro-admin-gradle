package com.eos.admin.serviceImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExportService {

    public void exportToExcel(List<Object[]> data, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employee Data");

        // Optional: Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "Employee ID", "Full Name", "Qualification", "Aadhaar", "Creation Date", "Current Address",
            "DOB", "Email", "Experience", "Gender", "Marital Status", "Mobile", "Permanent Address",
            "Previous Organisation", "Process Status", "Referral", "Source", "Sub Source", "Work Exp",
            "Languages", "Job Profile", "Initial Status", "HR Status", "Manager Status",
            "Last Interview Assign", "Remarks By HR", "Remarks By Manager", "Profile Screen Remarks",
            "HR Names", "Change Date Times", "Remarks History", "Statuses"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Fill data rows
        int rowNum = 1;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                if (rowData[i] != null) {
                    cell.setCellValue(rowData[i].toString());
                } else {
                    cell.setCellValue("");
                }
            }
        }

        // Resize columns to fit content
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

        System.out.println("Excel exported to " + filePath);
    }
}
