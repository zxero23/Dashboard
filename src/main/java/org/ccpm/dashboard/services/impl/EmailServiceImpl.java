package org.ccpm.dashboard.services.impl;

import java.io.File;
import java.util.List;

import org.ccpm.dashboard.services.EmailService;
import org.ccpm.dashboard.utilities.MailThread;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Value("${mails.host}")
	private String host;
	@Value("${mails.port}")
	private Integer port;
	@Value("${mails.email}")
	private String email;
	@Value("${mails.password}")
	private String password;
	@Value("${mails.emisor}")
	private String emisor;

	@Override
	public void enviarEmail(String destinatario,String asunto, String mensaje, List<File> adjuntos) throws Exception {
		MailThread mailThread = new MailThread(emisor,destinatario,asunto,mensaje,adjuntos,host,port,email,password);
		mailThread.run();		
	}

}
