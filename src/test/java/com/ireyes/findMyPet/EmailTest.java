package com.ireyes.findMyPet;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ireyes.findMyPet.model.email.EmailService;

@SpringBootTest
public class EmailTest {
	@Autowired
	EmailService emailService;
	
	@Test
	public void sendSimpleEmailTest() {
		assertThatNoException().isThrownBy(() -> emailService.
				sendEmail("findmypetcomm@gmail.com", "Test", "This is a test email"));
		
	}
	
	@Test
	public void sendEmailWithAttachmentTest() throws IOException {
		FileWriter writter = new FileWriter("test.txt");
		writter.write("This is a text file");
		writter.close();
		File file = new File("test.txt");
		List<String> filepaths = new ArrayList<>();
		filepaths.add(file.getAbsolutePath());
		assertThatNoException().isThrownBy(
				() -> emailService.sendEmailWithAttachment("findmypetcomm@gmail.com", "Test email with attachment",
				"This email has a text file attached", filepaths));
		file.deleteOnExit();
	}
}
