
public class startingPoint {
	
	public static void main(String args[]) {
		Operations op = new Operations();
		if(op.isWorking()){
			System.exit(0);
		}else{
			op.init();
			if(args.length>=1){
				if(args[0].equals("-autostart") && !op.data.autoStart){
					op.onClose();
				}
			}
			op.initTray();
			op.startServer();
			op.startOperation();
		}
	}

}