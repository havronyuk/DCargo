package dccargo.dcargoservice.service.dcargo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteSheetService {




    public byte[] generateEmptyExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            createStyles(workbook);
            createSheet1(workbook);
            createSheet2(workbook);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private CellStyle[] styles;

    private void createStyles(Workbook wb) {
        styles = new CellStyle[26];
        Font[] fonts = new Font[3];
        fonts[0] = wb.createFont(); fonts[0].setFontName("Calibri"); fonts[0].setFontHeightInPoints((short) 11);
        fonts[1] = wb.createFont(); fonts[1].setFontName("Calibri"); fonts[1].setFontHeightInPoints((short) 9);
        fonts[2] = wb.createFont(); fonts[2].setFontName("Calibri"); fonts[2].setFontHeightInPoints((short) 10); fonts[2].setBold(true);
        BorderStyle[][] borderDefs = new BorderStyle[][] {
                {BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE},
                {BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE, BorderStyle.THIN},
                {BorderStyle.NONE, BorderStyle.NONE, BorderStyle.THIN, BorderStyle.THIN},
                {BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN},
                {BorderStyle.NONE, BorderStyle.NONE, BorderStyle.THIN, BorderStyle.NONE},
                {BorderStyle.THIN, BorderStyle.NONE, BorderStyle.THIN, BorderStyle.THIN},
                {BorderStyle.NONE, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN},
                {BorderStyle.THIN, BorderStyle.NONE, BorderStyle.THIN, BorderStyle.NONE},
                {BorderStyle.NONE, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.NONE},
                {BorderStyle.THIN, BorderStyle.NONE, BorderStyle.NONE, BorderStyle.THIN},
                {BorderStyle.NONE, BorderStyle.THIN, BorderStyle.NONE, BorderStyle.THIN},
                {BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.NONE},
                {BorderStyle.THIN, BorderStyle.THIN, BorderStyle.NONE, BorderStyle.NONE},
                {BorderStyle.THIN, BorderStyle.THIN, BorderStyle.NONE, BorderStyle.THIN},
                {BorderStyle.THIN, BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE},
                {BorderStyle.NONE, BorderStyle.THIN, BorderStyle.NONE, BorderStyle.NONE},
        };
        for (int i = 0; i < styles.length; i++) {
            styles[i] = wb.createCellStyle();
        }
        configureStyle(styles[0], fonts[1], 0, 4);
        configureStyle(styles[1], fonts[2], 0, 3);
        configureStyle(styles[2], fonts[1], 3, 3);
        configureStyle(styles[3], fonts[0], 2, 0);
        configureStyle(styles[4], fonts[0], 6, 0);
        configureStyle(styles[5], fonts[1], 3, 1);
        configureStyle(styles[6], fonts[1], 0, 1);
        configureStyle(styles[7], fonts[0], 8, 0);
        configureStyle(styles[8], fonts[0], 13, 0);
        configureStyle(styles[9], fonts[0], 9, 0);
        configureStyle(styles[10], fonts[0], 10, 0);
        configureStyle(styles[11], fonts[1], 2, 3);
        configureStyle(styles[12], fonts[1], 4, 3);
        configureStyle(styles[13], fonts[0], 4, 0);
        configureStyle(styles[14], fonts[0], 12, 0);
        configureStyle(styles[15], fonts[0], 14, 0);
        configureStyle(styles[16], fonts[0], 15, 0);
        configureStyle(styles[17], fonts[1], 1, 4);
        configureStyle(styles[18], fonts[0], 1, 0);
        configureStyle(styles[19], fonts[1], 1, 3);
        configureStyle(styles[20], fonts[1], 0, 6);
        configureStyle(styles[21], fonts[1], 1, 7);
        configureStyle(styles[22], fonts[1], 1, 5);
        configureStyle(styles[23], fonts[1], 0, 5);
        configureStyle(styles[24], fonts[1], 1, 8);
        configureStyle(styles[25], fonts[1], 0, 3);
    }

    private void configureStyle(CellStyle s, Font font, int borderId, int alignId) {
        s.setFont(font);
        switch (borderId) {
            case 0:
                break;
            case 1:
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 2:
                s.setBorderTop(BorderStyle.THIN);
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 3:
                s.setBorderLeft(BorderStyle.THIN);
                s.setBorderRight(BorderStyle.THIN);
                s.setBorderTop(BorderStyle.THIN);
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 4:
                s.setBorderTop(BorderStyle.THIN);
                break;
            case 5:
                s.setBorderLeft(BorderStyle.THIN);
                s.setBorderTop(BorderStyle.THIN);
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 6:
                s.setBorderRight(BorderStyle.THIN);
                s.setBorderTop(BorderStyle.THIN);
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 7:
                s.setBorderLeft(BorderStyle.THIN);
                s.setBorderTop(BorderStyle.THIN);
                break;
            case 8:
                s.setBorderRight(BorderStyle.THIN);
                s.setBorderTop(BorderStyle.THIN);
                break;
            case 9:
                s.setBorderLeft(BorderStyle.THIN);
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 10:
                s.setBorderRight(BorderStyle.THIN);
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 11:
                s.setBorderLeft(BorderStyle.THIN);
                s.setBorderRight(BorderStyle.THIN);
                s.setBorderTop(BorderStyle.THIN);
                break;
            case 12:
                s.setBorderLeft(BorderStyle.THIN);
                s.setBorderRight(BorderStyle.THIN);
                break;
            case 13:
                s.setBorderLeft(BorderStyle.THIN);
                s.setBorderRight(BorderStyle.THIN);
                s.setBorderBottom(BorderStyle.THIN);
                break;
            case 14:
                s.setBorderLeft(BorderStyle.THIN);
                break;
            case 15:
                s.setBorderRight(BorderStyle.THIN);
                break;
            default: break;
        }
        switch (alignId) {
            case 0:
                break;
            case 1:
                s.setAlignment(HorizontalAlignment.CENTER);
                s.setVerticalAlignment(VerticalAlignment.CENTER);
                s.setWrapText(true);
                break;
            case 2:
                s.setWrapText(true);
                break;
            case 3:
                s.setAlignment(HorizontalAlignment.CENTER);
                s.setVerticalAlignment(VerticalAlignment.CENTER);
                break;
            case 4:
                s.setAlignment(HorizontalAlignment.LEFT);
                s.setVerticalAlignment(VerticalAlignment.CENTER);
                break;
            case 5:
                s.setAlignment(HorizontalAlignment.CENTER);
                break;
            case 6:
                s.setVerticalAlignment(VerticalAlignment.CENTER);
                break;
            case 7:
                s.setAlignment(HorizontalAlignment.LEFT);
                s.setWrapText(true);
                break;
            case 8:
                s.setAlignment(HorizontalAlignment.LEFT);
                break;
            default: break;
        }
    }

    private void cell(Sheet sheet, String address, Object value, int styleId) {
        CellReference ref = new CellReference(address);
        Row row = sheet.getRow(ref.getRow());
        if (row == null) row = sheet.createRow(ref.getRow());
        Cell cell = row.getCell(ref.getCol());
        if (cell == null) cell = row.createCell(ref.getCol());
        if (value != null) {
            if (value instanceof Number) cell.setCellValue(((Number) value).doubleValue());
            else cell.setCellValue(value.toString());
        }
        cell.setCellStyle(styles[styleId]);
    }

    private void merge(Sheet sheet, String range) {
        sheet.addMergedRegion(CellRangeAddress.valueOf(range));
    }

    private void columnWidth(Sheet sheet, int column, double width) {
        sheet.setColumnWidth(column - 1, (int) Math.round(width * 256));
    }
    private void rowHeight(Sheet sheet, int row, double height) {
        Row r = sheet.getRow(row - 1);
        if (r == null) r = sheet.createRow(row - 1);
        r.setHeightInPoints((float) height);
    }

    private void printSetup(Sheet sheet) {
        PrintSetup ps = sheet.getPrintSetup();
        ps.setLandscape(true);
        ps.setPaperSize(PrintSetup.A4_PAPERSIZE);
        ps.setScale((short) 77);
        sheet.setMargin(Sheet.LeftMargin, 0.23622047244094488);
        sheet.setMargin(Sheet.RightMargin, 0.23622047244094488);
        sheet.setMargin(Sheet.TopMargin, 0.7480314960629921);
        sheet.setMargin(Sheet.BottomMargin, 0.7480314960629921);
        sheet.setMargin(Sheet.HeaderMargin, 0.31496062992125984);
        sheet.setMargin(Sheet.FooterMargin, 0.31496062992125984);
    }

    private void createSheet1(Workbook wb) {
        Sheet sheet = wb.createSheet("Лист1");
        columnWidth(sheet, 1, 4.42578125);
        columnWidth(sheet, 2, 30.5703125);
        columnWidth(sheet, 3, 10.85546875);
        columnWidth(sheet, 4, 5.7109375);
        columnWidth(sheet, 5, 25.7109375);
        columnWidth(sheet, 6, 5.28515625);
        columnWidth(sheet, 7, 9.140625);
        columnWidth(sheet, 8, 5.7109375);
        columnWidth(sheet, 9, 15.7109375);
        columnWidth(sheet, 10, 11.85546875);
        columnWidth(sheet, 11, 12.7109375);
        columnWidth(sheet, 12, 5.7109375);
        columnWidth(sheet, 13, 15.7109375);
        columnWidth(sheet, 14, 11.7109375);
        columnWidth(sheet, 15, 13.5703125);
        columnWidth(sheet, 16, 9.140625);
        columnWidth(sheet, 17, 9.140625);
        rowHeight(sheet, 4, 12.75);
        rowHeight(sheet, 5, 30.0);
        rowHeight(sheet, 6, 15.0);
        rowHeight(sheet, 7, 17.1);
        rowHeight(sheet, 8, 60.0);
        rowHeight(sheet, 10, 72.0);
        rowHeight(sheet, 11, 30.0);
        rowHeight(sheet, 12, 30.0);
        rowHeight(sheet, 13, 30.0);
        rowHeight(sheet, 14, 30.0);
        rowHeight(sheet, 15, 30.0);
        rowHeight(sheet, 16, 30.0);
        rowHeight(sheet, 18, 17.1);
        rowHeight(sheet, 19, 17.1);
        rowHeight(sheet, 21, 17.1);
        rowHeight(sheet, 23, 30.0);
        cell(sheet, "A1", "ЗАО \"Доброном\"    УНП 191178504", 0);
        cell(sheet, "A2", null, 0);
        cell(sheet, "B4", "Карточка учета работы автомобиля", 1);
        cell(sheet, "G4", "Работа автомобиля", 2);
        cell(sheet, "H4", null, 3);
        cell(sheet, "I4", null, 3);
        cell(sheet, "J4", null, 3);
        cell(sheet, "K4", null, 3);
        cell(sheet, "L4", null, 3);
        cell(sheet, "M4", null, 3);
        cell(sheet, "N4", null, 3);
        cell(sheet, "O4", null, 4);
        cell(sheet, "G5", "Операция", 2);
        cell(sheet, "H5", "Дата (число, месяц)", 2);
        cell(sheet, "I5", null, 4);
        cell(sheet, "J5", "Показания одометра", 2);
        cell(sheet, "K5", null, 4);
        cell(sheet, "L5", "Показания счетчика РЭФ", 2);
        cell(sheet, "M5", null, 4);
        cell(sheet, "N5", "Время работы спецоборудования, ч", 5);
        cell(sheet, "O5", null, 4);
        cell(sheet, "A6", "Автомобиль, прицеп, полуприцеп", 5);
        cell(sheet, "B6", null, 3);
        cell(sheet, "C6", null, 3);
        cell(sheet, "D6", null, 3);
        cell(sheet, "E6", null, 4);
        cell(sheet, "F6", null, 6);
        cell(sheet, "G6", "Выезд на линию", 5);
        cell(sheet, "H6", null, 5);
        cell(sheet, "I6", null, 7);
        cell(sheet, "J6", null, 5);
        cell(sheet, "K6", null, 7);
        cell(sheet, "L6", null, 5);
        cell(sheet, "M6", null, 7);
        cell(sheet, "N6", "РЭФ", 5);
        cell(sheet, "O6", "Вебасто", 5);
        cell(sheet, "P6", null, 6);
        cell(sheet, "A7", "Марка автомобиля, прицепа, полуприцепа", 5);
        cell(sheet, "B7", null, 4);
        cell(sheet, "C7", "Регистрационный знак", 5);
        cell(sheet, "D7", null, 3);
        cell(sheet, "E7", null, 4);
        cell(sheet, "F7", null, 6);
        cell(sheet, "G7", null, 8);
        cell(sheet, "H7", null, 9);
        cell(sheet, "I7", null, 10);
        cell(sheet, "J7", null, 9);
        cell(sheet, "K7", null, 10);
        cell(sheet, "L7", null, 9);
        cell(sheet, "M7", null, 10);
        cell(sheet, "N7", null, 8);
        cell(sheet, "O7", null, 8);
        cell(sheet, "P7", null, 6);
        cell(sheet, "A8", null, 2);
        cell(sheet, "B8", null, 4);
        cell(sheet, "C8", null, 2);
        cell(sheet, "D8", null, 3);
        cell(sheet, "E8", null, 4);
        cell(sheet, "G8", "Возвращение с линии", 5);
        cell(sheet, "H8", null, 2);
        cell(sheet, "I8", null, 4);
        cell(sheet, "J8", null, 2);
        cell(sheet, "K8", null, 4);
        cell(sheet, "L8", null, 2);
        cell(sheet, "M8", null, 4);
        cell(sheet, "N8", null, 2);
        cell(sheet, "O8", null, 2);
        cell(sheet, "A9", "Водитель", 11);
        cell(sheet, "B9", null, 3);
        cell(sheet, "C9", null, 3);
        cell(sheet, "D9", null, 3);
        cell(sheet, "E9", null, 3);
        cell(sheet, "G9", "Движение топливно-смазочных материалов (ТСМ)", 12);
        cell(sheet, "H9", null, 13);
        cell(sheet, "I9", null, 13);
        cell(sheet, "J9", null, 13);
        cell(sheet, "K9", null, 13);
        cell(sheet, "L9", null, 13);
        cell(sheet, "M9", null, 13);
        cell(sheet, "N9", null, 13);
        cell(sheet, "O9", null, 13);
        cell(sheet, "A10", "Фамилия, Имя, Отчество", 5);
        cell(sheet, "B10", null, 4);
        cell(sheet, "C10", "Табельный номер", 5);
        cell(sheet, "D10", "Водитель по состоянию здоровья к управлению допущен. Подпись (штамп)", 5);
        cell(sheet, "E10", null, 4);
        cell(sheet, "F10", null, 6);
        cell(sheet, "G10", "Заправка", 5);
        cell(sheet, "H10", "Дата (число, месяц)\nБак (основной, дополнительный, РЭФ)", 5);
        cell(sheet, "I10", null, 4);
        cell(sheet, "J10", "марка ТСМ", 5);
        cell(sheet, "K10", "количество, л", 5);
        cell(sheet, "L10", "Дата (число, месяц)\nБак (основной, дополнительный, РЭФ)", 5);
        cell(sheet, "M10", null, 4);
        cell(sheet, "N10", "марка ТСМ", 5);
        cell(sheet, "O10", "количество, л", 5);
        cell(sheet, "P10", null, 6);
        cell(sheet, "A11", "1й", 2);
        cell(sheet, "B11", null, 2);
        cell(sheet, "C11", null, 2);
        cell(sheet, "D11", null, 2);
        cell(sheet, "E11", null, 7);
        cell(sheet, "G11", null, 14);
        cell(sheet, "H11", null, 2);
        cell(sheet, "I11", null, 4);
        cell(sheet, "J11", null, 2);
        cell(sheet, "K11", null, 2);
        cell(sheet, "L11", null, 2);
        cell(sheet, "M11", null, 4);
        cell(sheet, "N11", null, 2);
        cell(sheet, "O11", null, 2);
        cell(sheet, "A12", null, 14);
        cell(sheet, "B12", null, 14);
        cell(sheet, "C12", null, 14);
        cell(sheet, "D12", null, 15);
        cell(sheet, "E12", null, 16);
        cell(sheet, "G12", null, 14);
        cell(sheet, "H12", null, 2);
        cell(sheet, "I12", null, 4);
        cell(sheet, "J12", null, 2);
        cell(sheet, "K12", null, 2);
        cell(sheet, "L12", null, 2);
        cell(sheet, "M12", null, 4);
        cell(sheet, "N12", null, 2);
        cell(sheet, "O12", null, 2);
        cell(sheet, "A13", null, 8);
        cell(sheet, "B13", null, 8);
        cell(sheet, "C13", null, 8);
        cell(sheet, "D13", null, 9);
        cell(sheet, "E13", null, 10);
        cell(sheet, "G13", null, 14);
        cell(sheet, "H13", null, 2);
        cell(sheet, "I13", null, 4);
        cell(sheet, "J13", null, 2);
        cell(sheet, "K13", null, 2);
        cell(sheet, "L13", null, 2);
        cell(sheet, "M13", null, 4);
        cell(sheet, "N13", null, 2);
        cell(sheet, "O13", null, 2);
        cell(sheet, "A14", "2й", 2);
        cell(sheet, "B14", null, 2);
        cell(sheet, "C14", null, 2);
        cell(sheet, "D14", null, 2);
        cell(sheet, "E14", null, 7);
        cell(sheet, "G14", null, 14);
        cell(sheet, "H14", null, 2);
        cell(sheet, "I14", null, 4);
        cell(sheet, "J14", null, 2);
        cell(sheet, "K14", null, 2);
        cell(sheet, "L14", null, 2);
        cell(sheet, "M14", null, 4);
        cell(sheet, "N14", null, 2);
        cell(sheet, "O14", null, 2);
        cell(sheet, "A15", null, 14);
        cell(sheet, "B15", null, 14);
        cell(sheet, "C15", null, 14);
        cell(sheet, "D15", null, 15);
        cell(sheet, "E15", null, 16);
        cell(sheet, "G15", null, 14);
        cell(sheet, "H15", null, 2);
        cell(sheet, "I15", null, 4);
        cell(sheet, "J15", null, 2);
        cell(sheet, "K15", null, 2);
        cell(sheet, "L15", null, 2);
        cell(sheet, "M15", null, 4);
        cell(sheet, "N15", null, 2);
        cell(sheet, "O15", null, 2);
        cell(sheet, "A16", null, 8);
        cell(sheet, "B16", null, 8);
        cell(sheet, "C16", null, 8);
        cell(sheet, "D16", null, 9);
        cell(sheet, "E16", null, 10);
        cell(sheet, "G16", null, 8);
        cell(sheet, "H16", null, 2);
        cell(sheet, "I16", null, 4);
        cell(sheet, "J16", null, 2);
        cell(sheet, "K16", null, 2);
        cell(sheet, "L16", null, 2);
        cell(sheet, "M16", null, 4);
        cell(sheet, "N16", null, 2);
        cell(sheet, "O16", null, 2);
        cell(sheet, "A18", "Прочие отметки", 17);
        cell(sheet, "B18", null, 18);
        cell(sheet, "C18", null, 19);
        cell(sheet, "D18", null, 19);
        cell(sheet, "E18", null, 19);
        cell(sheet, "F18", null, 19);
        cell(sheet, "G18", null, 19);
        cell(sheet, "H18", null, 19);
        cell(sheet, "I18", null, 19);
        cell(sheet, "J18", null, 19);
        cell(sheet, "K18", null, 19);
        cell(sheet, "L18", null, 19);
        cell(sheet, "M18", null, 19);
        cell(sheet, "N18", null, 19);
        cell(sheet, "O18", null, 19);
        cell(sheet, "A19", null, 19);
        cell(sheet, "B19", null, 19);
        cell(sheet, "C19", null, 19);
        cell(sheet, "D19", null, 19);
        cell(sheet, "E19", null, 19);
        cell(sheet, "F19", null, 19);
        cell(sheet, "G19", null, 19);
        cell(sheet, "H19", null, 19);
        cell(sheet, "I19", null, 19);
        cell(sheet, "J19", null, 19);
        cell(sheet, "K19", null, 19);
        cell(sheet, "L19", null, 19);
        cell(sheet, "M19", null, 19);
        cell(sheet, "N19", null, 19);
        cell(sheet, "O19", null, 19);
        cell(sheet, "A21", "Водительское удостоверение проверил, задание выдал", 0);
        cell(sheet, "E21", "Автомобиль технически исправен. Выезд разрешен", 0);
        cell(sheet, "H21", null, 20);
        cell(sheet, "I21", "Автомобиль в технически исправном состоянии принял", 0);
        cell(sheet, "M21", "Автомобиль сдал", 0);
        cell(sheet, "A23", "Подпись\n(штамп)", 21);
        cell(sheet, "B23", null, 18);
        cell(sheet, "C23", null, 22);
        cell(sheet, "D23", null, 23);
        cell(sheet, "E23", "Подпись\n(штамп)", 21);
        cell(sheet, "F23", null, 18);
        cell(sheet, "G23", null, 22);
        cell(sheet, "H23", null, 23);
        cell(sheet, "I23", "Подпись водителя", 24);
        cell(sheet, "J23", null, 18);
        cell(sheet, "K23", null, 22);
        cell(sheet, "L23", null, 23);
        cell(sheet, "M23", "Подпись водителя", 24);
        cell(sheet, "N23", null, 18);
        cell(sheet, "O23", null, 22);
        merge(sheet, "B4:E4");
        merge(sheet, "G4:O4");
        merge(sheet, "H5:I5");
        merge(sheet, "J5:K5");
        merge(sheet, "L5:M5");
        merge(sheet, "N5:O5");
        merge(sheet, "A6:E6");
        merge(sheet, "G6:G7");
        merge(sheet, "H6:I7");
        merge(sheet, "J6:K7");
        merge(sheet, "L6:M7");
        merge(sheet, "N6:N7");
        merge(sheet, "O6:O7");
        merge(sheet, "A7:B7");
        merge(sheet, "C7:E7");
        merge(sheet, "A8:B8");
        merge(sheet, "C8:E8");
        merge(sheet, "H8:I8");
        merge(sheet, "J8:K8");
        merge(sheet, "L8:M8");
        merge(sheet, "A9:E9");
        merge(sheet, "G9:O9");
        merge(sheet, "A10:B10");
        merge(sheet, "D10:E10");
        merge(sheet, "G10:G16");
        merge(sheet, "H10:I10");
        merge(sheet, "L10:M10");
        merge(sheet, "A11:A13");
        merge(sheet, "B11:B13");
        merge(sheet, "C11:C13");
        merge(sheet, "D11:E13");
        merge(sheet, "H11:I11");
        merge(sheet, "L11:M11");
        merge(sheet, "H12:I12");
        merge(sheet, "L12:M12");
        merge(sheet, "H13:I13");
        merge(sheet, "L13:M13");
        merge(sheet, "A14:A16");
        merge(sheet, "B14:B16");
        merge(sheet, "C14:C16");
        merge(sheet, "D14:E16");
        merge(sheet, "H14:I14");
        merge(sheet, "L14:M14");
        merge(sheet, "H15:I15");
        merge(sheet, "L15:M15");
        merge(sheet, "H16:I16");
        merge(sheet, "L16:M16");
        merge(sheet, "A18:B18");
        merge(sheet, "A21:C21");
        merge(sheet, "E21:G21");
        merge(sheet, "A23:B23");
        merge(sheet, "E23:F23");
        merge(sheet, "I23:J23");
        merge(sheet, "M23:N23");
        printSetup(sheet);
    }

    private void createSheet2(Workbook wb) {
        Sheet sheet = wb.createSheet("Лист2");
        columnWidth(sheet, 1, 18.140625);
        columnWidth(sheet, 2, 22.42578125);
        columnWidth(sheet, 3, 45.5703125);
        columnWidth(sheet, 4, 16.28515625);
        columnWidth(sheet, 5, 9.140625);
        columnWidth(sheet, 6, 10.85546875);
        columnWidth(sheet, 7, 3.7109375);
        columnWidth(sheet, 8, 25.85546875);
        columnWidth(sheet, 9, 3.7109375);
        columnWidth(sheet, 10, 25.85546875);
        columnWidth(sheet, 11, 9.140625);
        columnWidth(sheet, 12, 13.0);
        rowHeight(sheet, 2, 36.0);
        rowHeight(sheet, 3, 17.1);
        rowHeight(sheet, 4, 17.1);
        rowHeight(sheet, 5, 17.1);
        rowHeight(sheet, 6, 17.1);
        rowHeight(sheet, 7, 17.1);
        rowHeight(sheet, 8, 17.1);
        rowHeight(sheet, 9, 17.1);
        rowHeight(sheet, 10, 17.1);
        rowHeight(sheet, 11, 17.1);
        rowHeight(sheet, 12, 17.1);
        rowHeight(sheet, 13, 17.1);
        rowHeight(sheet, 14, 17.1);
        rowHeight(sheet, 15, 17.1);
        rowHeight(sheet, 16, 17.1);
        rowHeight(sheet, 17, 17.1);
        rowHeight(sheet, 18, 17.1);
        rowHeight(sheet, 19, 17.1);
        rowHeight(sheet, 20, 17.1);
        rowHeight(sheet, 21, 17.1);
        rowHeight(sheet, 22, 17.1);
        rowHeight(sheet, 23, 17.1);
        rowHeight(sheet, 24, 17.1);
        rowHeight(sheet, 25, 17.1);
        rowHeight(sheet, 26, 17.1);
        rowHeight(sheet, 27, 17.1);
        rowHeight(sheet, 28, 17.1);
        rowHeight(sheet, 29, 12.0);
        rowHeight(sheet, 30, 17.1);
        rowHeight(sheet, 31, 17.1);
        rowHeight(sheet, 34, 17.1);
        cell(sheet, "D1", "Выполнение задания", 19);
        cell(sheet, "E1", null, 18);
        cell(sheet, "F1", null, 18);
        cell(sheet, "A2", "Заказчик", 5);
        cell(sheet, "B2", "Дата, время выезда и возвращения", 5);
        cell(sheet, "C2", "Маршрут\nпункт отправления - пункт назначения\n(адрес места погрузки - разгрузки)", 5);
        cell(sheet, "D2", "Номер склада -\nномер магазина", 5);
        cell(sheet, "E2", "Вес груза, т.", 5);
        cell(sheet, "F2", "Расстояние, км.", 5);
        cell(sheet, "G2", "Номера товарно-сопроводительных документов (ТТН, реестр)", 5);
        cell(sheet, "H2", null, 3);
        cell(sheet, "I2", null, 3);
        cell(sheet, "J2", null, 4);
        cell(sheet, "A3", null, 2);
        cell(sheet, "B3", null, 2);
        cell(sheet, "C3", null, 2);
        cell(sheet, "D3", null, 2);
        cell(sheet, "E3", null, 2);
        cell(sheet, "F3", null, 2);
        cell(sheet, "G3", 1, 2);
        cell(sheet, "H3", null, 2);
        cell(sheet, "I3", 26, 2);
        cell(sheet, "J3", null, 2);
        cell(sheet, "A4", null, 2);
        cell(sheet, "B4", null, 2);
        cell(sheet, "C4", null, 2);
        cell(sheet, "D4", null, 2);
        cell(sheet, "E4", null, 2);
        cell(sheet, "F4", null, 2);
        cell(sheet, "G4", 2, 2);
        cell(sheet, "H4", null, 2);
        cell(sheet, "I4", 27, 2);
        cell(sheet, "J4", null, 2);
        cell(sheet, "A5", null, 2);
        cell(sheet, "B5", null, 2);
        cell(sheet, "C5", null, 2);
        cell(sheet, "D5", null, 2);
        cell(sheet, "E5", null, 2);
        cell(sheet, "F5", null, 2);
        cell(sheet, "G5", 3, 2);
        cell(sheet, "H5", null, 2);
        cell(sheet, "I5", 28, 2);
        cell(sheet, "J5", null, 2);
        cell(sheet, "A6", null, 2);
        cell(sheet, "B6", null, 2);
        cell(sheet, "C6", null, 2);
        cell(sheet, "D6", null, 2);
        cell(sheet, "E6", null, 2);
        cell(sheet, "F6", null, 2);
        cell(sheet, "G6", 4, 2);
        cell(sheet, "H6", null, 2);
        cell(sheet, "I6", 29, 2);
        cell(sheet, "J6", null, 2);
        cell(sheet, "A7", null, 2);
        cell(sheet, "B7", null, 2);
        cell(sheet, "C7", null, 2);
        cell(sheet, "D7", null, 2);
        cell(sheet, "E7", null, 2);
        cell(sheet, "F7", null, 2);
        cell(sheet, "G7", 5, 2);
        cell(sheet, "H7", null, 2);
        cell(sheet, "I7", 30, 2);
        cell(sheet, "J7", null, 2);
        cell(sheet, "A8", null, 2);
        cell(sheet, "B8", null, 2);
        cell(sheet, "C8", null, 2);
        cell(sheet, "D8", null, 2);
        cell(sheet, "E8", null, 2);
        cell(sheet, "F8", null, 2);
        cell(sheet, "G8", 6, 2);
        cell(sheet, "H8", null, 2);
        cell(sheet, "I8", 31, 2);
        cell(sheet, "J8", null, 2);
        cell(sheet, "A9", null, 2);
        cell(sheet, "B9", null, 2);
        cell(sheet, "C9", null, 2);
        cell(sheet, "D9", null, 2);
        cell(sheet, "E9", null, 2);
        cell(sheet, "F9", null, 2);
        cell(sheet, "G9", 7, 2);
        cell(sheet, "H9", null, 2);
        cell(sheet, "I9", 32, 2);
        cell(sheet, "J9", null, 2);
        cell(sheet, "A10", null, 2);
        cell(sheet, "B10", null, 2);
        cell(sheet, "C10", null, 2);
        cell(sheet, "D10", null, 2);
        cell(sheet, "E10", null, 2);
        cell(sheet, "F10", null, 2);
        cell(sheet, "G10", 8, 2);
        cell(sheet, "H10", null, 2);
        cell(sheet, "I10", 33, 2);
        cell(sheet, "J10", null, 2);
        cell(sheet, "A11", null, 2);
        cell(sheet, "B11", null, 2);
        cell(sheet, "C11", null, 2);
        cell(sheet, "D11", null, 2);
        cell(sheet, "E11", null, 2);
        cell(sheet, "F11", null, 2);
        cell(sheet, "G11", 9, 2);
        cell(sheet, "H11", null, 2);
        cell(sheet, "I11", 34, 2);
        cell(sheet, "J11", null, 2);
        cell(sheet, "A12", null, 2);
        cell(sheet, "B12", null, 2);
        cell(sheet, "C12", null, 2);
        cell(sheet, "D12", null, 2);
        cell(sheet, "E12", null, 2);
        cell(sheet, "F12", null, 2);
        cell(sheet, "G12", 10, 2);
        cell(sheet, "H12", null, 2);
        cell(sheet, "I12", 35, 2);
        cell(sheet, "J12", null, 2);
        cell(sheet, "A13", null, 2);
        cell(sheet, "B13", null, 2);
        cell(sheet, "C13", null, 2);
        cell(sheet, "D13", null, 2);
        cell(sheet, "E13", null, 2);
        cell(sheet, "F13", null, 2);
        cell(sheet, "G13", 11, 2);
        cell(sheet, "H13", null, 2);
        cell(sheet, "I13", 36, 2);
        cell(sheet, "J13", null, 2);
        cell(sheet, "A14", null, 2);
        cell(sheet, "B14", null, 2);
        cell(sheet, "C14", null, 2);
        cell(sheet, "D14", null, 2);
        cell(sheet, "E14", null, 2);
        cell(sheet, "F14", null, 2);
        cell(sheet, "G14", 12, 2);
        cell(sheet, "H14", null, 2);
        cell(sheet, "I14", 37, 2);
        cell(sheet, "J14", null, 2);
        cell(sheet, "A15", null, 2);
        cell(sheet, "B15", null, 2);
        cell(sheet, "C15", null, 2);
        cell(sheet, "D15", null, 2);
        cell(sheet, "E15", null, 2);
        cell(sheet, "F15", null, 2);
        cell(sheet, "G15", 13, 2);
        cell(sheet, "H15", null, 2);
        cell(sheet, "I15", 38, 2);
        cell(sheet, "J15", null, 2);
        cell(sheet, "A16", null, 2);
        cell(sheet, "B16", null, 2);
        cell(sheet, "C16", null, 2);
        cell(sheet, "D16", null, 2);
        cell(sheet, "E16", null, 2);
        cell(sheet, "F16", null, 2);
        cell(sheet, "G16", 14, 2);
        cell(sheet, "H16", null, 2);
        cell(sheet, "I16", 39, 2);
        cell(sheet, "J16", null, 2);
        cell(sheet, "A17", null, 2);
        cell(sheet, "B17", null, 2);
        cell(sheet, "C17", null, 2);
        cell(sheet, "D17", null, 2);
        cell(sheet, "E17", null, 2);
        cell(sheet, "F17", null, 2);
        cell(sheet, "G17", 15, 2);
        cell(sheet, "H17", null, 2);
        cell(sheet, "I17", 40, 2);
        cell(sheet, "J17", null, 2);
        cell(sheet, "A18", null, 2);
        cell(sheet, "B18", null, 2);
        cell(sheet, "C18", null, 2);
        cell(sheet, "D18", null, 2);
        cell(sheet, "E18", null, 2);
        cell(sheet, "F18", null, 2);
        cell(sheet, "G18", 16, 2);
        cell(sheet, "H18", null, 2);
        cell(sheet, "I18", 41, 2);
        cell(sheet, "J18", null, 2);
        cell(sheet, "A19", null, 2);
        cell(sheet, "B19", null, 2);
        cell(sheet, "C19", null, 2);
        cell(sheet, "D19", null, 2);
        cell(sheet, "E19", null, 2);
        cell(sheet, "F19", null, 2);
        cell(sheet, "G19", 17, 2);
        cell(sheet, "H19", null, 2);
        cell(sheet, "I19", 42, 2);
        cell(sheet, "J19", null, 2);
        cell(sheet, "A20", null, 2);
        cell(sheet, "B20", null, 2);
        cell(sheet, "C20", null, 2);
        cell(sheet, "D20", null, 2);
        cell(sheet, "E20", null, 2);
        cell(sheet, "F20", null, 2);
        cell(sheet, "G20", 18, 2);
        cell(sheet, "H20", null, 2);
        cell(sheet, "I20", 43, 2);
        cell(sheet, "J20", null, 2);
        cell(sheet, "A21", null, 2);
        cell(sheet, "B21", null, 2);
        cell(sheet, "C21", null, 2);
        cell(sheet, "D21", null, 2);
        cell(sheet, "E21", null, 2);
        cell(sheet, "F21", null, 2);
        cell(sheet, "G21", 19, 2);
        cell(sheet, "H21", null, 2);
        cell(sheet, "I21", 44, 2);
        cell(sheet, "J21", null, 2);
        cell(sheet, "A22", null, 2);
        cell(sheet, "B22", null, 2);
        cell(sheet, "C22", null, 2);
        cell(sheet, "D22", null, 2);
        cell(sheet, "E22", null, 2);
        cell(sheet, "F22", null, 2);
        cell(sheet, "G22", 20, 2);
        cell(sheet, "H22", null, 2);
        cell(sheet, "I22", 45, 2);
        cell(sheet, "J22", null, 2);
        cell(sheet, "A23", null, 2);
        cell(sheet, "B23", null, 2);
        cell(sheet, "C23", null, 2);
        cell(sheet, "D23", null, 2);
        cell(sheet, "E23", null, 2);
        cell(sheet, "F23", null, 2);
        cell(sheet, "G23", 21, 2);
        cell(sheet, "H23", null, 2);
        cell(sheet, "I23", 46, 2);
        cell(sheet, "J23", null, 2);
        cell(sheet, "A24", null, 2);
        cell(sheet, "B24", null, 2);
        cell(sheet, "C24", null, 2);
        cell(sheet, "D24", null, 2);
        cell(sheet, "E24", null, 2);
        cell(sheet, "F24", null, 2);
        cell(sheet, "G24", 22, 2);
        cell(sheet, "H24", null, 2);
        cell(sheet, "I24", 47, 2);
        cell(sheet, "J24", null, 2);
        cell(sheet, "A25", null, 2);
        cell(sheet, "B25", null, 2);
        cell(sheet, "C25", null, 2);
        cell(sheet, "D25", null, 2);
        cell(sheet, "E25", null, 2);
        cell(sheet, "F25", null, 2);
        cell(sheet, "G25", 23, 2);
        cell(sheet, "H25", null, 2);
        cell(sheet, "I25", 48, 2);
        cell(sheet, "J25", null, 2);
        cell(sheet, "A26", null, 2);
        cell(sheet, "B26", null, 2);
        cell(sheet, "C26", null, 2);
        cell(sheet, "D26", null, 2);
        cell(sheet, "E26", null, 2);
        cell(sheet, "F26", null, 2);
        cell(sheet, "G26", 24, 2);
        cell(sheet, "H26", null, 2);
        cell(sheet, "I26", 49, 2);
        cell(sheet, "J26", null, 2);
        cell(sheet, "A27", null, 2);
        cell(sheet, "B27", null, 2);
        cell(sheet, "C27", null, 2);
        cell(sheet, "D27", null, 2);
        cell(sheet, "E27", null, 2);
        cell(sheet, "F27", null, 2);
        cell(sheet, "G27", 25, 2);
        cell(sheet, "H27", null, 2);
        cell(sheet, "I27", 50, 2);
        cell(sheet, "J27", null, 2);
        cell(sheet, "A28", "Итого", 2);
        cell(sheet, "B28", null, 3);
        cell(sheet, "C28", null, 3);
        cell(sheet, "D28", null, 4);
        cell(sheet, "E28", null, 2);
        cell(sheet, "F28", null, 2);
        cell(sheet, "G28", null, 2);
        cell(sheet, "H28", null, 4);
        cell(sheet, "I28", null, 2);
        cell(sheet, "J28", null, 4);
        cell(sheet, "A30", "Прочие отметки", 17);
        cell(sheet, "B30", null, 19);
        cell(sheet, "C30", null, 19);
        cell(sheet, "D30", null, 19);
        cell(sheet, "E30", null, 19);
        cell(sheet, "F30", null, 19);
        cell(sheet, "G30", null, 19);
        cell(sheet, "H30", null, 19);
        cell(sheet, "I30", null, 19);
        cell(sheet, "J30", null, 19);
        cell(sheet, "A31", null, 11);
        cell(sheet, "B31", null, 11);
        cell(sheet, "C31", null, 11);
        cell(sheet, "D31", null, 11);
        cell(sheet, "E31", null, 11);
        cell(sheet, "F31", null, 11);
        cell(sheet, "G31", null, 11);
        cell(sheet, "H31", null, 11);
        cell(sheet, "I31", null, 11);
        cell(sheet, "J31", null, 11);
        cell(sheet, "A34", "Подпись водителя", 0);
        cell(sheet, "B34", null, 19);
        cell(sheet, "D34", "Проверил, подпись (штамп)", 25);
        cell(sheet, "F34", null, 19);
        cell(sheet, "G34", null, 19);
        cell(sheet, "H34", null, 19);
        merge(sheet, "D1:F1");
        merge(sheet, "G2:J2");
        merge(sheet, "A28:D28");
        merge(sheet, "G28:H28");
        merge(sheet, "I28:J28");
        merge(sheet, "D34:E34");
        printSetup(sheet);
    }

}