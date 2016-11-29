import java.util.TimerTask;

public class ScheduledTask extends TimerTask{
	
	private Runnable r;
	
	@Override
	public void run(){
		r.run();
	}
	
	ScheduledTask(Runnable r){
		this.r = r;
	}
}
