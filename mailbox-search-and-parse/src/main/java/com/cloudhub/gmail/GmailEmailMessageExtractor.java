/*
 * Copyright (c) 2016 http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.cloudhub.gmail;

import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.cloudhub.Options;
import com.cloudhub.extractor.EmailMessageExtractor;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;


/**
 * <p>Extracts <i>actual</i> billing information from the invoice emailed by the specified provider.</p>
 *
 * @author sah2ed
 * @version 1.0
 */
public abstract class GmailEmailMessageExtractor extends EmailMessageExtractor {

	/**
	 * Authorized Gmail API instance.
	 */
	private Gmail service;

	/**
	 *
	 * User's email address. The special value "me"
	 * can be used to indicate the authenticated user.
	 */
	private String userId;

	/**
	 * @param service Authorized Gmail API instance.
	 */
	public GmailEmailMessageExtractor(Gmail service, String userId) {
		this.service = service;
		this.userId = userId;
	}

	/**
	 * List all Messages of the user's mailbox matching the query.
	 *
	 * @param query String used to filter the Messages listed.
	 * @throws IOException
	 */
	public List<Message> listMessagesMatchingQuery(String query) throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setQ(query)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

//		for (Message message : messages) {
//			System.out.println(message.toPrettyString());
//		}

		return messages;
	}

	/**
	 * List all Messages of the user's mailbox with labelIds applied.
	 *
	 * @param labelIds Only return Messages with these labelIds applied.
	 * @throws IOException
	 */
	public List<Message> listMessagesWithLabels(List<String> labelIds) throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId)
				.setLabelIds(labelIds).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setLabelIds(labelIds)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		for (Message message : messages) {
			System.out.println(message.toPrettyString());
		}

		return messages;
	}

	/**
	 *
	 * Prints all the labels in the user's inbox.
	 * @throws IOException
	 */
	public void printLabels() throws IOException {
        // Print the labels in the user's account.
        ListLabelsResponse listResponse =
            service.users().labels().list(userId).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.size() == 0) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
	}

	/**
	 * Get Message with given ID.
	 *
	 * @param messageId ID of Message to retrieve.
	 * @return Message Retrieved Message.
	 * @throws IOException
	 */
	public Message getMessage(String messageId)
			throws IOException {
		Message message = service.users().messages().get(userId, messageId).execute();

		// System.out.println("Message snippet: " + message.getSnippet());

		return message;
	}

	/**
	 * Get a Message and use it to create a MimeMessage.
	 *
	 * @param messageId ID of Message to retrieve.
	 * @return MimeMessage MimeMessage populated from retrieved Message.
	 * @throws IOException
	 * @throws MessagingException
	 */
	public MimeMessage getMimeMessage(String messageId)
			throws IOException, MessagingException {
		Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();
		// System.out.println(message.toPrettyString());

		byte[] emailBytes = Base64.decodeBase64(message.getRaw());

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

		return email;
	}

	/**
	 * Get the attachment in a given email.
	 *
	 * @param messageId ID of Message containing attachment..
	 * @throws IOException
	 */
	public File getAttachment(String messageId) throws IOException {
		File attachment = null;

		Message message = service.users().messages().get(userId, messageId).execute();
		List<MessagePart> parts = message.getPayload().getParts();
		for (MessagePart part : parts) {
			if (part.getFilename() != null && part.getFilename().length() > 0) {
				String filename = part.getFilename();
				String attId = part.getBody().getAttachmentId();
				MessagePartBody attachPart = service.users().messages().attachments().
						get(userId, messageId, attId).execute();
				byte[] fileByteArray = Base64.decodeBase64(attachPart.getData());

				attachment = new File(Options.DATA_FOLDER, filename);
				FileOutputStream fileOutFile =
						new FileOutputStream(attachment);
				fileOutFile.write(fileByteArray);
				fileOutFile.close();

				break;
			}
		}

		return attachment;
	}

}