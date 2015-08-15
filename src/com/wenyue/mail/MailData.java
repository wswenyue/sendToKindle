package com.wenyue.mail;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import com.wenyue.domain.User;
import com.wenyue.download.HtmlResult;

public class MailData {
	
	private HtmlResult result;
	public MailData(HtmlResult result){
		this.result = result;
	}

	public Message makeMessage(Session session, User user, String from)
			throws Exception {

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(Message.RecipientType.TO,
				new InternetAddress(user.getEmail()));
		message.setSubject("convert");

		// 创建邮件正文
//		MimeBodyPart text = new MimeBodyPart();
//		text.setContent("aaaaaaaaaaa", "text/html");

		// 创建邮件附件
		MimeBodyPart attach = new MimeBodyPart();
		
		String path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
		String[] s =path.split("classes");
		String p = s[0];
		
		//获取附件的路径，这个通过servlet传进来
		String Txtpath = p+"out/"+result.getTitle()+".txt";
		FileDataSource fd= new FileDataSource(Txtpath);
		System.out.println(result.getTitle());
		
		user.setTxtPath(Txtpath);

		DataHandler dh = new DataHandler(fd);
		attach.setDataHandler(dh);
		attach.setFileName(MimeUtility.encodeText(dh.getName())); //

		
		// 创建容器描述数据关系
		MimeMultipart mp = new MimeMultipart();
//		mp.addBodyPart(text);
		mp.addBodyPart(attach);
//		mp.setSubType("mixed");

		message.setContent(mp);
		message.saveChanges();
		
		System.out.println("邮件创建完毕");
		return message;
	}

}
