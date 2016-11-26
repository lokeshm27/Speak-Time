import java.awt.TrayIcon;
import java.util.Calendar;
import java.util.Date;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class speakThread extends Thread {

	private boolean enable = true, exit = false;
	private String VOICENAME = "kevin16";
	private Voice voice;
	private VoiceManager vm;
	
	@Override
	public void run() {
		while (true) {
			System.out.println("Waiting");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	speakThread(){
		vm = VoiceManager.getInstance();
		voice = vm.getVoice(VOICENAME);
		voice.allocate();
	}
	
	@SuppressWarnings("deprecation")
	public void test(){
		Date date = new Date();
		System.out.println("It's " + date.getHours() + " Hours and " + date.getMinutes() + " Minutes.");
		voice.speak("It's " + date.getHours() + " Hours and " + date.getMinutes() + " Minutes.");
	}
}
