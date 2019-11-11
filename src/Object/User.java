package Object;

import java.io.Serializable;

/**
 * @author MinhHieu
 * Class used to store user information - to expands
 * Converted to Serializable Object
 * 
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4825584721687678183L;
	private String name = "Hieu";
	
	//Data encapsulation
	public String getName() {
		return (this.name == null) ? "Ko co gi" : this.name;
	}
	public void setName(String name) {
		if(this.name == null) {
			System.out.print("Da set ten");
			this.name = name;
		}
	}
	
	//Constructor
	public User(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
	
}
