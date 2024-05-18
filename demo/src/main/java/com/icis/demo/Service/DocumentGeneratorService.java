package com.icis.demo.Service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
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
            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                document.write(out);
            }
        }


    }
}
