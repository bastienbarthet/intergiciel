
import java.rmi.RemoteException;
import java.util.Random;

/**
 *
 * @author rvlander
 */
public class Incrementer{
	SharedObject entier;
        int apport = 0;
	int nbc;
        Random rr;

	/** Creates new form Ircv2 
	 * @throws RemoteException */
	public Incrementer(int nbcalc) throws RemoteException {

		//Disable verbose mod for Shared Object.
		//SharedObject.verbose = false;

		//Initialisation de la couche client
		Client.init();

        	nbc = nbcalc;

	    	entier = Client.lookup("int");
        	if(entier == null){
            		entier = Client.create(new Entier());
            		Client.register("int", entier);
       		}

        	rr = new Random(Math.round(Math.random()*456));

        	this.mouline();
		this.affiche();

	}

	private void mouline() {
		for(int i=0;i<this.nbc;i++){
			int op1 = rr.nextInt(2);
			switch(op1){
			case 1 :
				entier.lock_write();
				((Entier)(entier.obj)).add(1);
                		this.apport++;
                		entier.unlock();
				break;
			default :
				entier.lock_read();
				((Entier)(entier.obj)).read();
				entier.unlock();
                                break;
			}
		}
		System.out.println("Moulinette Done");
	}

	private void affiche(){
        	System.out.println("Nombres d'inc effectuées : " + this.apport);
		entier.lock_read();
		System.out.println("Valeur dans l'incrémenteur : " + ((Entier)(entier.obj)).read());
		entier.unlock();
		System.exit(0);
    }

	/**
    * @param args the command line arguments
	 * @throws RemoteException 
	 * @throws NumberFormatException 
    */
    public static void main(String args[]) throws NumberFormatException, RemoteException {
        if (args.length != 1) {
			System.out.println("java Incrementer <nb d'opérations>");
			return;
		}
        new Incrementer(Integer.parseInt(args[0]));
        return;
    }


}
