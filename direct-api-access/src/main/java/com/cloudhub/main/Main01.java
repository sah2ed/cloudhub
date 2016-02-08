/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.main;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cloudhub.Options;
import com.cloudhub.aws.extractor.AWSCSVExtractor;
import com.cloudhub.extractor.Extractor;

/**
 * <p>Entry point into the first application. </p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class Main01 {

	/**
	 * Handles our logging needs.
	 */
	private static final Logger log = LogManager.getLogger(Main01.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Extractor extractor = new AWSCSVExtractor(Options.BUCKET_NAME_AWS, Options.DATA_FOLDER);
        log.info("The total cost (estimated) for the \"" + Options.TIME_PERIOD + "\" billing period is " + extractor.getTotalCost());
	}

}
