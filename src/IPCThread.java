import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IPCThread extends Thread {

	final Socket socket;
	BufferedReader br;
	PrintWriter pw;
	Operations op = new Operations();

	IPCThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			String inputLine;
			if((inputLine = br.readLine()) != null) {
				String opLine = processMsg(inputLine);
				if(opLine != null){
					pw.println(opLine);
					pw.flush();
				}
				socket.close();
				return;
			}
		} catch (Exception e) {
			
		}
	}

	private String processMsg(String inputLine) {
		System.out.println("Server : " + inputLine);
		switch(inputLine){
			case "Confirm" : 	op.showTrayMsg("Speak-Time is already running.\nClick this bubble to open speak-time.", "Speak-Time", TrayIcon.MessageType.WARNING);
								return "Affirmative";
		
		
		}
		return null;
	}

}
