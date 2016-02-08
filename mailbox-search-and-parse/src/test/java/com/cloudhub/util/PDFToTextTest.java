/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.util;
import java.io.IOException;

import com.cloudhub.util.PDFToText;

/**
 * <p>
 *  Test class for the {@code PDFToText} class.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class PDFToTextTest {

	/** The original PDF that will be parsed. */
    private static final String PREFACE = "C:\\Work\\TC\\CloudHub\\direct-api-access\\test_files\\invoice64374056.pdf";
    /** The resulting text file. */
    private static final String RESULT = "C:\\Work\\TC\\CloudHub\\direct-api-access\\test_files\\invoice64374056.txt";


    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        PDFToText.parsePdf(PREFACE, RESULT);
    }

}
