import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;



public class GenerateurDeStub {

	public static void main(String[] args) {
		
			try {
				genererStub(Class.forName(args[0]));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	
	}

	private static void genererStub(Class classe) {
		String code;
		String nomClasse = classe.getSimpleName();

		// L'entete
		code = "public class " + classe.getSimpleName() + "_stub extends SharedObject implements java.io.Serializable { \n \n";
	
		code += "	private static final long serialVersionUID = 3173827029004946005L; \n\n";
		
		// Le constructeur
		code += "	public " + nomClasse + "_stub (int id, Object o){ \n		super(id, o) \n	} \n \n";
		
		//tableau des méthodes de la classe
		Method[] methodes = classe.getDeclaredMethods();
		
		for (int i = 0; i < methodes.length; i++) {
	            Method meth = methodes[i];
	            String nom = meth.getName();
	            boolean estPublic = Modifier.isPublic(meth.getModifiers());
	            String typeRetour = meth.getReturnType().getName();
	            Class[] params = meth.getParameterTypes();
	            //params[j].getName();
	            
	         code += "	public " + nom +" "+ typeRetour + "(";

	         for(int j=0; j<params.length; j++ ){
	        	 String typeAttribut = params[j].getSimpleName();
	        	 code += typeAttribut +" "+ params[j]+", ";	    	 
	        	 }
	         
	         code += "){ \n\n";
      //corp de la méthode
	         
	         
	// fin de la méthode 	         
	code +="\n	}\n\n";
		}

		
		
		code +="\n\n}\n";

		try {		
		File fichierStub = new File(nomClasse + "_stub.java");
		fichierStub.createNewFile();
		FileOutputStream fileOS = new FileOutputStream(fichierStub);
		fileOS.write(code.getBytes());
		fileOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	
	
	}
	
	
	
}
