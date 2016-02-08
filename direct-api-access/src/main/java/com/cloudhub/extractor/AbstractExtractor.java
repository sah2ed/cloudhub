/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.extractor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * <p>
 * Base class for all <code>Extractor</code> implementations.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public abstract class AbstractExtractor implements Extractor {

	/** Handles all logging needs. */
	protected Logger log;

	/**
	 * Default no arguments constructor.
	 */
	public AbstractExtractor() {
		this.log = LogManager.getLogger(this.getClass());
	}
}
