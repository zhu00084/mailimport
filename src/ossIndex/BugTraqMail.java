package ossIndex;

import java.io.Serializable;
import java.util.Date;

public class BugTraqMail implements Serializable {
	private static final long serialVersionUID = 1L;
	private String subject;
	private String msgid;
	private String from;
	private Date receivedDate;
	private String body = "";
	/**
	 * Stores message index in the Inbox starting from 1.
	 * This index will be saved in a non-unique column in database. 
	 * The deletion of any mail in the Inbox will cause duplicate number in this column.  
	 */
	private int mailIndex;
	
	

	public BugTraqMail(String subject, String msgId, String from, Date receivedDate, String body){
		
		this.subject = subject;
		this.msgid = msgId;
		this.from = from;
		this.receivedDate = receivedDate;
		this.body = body;
		
		
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date date) {
		this.receivedDate = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public int getMailIndex() {
		return mailIndex;
	}

	public void setMailIndex(int mailIndex) {
		this.mailIndex = mailIndex;
	}
	

}
