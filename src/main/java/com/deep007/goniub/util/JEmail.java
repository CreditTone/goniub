package com.deep007.goniub.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JEmail {
	
	private String smtpHost;
	private String smtpPort;
	private String fromMail;
	private String username;
	private String password;
	private String title;
	private boolean ssl;
	private boolean tls;
	private boolean auth = true;
	private String content;
	private String toMails;
	
	public static class JEmailBuilder {
		
		private StringBuilder contentBuilder = new StringBuilder();
		private String toMails = "";
		
		public JEmailBuilder addContentLine(String line) {
			if (!Strings.isValidString(line)) {
				return this;
			}
			contentBuilder.append(line).append("\r\n").append("</br>");
			content = contentBuilder.toString();
			return this;
		}
		
		public JEmailBuilder addToMail(String mailAddr) {
			if (toMails.isEmpty()) {
				toMails += mailAddr;
			}else {
				toMails += "," + mailAddr;
			}
			return this;
		}
		
	}

	/**
	 * 
	 * @param fromMail 发件人的地址
	 * @param user 
	 * @param password
	 * @param mailTitle
	 * @param mailContent
	 * @param toMails  收件人地址，可以是多个 
	 * @throws Exception
	 */
	public void send() {
		if (toMails == null || toMails.length() == 0) {
			return;
		}
		// 加载一个配置文件
		Properties props = new Properties();

		// smtp：简单邮件传输协议
		// 设置邮件服务器名
		if (smtpHost == null) {
			props.put("mail.smtp.host", "smtp.163.com");
		}else {
			props.put("mail.smtp.host", smtpHost);
		}
		if (smtpPort == null) {
			smtpPort = "25";
		}
		props.put("mail.smtp.port", smtpPort);
		if (auth) {
			if (ssl) {
				props.put("mail.smtps.auth", "true");
			}else {
				props.put("mail.smtp.auth", "true");
			}
		}else {
			props.put("mail.smtp.auth", "false");
		}
		props.put("mail.transport.protocol", "smtp");
		if (ssl) {
			 props.put("mail.smtp.port", smtpPort);
             props.put("mail.smtp.socketFactory.port", smtpPort);
             props.put("mail.smtp.socketFactory.class", "play.utils.YesSSLSocketFactory");
             props.put("mail.smtp.socketFactory.fallback", "false");
		}else if (tls) {
			props.put("mail.smtp.starttls.enable", "true");
		}
        //props.put("mail.imap.auth.plain.disable","true");
		// 设置环境信息
		Session session = null;
		if (auth) {
			session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
		}else {
			session = Session.getInstance(props);
		}
		session.setDebug(false);
		// 由邮件会话新建一个消息对象
		MimeMessage message = new MimeMessage(session);
		try {
			// 设置邮件内容
			message.setFrom(new InternetAddress(fromMail));// 设置发件人的地址
			InternetAddress[] addresses = InternetAddress.parse(toMails);
			message.setRecipients(Message.RecipientType.TO, addresses);// 设置收件人,并设置其接收类型为TO
			message.setSubject(title);// 设置标题
			
			// 设置信件内容
	        //message.setText(content); //发送 纯文本 邮件 todo
			message.setContent(content, "text/html;charset=utf-8"); // 发送HTML邮件，内容样式比较丰富
			message.setSentDate(new Date());// 设置发信时间
			message.saveChanges();// 存储邮件信息
			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
