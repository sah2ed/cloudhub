/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.matcher;

/**
 * <p>
 *  Its attributes are used to match the billing information we are interested in from a CSV file.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public abstract class CSVMatcher {

	/** The name of this matcher instance. */
	protected String name;

	/** A unique part of the end of the key/file name in the enclosing bucket. */
	protected String keyPattern;

	/** A unique string on the line containing the total cost in the CSV. */
	protected String linePattern;

	public CSVMatcher(final String name, final String keyPattern, final String linePattern) {
		this.name = name;
		this.keyPattern = keyPattern;
		this.linePattern = linePattern;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the key pattern
	 */
	public String getKeyPattern() {
		return keyPattern;
	}

	/**
	 * @return the line pattern
	 */
	public String getLinePattern() {
		return linePattern;
	}

}
