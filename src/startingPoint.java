public class startingPoint {
	
	public static void main(String args[]) {
		
		Operations op = new Operations();
		if(op.isWorking()){
			////TODO implement pass arguments
			op.passMessage("Overlap");
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
			
		}
		
	}

}