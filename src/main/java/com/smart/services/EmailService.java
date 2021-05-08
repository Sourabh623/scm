package com.smart.services;

import java.util.Properties;
import org.springframework.stereotype.Service;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {	
public boolean sendEmail(String subject, String message, String to) {
		
		boolean f=false;
	    String from="sourabhfulmali623@gmail.com";
		//variable for gmail host
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties properties=System.getProperties();
		System.out.println(properties);
		
		//setting important details for properties object
		//host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		//step 1: to get the session object
		Session session=Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication("sourabhfulmali623@gmail.com","sobhu623@");
			}
			
		});

		session.setDebug(true);
		
		//step 2: compose the message [text, multi media]
		MimeMessage mimeMessage = new MimeMessage(session);
		
		try {
			
			//set email
			mimeMessage.setFrom(from);
			
			//adding recipient to the message
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			//adding subject to message
			mimeMessage.setSubject(subject);
			
			//set text to message
			//mimeMessage.setText(message);
			mimeMessage.setContent(message, "text/html");
			
			//send
			//step 3: send the message using transport class
			Transport.send(mimeMessage);
			
			System.out.println("sent message.......");
			f=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f;

    }

public boolean contactSend(String subject, String message, String from) {
	
	boolean f=false;
    String to="sourabhfulmali623@gmail.com";
	//variable for gmail host
	String host="smtp.gmail.com";
	
	//get the system properties
	Properties properties=System.getProperties();
	System.out.println(properties);
	
	//setting important details for properties object
	//host set
	properties.put("mail.smtp.host", host);
	properties.put("mail.smtp.port", "465");
	properties.put("mail.smtp.ssl.enable", "true");
	properties.put("mail.smtp.auth", "true");
	
	//step 1: to get the session object
	Session session=Session.getInstance(properties, new Authenticator() {

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			
			return new PasswordAuthentication("sourabhfulmali623@gmail.com","sobhu623@");
		}
		
	});

	session.setDebug(true);
	
	//step 2: compose the message [text, multi media]
	MimeMessage mimeMessage = new MimeMessage(session);
	
	try {
		
		//set email
		mimeMessage.setFrom(from);
		
		//adding recipient to the message
		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//adding subject to message
		mimeMessage.setSubject(subject);
		
		//set text to message
		//mimeMessage.setText(message);
		mimeMessage.setContent(message, "text/html");
		
		//send
		//step 3: send the message using transport class
		Transport.send(mimeMessage);
		
		System.out.println("sent message.......");
		f=true;
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return f;

}
}