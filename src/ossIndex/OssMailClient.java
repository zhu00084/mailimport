package ossIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OssMailClient {

	private String host;
	private int port;
	private String ssl;
	private String mailbox;
	private String password;
	
	private int nextMailIndex; 

	

	private final Logger slf4jLogger = LoggerFactory.getLogger(OssMailClient.class);

	public OssMailClient(String host, int port, String ssl, String mailbox, String pass) {
		setHost(host);
		setPort(port);
		setSsl(ssl);
		setMailbox(mailbox);
		setPassword(pass);

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSsl() {
		return ssl;
	}

	public void setSsl(String ssl) {
		this.ssl = ssl;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getNextMailIndex() {
		return nextMailIndex;
	}

	public void setNextMailIndex(int nextMailIndex) {
		this.nextMailIndex = nextMailIndex;
	}
	
	public ArrayList<BugTraqMail> getMail(int nextMailIndex) {

		ArrayList<BugTraqMail> mails = new ArrayList<BugTraqMail>();

		Properties props = new Properties();
		props.put("mail.imaps.host", getHost());
		props.put("mail.imaps.port", getPort());
		props.put("mail.imap.ssl.enable", getSsl());
		props.put("mail.imaps.auth", "true");
		props.put("mail.imaps.ssl.trust", "*");

		try {
			Session session = Session.getDefaultInstance(props);
			Store store = session.getStore("imaps");
			slf4jLogger.info("Connecting to {}", getMailbox() + "@" + getHost());

			store.connect(getMailbox(), getPassword());
			slf4jLogger.info("Connected to {}", store);

			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_ONLY);

			int totalMessages = inbox.getMessageCount();
			slf4jLogger.info("Total Message: {}" , totalMessages);
			slf4jLogger.info("Start read email from {}", nextMailIndex);
		
			boolean success = readAllMails(inbox, mails,totalMessages, nextMailIndex);
						
			if (!success)
			{
				slf4jLogger.info("Could not read all emails successfully.");
				slf4jLogger.info("Stop importing emails.");
				System.exit(0);
			}
			
			this.setNextMailIndex(totalMessages+1);

		} catch (NoSuchProviderException e) {
			slf4jLogger.error(e.toString());
			System.exit(1);
		} catch (MessagingException e) {
			slf4jLogger.error(e.toString());
			System.exit(2);

		}

		slf4jLogger.info("Read total {} mails.", mails.size());
		return mails;

	}

	private static void showUnreadMails(Folder inbox) {
		try {
			FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			Message msg[] = inbox.search(ft);
			System.out.println("MAILS: " + msg.length);
			for (Message message : msg) {
				try {
					System.out.println("DATE: " + message.getSentDate().toString());
					System.out.println("FROM: " + message.getFrom()[0].toString());
					System.out.println("SUBJECT: " + message.getSubject().toString());
					System.out.println("CONTENT: " + message.getContent().toString());
					System.out.println("-------------------------------------------");
				} catch (Exception e) {
					System.out.println("No Information");
				}
			}
		} catch (MessagingException e) {
			System.out.println(e.toString());
		}
	}

	private static void showAllMails(Folder inbox, List<BugTraqMail> mails) {
		try {
			Message msg[] = inbox.getMessages(1, 3037);
			int msgCount = inbox.getMessageCount();
			// System.out.println("MAILS: "+msg.length);

			int i = 0;
			for (Message message : msg) {
				// for(int i = 0; i< 10; i++) {
				try {
					// Message message =
					// inbox.getMessage(i);
					MimeMessage mmsg = (MimeMessage) message;
					BugTraqMail mail = new BugTraqMail(mmsg.getSubject().toString(),
							mmsg.getMessageID().toString(), mmsg.getFrom()[0].toString(),
							mmsg.getReceivedDate(), mmsg.getContent().toString());

					mails.add(mail);
					// System.out.println("Message Number" +
					// mmsg.getMessageID());
					// System.out.println("DATE:
					// "+message.getSentDate().toString());
					// System.out.println("FROM:
					// "+message.getFrom()[0].toString());
					// System.out.println("SUBJECT:
					// "+message.getSubject().toString());
					// System.out.println("CONTENT:
					// "+message.getContent().toString());
					// System.out.println("------------------------------------------");
					// if (i>5)
					// break;
					i++;
					System.out.printf("current mail %s", i);
					System.out.println("");
				} catch (Exception e) {
					System.out.println("No Information");
				}
			}
		} catch (MessagingException e) {
			System.out.println(e.toString());
		}
	}

	private boolean readAllMails(Folder inbox, List<BugTraqMail> mails, int totalMessages, int nextMailIndex) 
	{
		try {
			//no new messages in the box
			if (nextMailIndex > totalMessages)
				return true;
			
			Message msg[] = inbox.getMessages(nextMailIndex,totalMessages);

			int i = 0;
			for (Message message : msg) {
				try {
					MimeMessage mmsg = (MimeMessage) message;
					BugTraqMail mail = new BugTraqMail(mmsg.getSubject().toString(),
							mmsg.getMessageID().toString(), mmsg.getFrom()[0].toString(),
							mmsg.getReceivedDate(), mmsg.getContent().toString());

					mails.add(mail);
					
					i++;
				} catch (Exception e) {
					slf4jLogger.error("Exception reading message {}", i);
					slf4jLogger.error("Exception message: {}", e.toString());
					return false;
				}
			}
		} catch (MessagingException e) {
			slf4jLogger.error(e.toString());
			return false;
		} catch (Exception e) {
			slf4jLogger.error(e.toString());
			return false;
		}
		
		return true;
	}

}
