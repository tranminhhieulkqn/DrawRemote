package Object;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4825584721687678183L;
	private String name = "Hieu";
	public User(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
	public String getName() {
		return (this.name == null) ? "Ko co gi" : this.name;
	}
	public void setName(String name) {
		if(this.name == null) {
			System.out.print("Da set ten");
			this.name = name;
		}
	}
}
