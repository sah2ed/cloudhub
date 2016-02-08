/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub;

import com.cloudhub.aws.matcher.BillingCSVMatcher;
import com.cloudhub.aws.matcher.CostAllocationCSVMatcher;
import com.cloudhub.matcher.CSVMatcher;

/**
 * <p>
 *  Container class for application constants.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public final class Constants {

	/** Will match 'aws-billing-csv' keys in the bucket. */
	public static final CSVMatcher MATCHER_BILLING_CSV = new BillingCSVMatcher(Options.TIME_PERIOD);

	/** Will match 'aws-cost-allocation' keys in the bucket. */
	public static final CSVMatcher MATCHER_COST_ALLOCATION = new CostAllocationCSVMatcher(Options.TIME_PERIOD);

	/**
	 * Will match AWS billing emails from 'aws-receivables@amazon.com' and 'aws-receivables-support@email.amazon.com'.
	 */
	public static final String AWS_FROM_EMAIL_PATTERN = "from:aws-receivables*@*amazon.com";

	/**
	 * Will match AWS billing emails sent after the specified date.
	 */
	public static final String AWS_DATE_FILTER = "before:" + Options.TIME_PERIOD + "-05";

}
