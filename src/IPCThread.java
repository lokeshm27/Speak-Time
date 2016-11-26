import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IPCThread extends Thread {

	final Socket socket;
	BufferedReader br;
	PrintWriter pw;

	IPCThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			String inputLine;
			if((inputLine = br.readLine()) != null) {
				processMsg(inputLine);
				socket.close();
				return;
			}
		} catch (Exception e) {
			
		}
	}

	private void processMsg(String inputLine) {
		// TODO Auto-generated method stub
		System.out.println("Server : " + inputLine);
	}

}
