/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.aws.matcher;

import com.cloudhub.matcher.CSVMatcher;

/**
 * <p>
 * {@see BillingCSVMatcher}
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class CostAllocationCSVMatcher extends CSVMatcher {

	/**
	 *
	 * @param period - string representation of the time period whose CSV billing file is to be matched.
	 * 					must be formatted as 'YYYY-MM' ex: '2016-01'
	 *
	 */
	public CostAllocationCSVMatcher(final String period) {
		super("AWS Cost Allocation", "-aws-cost-allocation-" + period + ".csv", "StatementTotal");
	}
}
