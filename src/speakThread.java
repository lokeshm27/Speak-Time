
public class speakThread extends Thread {

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
}
