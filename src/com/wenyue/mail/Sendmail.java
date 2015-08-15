package com.wenyue.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;

import com.wenyue.domain.User;
import com.wenyue.download.HtmlResult;
import com.wenyue.utils.DeletTxt;


public class Sendmail extends Thread {

	private String from = "wswenyue@126.com";
	private String username = "wswenyue";
	private String password = "294148946";
	private String host = "smtp.126.com";
	
	private User user; 
	private HtmlResult result;
	public Sendmail(User user, HtmlResult result){
		this.user = user;
		this.result = result;
	}
	
	@Override
	public void run() {
		try{
			Properties prop = new Properties();
			prop.setProperty("mail.host", host);
			prop.setProperty("mail.transport.protocol", "smtp");
			prop.setProperty("mail.smtp.auth", "true");
			
			Session session = Session.getInstance(prop);
			
			
			//session 由web服务器创建     JNDI技术
//			Context initCtx = new InitialContext();
//			Context envCtx = (Context) initCtx.lookup("java:comp/env");
//			Session session = (Session) envCtx.lookup("mail/Session");  //Session s = session

			
			//			session.setDebug(true);

			
			Transport ts = session.getTransport();
			ts.connect(host, username, password);
			MailData md = new MailData(result);
			Message message = md.makeMessage(session, user, from); 
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();
			
			System.out.println("邮件发送成功，应删除文件"+ result.getTitle());
			
			if(DeletTxt.doDelet(user.getTxtPath())){
				System.out.println("删除文件成功");
			}
			
			
		}catch (Exception e) {
			System.out.println("邮件发送失败");
			throw new RuntimeException(e);
		}
		
		
	}
	
	
	
}
