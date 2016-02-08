/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.extractor;

/**
 * <p>
 *  Base class for implementations of the {@code Extractor} interface that consume <code>email inboxes</code>.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public abstract class EmailMessageExtractor extends CSVExtractor {

	@Override
	abstract protected String getTotalCostFromLine(final String line);
}
