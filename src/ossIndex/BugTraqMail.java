package ossIndex;

import java.io.Serializable;
import java.util.Date;

public class BugTraqMail implements Serializable {
	private static final long serialVersionUID = 1L;
	public String subject;
	public String msgid;
        public String from;
	public Date date;
	public String body = "";
	
	public BugTraqMail(String subject, String msgId, String from, Date receivedDate, String body){
		
		this.subject = subject;
		this.msgid = msgId;
		this.from = from;
		this.date = receivedDate;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	

}
