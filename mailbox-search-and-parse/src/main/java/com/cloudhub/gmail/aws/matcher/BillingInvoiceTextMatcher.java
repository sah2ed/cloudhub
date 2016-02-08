/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.gmail.aws.matcher;

import com.cloudhub.matcher.CSVMatcher;

/**
 * <p>
 *  Allows us to match the total cost of an Amazon PDF invoice converted to text.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class BillingInvoiceTextMatcher extends CSVMatcher {

	/**
	 *  Default constructor.
	 */
	public BillingInvoiceTextMatcher() {
		super("AWS Billing Invoice", "invoice*.txt", "Total for this invoice ");
	}
}
