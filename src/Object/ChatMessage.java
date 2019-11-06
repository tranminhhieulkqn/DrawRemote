package Object;
import java.io.*;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	private String message;
	
	// constructor
	public ChatMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
