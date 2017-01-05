package ossIndex;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class OssSerializationHelper {

	public static void serialize(ArrayList<BugTraqMail> btMails) {
		try {
			FileOutputStream fos = new FileOutputStream("ossmail.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for (int j = 0; j < btMails.size(); j++) {

				oos.writeObject(btMails.get(j));

			}
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<BugTraqMail> deSerialize(String absPath) {
		ArrayList<BugTraqMail> btMails = new ArrayList<BugTraqMail>();

		try {
			FileInputStream fis = new FileInputStream(absPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object object = ois.readObject();
			btMails.add((BugTraqMail) object);
			while (object != null) {
				object = ois.readObject();
				btMails.add((BugTraqMail) object);
			}
			fis.close();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException ex) {

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return btMails;

	}
	
	
}
