/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub;

/**
 * <p>
 *  Holds all command line options passed to the application.
 * </p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class Options {

	/**
	 * The default time period whose billing info will be fetched.
	 */
	public static final String TIME_PERIOD = System.getProperty("time_period", "2016-01");

	/**
	 * The local file folder where cloud billing files will be written to.
	 */
	public static final String DATA_FOLDER = System.getProperty("data_folder", "C:\\Work\\TC\\CloudHub\\direct-api-access\\test_files");

	/**
	 * The Amazon AWS S3 service bucket name where billing files will be read from.
	 */
	public static final String BUCKET_NAME_AWS = System.getProperty("bucket_name_aws", "cloudhub-aws-demo");

	/**
	 * The Gmail address like 'example@gmail.com' where billing files will be read from.
	 * 'me' can be used as a stand-in user id for the currently-logged in user's email address.
	 */
	public static final String GMAIL_USER = System.getProperty("gmail_user", "me");

	/**
	 * The local file folder to store Gmail user credentials for this application.
	 */
	public static final String GMAIL_DATA_FOLDER = System.getProperty("gmail_data_folder", ".gmail/credentials");



}
