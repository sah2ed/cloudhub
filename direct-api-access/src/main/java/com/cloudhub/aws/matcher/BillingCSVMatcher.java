/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.aws.matcher;

import com.cloudhub.matcher.CSVMatcher;

/**
 * <p>
 *  Allows us to match the contents of an Amazon S3 bucket containing billing CSV files
 *  whole key/file name adhere to the following file name format:
 *  <pre>
 *  <code><aws-account-identifier>-aws-billing-csv-YYYY-MM.csv</code>.
 *
 *  An example: <code>999999999999-aws-billing-csv-2016-01.csv</code>.
 *  </pre>
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class BillingCSVMatcher extends CSVMatcher {

	/**
	 *
	 * @param period - string representation of the time period whose CSV billing file is to be matched.
	 * 					must be formatted as 'YYYY-MM' ex: '2016-01'
	 */
	public BillingCSVMatcher(final String period) {
		super("AWS Billing CSV", "-aws-billing-csv-" + period + ".csv", "StatementTotal");
	}
}
