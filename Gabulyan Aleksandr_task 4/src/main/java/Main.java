import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            int rowCounter = 0;
            int cellCounter = 0;
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("src/main/resources/file.xlsx"));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(rowCounter);
            XSSFCell cell = row.getCell(cellCounter);

            StringBuilder s = new StringBuilder();
            int i = 0;
            for (char c : cell.toString().toCharArray()) {
                if (c == '_') {
                    i++;
                    continue;
                }
                if (i % 2 == 1) {
                    i++;
                    String str = "" + c;
                    str = str.toUpperCase();
                    s.append(str);
                    continue;
                }

                s.append(c);
            }

            XSSFRow row1 = sheet.createRow(++rowCounter);
            Cell cell1 = row1.createCell(++cellCounter);

            CellStyle style = workbook.createCellStyle();
            sheet.setColumnWidth(1, 20800);
            style.setWrapText(true);

            cell1.setCellStyle(style);
            cell1.setCellValue(s.toString());

            workbook.write(new FileOutputStream("src/main/resources/file.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}