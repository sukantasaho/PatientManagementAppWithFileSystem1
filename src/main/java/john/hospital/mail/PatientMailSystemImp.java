package john.hospital.mail;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class PatientMailSystemImp implements IPatientMailSystem 
{
    
	 @Autowired
     private JavaMailSender sender;
	 
	 @Value("${spring.mail.username}")
	 private String fromEmail;
	 
	@Override
	public  String patientCreationMailTriggering(String msg, String[] toEmails) 
	{
		      //create the email message
				MimeMessage message = sender.createMimeMessage();
				MimeMessageHelper helper;
				try 
				{
					helper = new MimeMessageHelper(message, true);
					helper.setFrom(fromEmail);
					helper.setTo(toEmails);
					helper.setSubject("Open it and know it");
					helper.setSentDate(new Date());
					helper.setText(msg);
					helper.addAttachment("batch1.jpg", new ClassPathResource("batch1.jpg"));
					sender.send(message);
				} 
				catch (MessagingException e) 
				{
					 
					e.printStackTrace();
				}
				
				
				return "Patient Mail Sent Successfully Check Your Email";
	}

}
