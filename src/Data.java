import java.io.Serializable;

public class Data implements Serializable{

	private static final long serialVersionUID = 1L;
	
	int minutes;
	boolean autoStart;
	boolean ignore;
	boolean confirm;
	
	Data(int minutes, boolean autoStart, boolean ignore, boolean confirm){
		this.minutes = minutes;
		this.autoStart = autoStart;
		this.ignore = ignore;
		this.confirm = confirm;
	}
	
	Data(){
		minutes = 30;
		autoStart = true;
		ignore = false;
		confirm = true;
	}
}

