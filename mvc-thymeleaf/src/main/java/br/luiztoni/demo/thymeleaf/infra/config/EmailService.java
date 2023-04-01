package br.luiztoni.demo.thymeleaf.infra.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender emailSender;
	
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);


    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply@baeldung.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
    
    public void sendMessage(String to, String subject, String text, String pathToAttachment) {
        MimeMessage message = emailSender.createMimeMessage();      
        MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom("noreply@baeldung.com");
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(text);
	            
	        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
	        helper.addAttachment("Invoice", file);
	        emailSender.send(message);

		} catch (MessagingException e) {
			log.info("Exception in EmailService, cause {}", e.getMessage(), e);
		}
        
    }

}
