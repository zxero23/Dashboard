package org.ccpm.dashboard.utilities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MailThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(MailThread.class);
	private String emisor;
	private String destinatario;
	private String asunto;
	private String mensaje;
	private List<File> adjuntos;
	private String host;
	private Integer port;
	private String email;
	private String password;

	@Override
	public void run() {
		try {
			logger.info("Starts mail thread." + this.getHost());
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.trust", this.getHost());
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", this.getHost());
			props.put("mail.smtp.port", this.getPort());
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(email, password);
				}
			});
			this.sendMessage(session);
			logger.info("Finish mail thread successfully.");

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	private void sendMessage(Session session) {
		try {
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(this.getEmail(), emisor));
			InternetAddress[] internetAddressesTo = { new InternetAddress(this.getDestinatario()) };
			mimeMessage.setRecipients(Message.RecipientType.TO, internetAddressesTo);
			mimeMessage.setText(this.getMensaje());
			mimeMessage.setSubject(this.getAsunto());
			Multipart multipart = new MimeMultipart();
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(mensaje, "UTF-8", "html");
			multipart.addBodyPart(mimeBodyPart);
			if (!this.getAdjuntos().isEmpty()) {
				List<MimeBodyPart> attachmentsMimeBodyPart = new LinkedList<>();
				for (File file : this.adjuntos) {
					MimeBodyPart mimeBodyPartAttachment = new MimeBodyPart();
					mimeBodyPartAttachment.setDataHandler(new DataHandler(new FileDataSource(file)));
					mimeBodyPartAttachment.setFileName(file.getName());
					attachmentsMimeBodyPart.add(mimeBodyPartAttachment);
				}
				for (MimeBodyPart attach : attachmentsMimeBodyPart) {
					multipart.addBodyPart(attach);
				}
			}
			mimeMessage.setContent(multipart);
			Transport transport = session.getTransport("smtp");
			transport.connect(this.getEmail(), this.getPassword());
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
		} catch (UnsupportedEncodingException | MessagingException e) {
			logger.error(e.getMessage());
		}
	}

}
