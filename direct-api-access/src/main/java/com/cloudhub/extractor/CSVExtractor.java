/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.cloudhub.matcher.CSVMatcher;

/**
 * <p>
 *  Base class for implementations of the {@code Extractor} interface that consume <code>CSV</code>.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public abstract class CSVExtractor extends AbstractExtractor {

	/**
	 * Parses the total cost from the billing csv file.
	 *
	 * @param matcher - the CSV matcher to use.
	 * @param csvFile - the CSV file to parse
	 * @return - the total cost
	 * @throws IOException - if any I/O error occurs while parsing the file.
	 */
	protected String getTotal(final CSVMatcher matcher, final File csvFile) throws IOException {
		String totalCost = null;

		try (final BufferedReader csvReader = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = csvReader.readLine()) != null) {
				line = line.trim();
				if (line.contains(matcher.getLinePattern())) {
					totalCost = getTotalCostFromLine(line);
					break;
				}
			}
		}

		return totalCost;
	}

	/**
	 *
	 * @param line - the line that matches our criteria
	 * @return - the total cost including currency.
	 */
	protected String getTotalCostFromLine(final String line) {
		String columns[] = line.split(",");

		return columns[24].replaceAll("\"", "")
				+ columns[columns.length - 1].replaceAll("\"", ""); // "USD" + "35.43"
	}
}
