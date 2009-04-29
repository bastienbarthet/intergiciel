import java.rmi.RemoteException;
import java.util.Random;


public class Moulinette {

	public static void main(String[] args) throws InterruptedException, RemoteException {

		Random rdm = new Random();
		
		if(args.length != 1) {
			System.out.println("java Moulinette <Name>");
			return;
		}
		String name = args[0];

		// initialize the system
		Client.init();

		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = (SharedObject) Client.lookup("IRC");
		if (s == null) {
			s = (SharedObject) Client.create(new Sentence());
			Client.register("IRC", s);
		}
		
		while(true){
			
//			Thread.sleep(100);

			if(rdm.nextBoolean()) {
				// We read
				s.lock_read();
				
				System.out.println("Read :" + ((Sentence)(s.obj)).read());
				
				s.unlock();

			} else {
				// We write

				s.lock_write();

				// invoke the method
				((Sentence)(s.obj)).write(name);
				if(s.obj == null) System.out.println("ça va planter en write");

				// unlock the object
				s.unlock();
			}
		}

	}

}
