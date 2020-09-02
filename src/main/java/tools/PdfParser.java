package tools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;


public class PdfParser {

    private String content;


    public PdfParser(String url) {
        try (BufferedInputStream fileToParse = new BufferedInputStream(new URL(url).openStream()); PDDocument document = PDDocument.load(fileToParse)) {
            content = new PDFTextStripper().getText(document);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }


    public PdfParser(URL url) {
        try (BufferedInputStream fileToParse = new BufferedInputStream(url.openStream()); PDDocument document = PDDocument.load(fileToParse)) {
            content = new PDFTextStripper().getText(document);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }


    public void removeDoubleSpaces() {
        boolean marker = true;
        while (marker) {
            String newText = content.replace("  ", " ");
            if(newText.equals(content))
                marker = false;
            content = newText;
        }
    }


    public String getContent() {
        return content;
    }
}