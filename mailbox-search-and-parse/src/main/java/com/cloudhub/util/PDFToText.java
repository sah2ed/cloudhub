/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * <p>
 *  Converts a source PDF file to its textual constituents.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class PDFToText {

    /**
     * @param name - the file name
     * @return - the file name minus the file extension
     */
    public static String getName(String name) {
    	int index = name.lastIndexOf(".");
    	return name.substring(0, index);
    }

    /**
     * @param src - the PDF file to parse.
     * @return - the text equivalent of the PDF file.
     * @throws IOException - if any I/O error occurs.
     */
    public static File parsePdf(File src) throws IOException {
    	File dest = new File(src.getParent(), getName(src.getName()) + ".txt");
    	parsePdf(src.getAbsolutePath(), dest.getAbsolutePath());
    	return dest;
    }

    /**
     * Parses a PDF to a plain text file.
     *
     * @param source the original PDF
     * @param destination the resulting text
     * @throws IOException
     */
    public static void parsePdf(String source, String destination) throws IOException {
        PdfReader reader = new PdfReader(source);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(destination));
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            out.println(strategy.getResultantText());
        }
        out.flush();
        out.close();
    }
}
