package com.icis.demo.Service;

import com.icis.demo.Entity.Application;
import com.icis.demo.Entity.DocumentStorable;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class DocumentGeneratorService {

    public void generateApplicationLetter(Map<String, String> data, String templatePath, String outputPath) throws IOException {
        Resource templateResource = new ClassPathResource(templatePath);
        try (InputStream is = templateResource.getInputStream()) {
            XWPFDocument document = new XWPFDocument(is);

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replacePlaceholders(paragraph, data);
            }

            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replacePlaceholders(paragraph, data);
                        }
                    }
                }
            }

            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                document.write(out);
            }
        }
    }

    public DocumentStorable createStorableDocument(Application application, String name, String data) {
        DocumentStorable documentStorable = new DocumentStorable();
        documentStorable.setData(data);
        documentStorable.setName(name);
        documentStorable.setApplicationId(application);
        return documentStorable;
    }

    private void replacePlaceholders(XWPFParagraph paragraph, Map<String, String> data) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    text = text.replace("{{" + entry.getKey() + "}}", entry.getValue());
                }
                run.setText(text, 0);
            }
        }
    }
}
