import java.util.Date;
import java.util.Timer;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class speakThread extends Thread {

	private boolean enable = true,sched = false;
	private String VOICENAME = "kevin16";
	private Voice voice;
	private VoiceManager vm;
	ScheduledTask task;
	Timer timer;
	Data data;
	
	@Override
	public void run() {
		timer.schedule(task, waitFor(), data.minutes*60*1000);
	}
	
	private boolean audible() {
		return false;
	}
	
	speakThread(Data data){
		this.data = data;
		vm = VoiceManager.getInstance();
		voice = vm.getVoice(VOICENAME);
		voice.allocate();
		task = new ScheduledTask(new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Date date = new Date();
				if(((data.minutes<60) && (date.getMinutes()%data.minutes==0)) || ((data.minutes>=60) && (date.getHours()%(data.minutes/60)==0))){
					if(enable && !audible()){
						if(date.getMinutes()==0){
							voice.speak("It's " + date.getHours() + " hours.");
						}else{
							voice.speak("It's " + date.getHours() + " hours and " + date.getMinutes() + " minutes");
						}
					}
				}else{
					try {
						Thread.sleep(waitFor());
					} catch (InterruptedException e) {
						
					}
				}
			}
			
		});
		timer = new Timer();
	}
	
	@SuppressWarnings("deprecation")
	public void test(){
		Date date = new Date();
		System.out.println("It's " + date.getHours() + " Hours and " + date.getMinutes() + " Minutes.");
		voice.speak("It's " + date.getHours() + " Hours and " + date.getMinutes() + " Minutes.");
	}
	
	public void setData(Data data){
		this.data = data;
	}

	@SuppressWarnings("deprecation")
	public long waitFor(){
		Date date = new Date();
		if(data.minutes<60){
			int mins,secs;
			long msecs;
			mins = data.minutes - (date.getMinutes()%data.minutes) -1;
			secs = 60 - date.getSeconds() -1;
			msecs = 1000 -(System.currentTimeMillis() % 1000);
			System.out.println("Waiting for " + mins + " mins, " + secs + " secs & " + msecs + " msecs.");
			return (mins*60*1000)+(secs*1000)+msecs;
		}else{
			int hrs,mins,secs;
			int hours = data.minutes/60;
			long msecs;
			hrs = hours - (date.getHours()%hours) - 1;
			mins = data.minutes - (date.getMinutes()%data.minutes) -1;
			secs = 60 - date.getSeconds() -1;
			msecs = 1000 -(System.currentTimeMillis() % 1000);
			System.out.println("Waiting for " + hrs + " hours, " + mins + " mins, " + secs + " secs & " + msecs + " msecs.");
			return (hrs*60*60*1000)+(mins*60*1000)+(secs*1000)+msecs;
		}
	}

}
