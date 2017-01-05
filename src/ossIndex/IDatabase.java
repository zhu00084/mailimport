package ossIndex;

import java.util.ArrayList;

public interface IDatabase {
	
	boolean ImportMail(ArrayList<BugTraqMail> mails);
	
	int GetNextMailIndex() throws OssNoConnectionException;
	
	boolean UpdateNextMailIndex(int nextMailIndex) throws OssNoConnectionException;

}
