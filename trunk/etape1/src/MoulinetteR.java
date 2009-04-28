import java.rmi.RemoteException;


public class MoulinetteR {

	public static void main(String[] argv) throws RemoteException{
		Client.init();
		SharedObject truc = Client.lookup("machin");
		if (truc == null) {
			truc = Client.create(new Sentence());
			Client.register("machin", truc);
		}
		int instNum = 0;
		while(true){
			
			truc.lock_read();
			String txt = ((Sentence)truc.obj).read();
			truc.unlock();
			
			if(!txt.equals("")){
				System.out.println("Lecture n�"+instNum+": \""+
						txt+"\"");
				instNum++;
			}
			
		}
	}

}
