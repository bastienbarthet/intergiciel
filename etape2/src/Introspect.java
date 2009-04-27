// Time-stamp: <14 Dec 2005 08:39 queinnec@enseeiht.fr>

import java.lang.reflect.*;

public class Introspect {

    public static void main (String[] args) {
        if (args.length != 1) {
            System.err.println("Missing class or interface name.");
            return;
        }

        String classname = args[0];
        Class classe;
        try {
            classe = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            System.err.println ("Class "+classname+" not found.");
            return;
        }

        // Regardons un peu ce qu'il y a dans cette classe/interface.
        if (classe.isInterface()) {
            System.out.println (classname+" est une interface.");
        } else {
            System.out.println (classname+" n'est pas une interface.");
        }
                
        Method[] methodes = classe.getMethods();

        for (int i = 0; i < methodes.length; i++) {
            Method meth = methodes[i];
            System.out.println ("Methode: "+meth.toString());
            System.out.println ("  nom = "+meth.getName());
            System.out.println ("  return type = "+meth.getReturnType().getName());
            Class[] params = meth.getParameterTypes();
            for (int j = 0; j < params.length; j++) {
                System.out.println ("  param "+j+" = "+params[j].getName());
            }
        }
        
        // Essayons de créer une instance
        System.out.println();
        System.out.println("Création d'une instance.");
        Object newobj = null;
        try {
            newobj = classe.newInstance();
        } catch (InstantiationException e) {
            System.err.println("Echec instanciation:"+e);
        } catch (IllegalAccessException e) {
            System.err.println("Echec instanciation:"+e);
        }
        if (newobj != null) {
            // appelons la méthode "toString" (qui existe pour tout objet) de deux manières différentes:
            // - normalement
            // - par réflexion
            String result1 = newobj.toString();
            System.out.println("Appel direct à toString : "+result1);
            
            try {
                Method methToString = classe.getMethod("toString", null);
                String result2 = (String) methToString.invoke(newobj, null);
                System.out.println("Appel par réflexivité à toString : "+result2);
            } catch (Exception e) {
                System.err.println ("Echec appel à toString:"+e);
            }
            
        }
    }
}
