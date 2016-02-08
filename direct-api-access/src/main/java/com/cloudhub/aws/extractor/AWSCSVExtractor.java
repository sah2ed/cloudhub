/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.aws.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloudhub.Constants;
import com.cloudhub.extractor.CSVExtractor;
import com.cloudhub.matcher.CSVMatcher;

/**
 * <p>Extracts estimated billing information from an Amazon S3 Bucket.</p>
 *
 *
 * @author sah2ed
 * @version 1.0
 */
public class AWSCSVExtractor extends CSVExtractor {
	/**
	 * The name of the bucket containing billing info to be accessed.
	 */
	private final String bucketName;

	/**
	 * The path to the folder where all downloaded files will be written.
	 */
	private final String dataFolder;

	/**
	 * The Amazon S3 client object;
	 */
	private final AmazonS3 s3client;


	public AWSCSVExtractor(final String bucketName, final String dataFolder) {
		this.bucketName = bucketName;
		this.dataFolder = dataFolder;
		this.s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	}

	/**
	 * Requests billing information from Amazon S3.
	 * This method may spawn multiple threads as needed to complete the task.
	 *
	 */
	@Override
	public String getTotalCost() {
		String totalCost = null;

		try {
			log.debug("Listing objects ...");

			final ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
					.withBucketName(bucketName);

			ObjectListing objectListing;
			do {
				objectListing = s3client.listObjects(listObjectsRequest);
				for (final S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
					log.debug(" - " + objectSummary.getKey() + "  " +
							"(size = " + objectSummary.getSize() + ")");

					if (objectSummary.getKey().contains(Constants.MATCHER_BILLING_CSV.getKeyPattern())) {
						totalCost = persist(Constants.MATCHER_BILLING_CSV, objectSummary);

					} else if (objectSummary.getKey().contains(Constants.MATCHER_COST_ALLOCATION.getKeyPattern())) {
						totalCost = persist(Constants.MATCHER_COST_ALLOCATION, objectSummary);
					}
				}
				listObjectsRequest.setMarker(objectListing.getNextMarker());
			} while (objectListing.isTruncated());

		} catch (AmazonServiceException ase) {
			log.error("Caught an AmazonServiceException, " +
					"which means your request made it " +
					"to Amazon S3, but was rejected with an error response " +
					"for some reason.");
			log.error("Error Message:    " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code:   " + ase.getErrorCode());
			log.error("Error Type:       " + ase.getErrorType());
			log.error("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			log.error("Caught an AmazonClientException, " +
					"which means the client encountered " +
					"an internal error while trying to communicate" +
					" with S3, " +
					"such as not being able to access the network.");
			log.error("Error Message: " + ace.getMessage());

		} catch (IOException ioe) {
			log.error("Caught an IOException while writing to disk.");
			log.error("Error Message: " + ioe.getMessage());

		}

		return totalCost;
	}

	/**
	 * Persists the Amazon S3 object to disk
	 *
	 * @param objectSummary - the S3 object to be persisted to disk.
	 * @throws IOException - if an I/O error occurs.
	 */
	private String persist(final CSVMatcher matcher, final S3ObjectSummary objectSummary) throws IOException {
		log.debug("Downloading the body of " + objectSummary.getKey() + " from Amazon S3.");
		final S3Object object = s3client.getObject(bucketName, objectSummary.getKey());
		log.debug("Downloaded " + objectSummary.getSize() + " bytes.");

		log.debug("Writing the body of " + objectSummary.getKey() + " to disk path: " + dataFolder + File.separator + bucketName);
        final File objectCSVFile = writeOutObjectToFile(objectSummary, object.getObjectContent());

        return getTotal(matcher, objectCSVFile);
	}

	/**
	 * Writes the specified Amazon S3 object to the file system.
	 *
	 * @param object - the object to write out
	 * @param inputStream - the body of the object
	 * @throws IOException - if any I/O error occurs.
	 */
	@SuppressWarnings("resource")
	private File writeOutObjectToFile(final S3ObjectSummary object, final InputStream inputStream) throws IOException {

		final File parent = new File(dataFolder, object.getBucketName());
		if (!parent.exists()) {
			parent.mkdirs();
		}
		final File objectFile = new File(parent, object.getKey());

		final ReadableByteChannel src = Channels.newChannel(inputStream);
		final FileChannel dest =
				new FileOutputStream(objectFile).getChannel();

		dest.transferFrom(src, 0, object.getSize());
		dest.close();
		src.close();

		return objectFile;
	}
}
