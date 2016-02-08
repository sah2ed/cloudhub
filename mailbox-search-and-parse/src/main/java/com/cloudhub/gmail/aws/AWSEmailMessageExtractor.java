/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.gmail.aws;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.cloudhub.Constants;
import com.cloudhub.gmail.GmailEmailMessageExtractor;
import com.cloudhub.gmail.aws.matcher.BillingInvoiceTextMatcher;
import com.cloudhub.matcher.CSVMatcher;
import com.cloudhub.util.PDFToText;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

/**
 * <p>Extracts <i>actual</i> billing information for AWS from monthly invoice emails.</p>
 *
 * <p>This class is thread safe.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public class AWSEmailMessageExtractor extends GmailEmailMessageExtractor {
	/**
	 * The CSV matcher to use to match the relevant text in the invoice.
	 */
	private CSVMatcher awsMatcher;

	public AWSEmailMessageExtractor(Gmail service, String userId) {
		super(service, userId);
		this.awsMatcher = new BillingInvoiceTextMatcher();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getTotalCostFromLine(final String line) {
		return line.substring(awsMatcher.getLinePattern().length(), line.length());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTotalCost() {
		String totalCost = null;

		String query = Constants.AWS_FROM_EMAIL_PATTERN + " " + Constants.AWS_DATE_FILTER;
		try {
			List<Message> messages = listMessagesMatchingQuery(query);
			Iterator<Message> messageIterator = messages.iterator();

			log.info("Total matching email messages: " + messages.size());
			if (messageIterator.hasNext()) {
				// check only the first matching message
				Message message = messageIterator.next();
				message = getMessage(message.getId());
				log.debug(message.toPrettyString());

				File attachment = getAttachment(message.getId());
				log.debug("Attachment location: " + attachment.getAbsolutePath());
				File attachmentText = PDFToText.parsePdf(attachment);
				totalCost = getTotal(awsMatcher, attachmentText);
			}

		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}

		return totalCost;
	}

}
