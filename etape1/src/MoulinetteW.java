import java.rmi.RemoteException;


public class MoulinetteW {

	public static void main(String[] argv) throws RemoteException{
		Client.init();
		SharedObject truc = Client.lookup("machin");
		if (truc == null) {
			truc = Client.create(new Sentence());
			Client.register("machin", truc);
		}
		int instNum = 0;
		while(true){
			truc.lock_write();
			System.out.println("Ecriture n�"+instNum);
			((Sentence)truc.getObject()).write("Ecriture n�"+instNum);
			instNum++;
			truc.unlock();
		}
	}

}
