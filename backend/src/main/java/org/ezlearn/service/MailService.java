package org.ezlearn.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	@Autowired
    private JavaMailSender mailSender;
	
	public void sendSimpleHtml(Collection<String> receivers, String subject, String content) {
		 try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message);
	            helper.setTo(receivers.toArray(new String[0]));
	            helper.setFrom("EZLEARN Support<dcnohime@gmail.com>");
	            helper.setSubject(subject);
	            helper.setText(content, true);

	            mailSender.send(message);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
    }
}
