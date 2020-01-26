package clienttcp;



import java.io.IOException ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.PrintWriter ;
import java.io.IOException ;
import java.net.Socket ;
import java.net.UnknownHostException ;

public class ClientTCP {
    public static void main (String argv []) throws IOException , ArrayIndexOutOfBoundsException  {
        Socket socket = null ;
        PrintWriter flux_sortie = null ;
        BufferedReader flux_entree = null ;
        String chaine ;
        System.out.println("client est lancer:");
        System.out.println("clic <connect> pour connecter au serveur");



        try {
        	if ( argv.length == 0) {
        		try {
            		socket = new Socket ("127.0.0.1", 5000) ;

				} catch (Exception e) {
					System.out.println("il ya un seul ");
				}
        	}
            // deuxieme argument : le numero de port que l'on contacte
        	//String portt =  argv[0];
        	//if (portt.isEmpty() ) {
              //  socket = new Socket ("127.0.0.1", 5000) ;

        	//}
        	else {
            	int port = Integer.parseInt(argv[0]);
            	String ip =  argv[1];

            try {
            	socket = new Socket (ip, port) ;
			} catch (Exception e) {
				System.out.println("il y a un seul");
				// TODO: handle exception
			}

        	}
        	try {
                flux_sortie = new PrintWriter (socket.getOutputStream (), true) ;

			} catch (Exception e) {
				System.out.println("il ya un seul client");
	            System.exit (1) ;

			}
            flux_entree = new BufferedReader (new InputStreamReader (
                                        socket.getInputStream ())) ;

        //    socket.setSoTimeout(2000);

       }
        catch (UnknownHostException e) {
           // System.err.println ("Hote inconnu") ;
        	System.out.println("il ya un seul client");
            System.exit (1) ;
        }

        BufferedReader entree_standard = new BufferedReader ( new InputStreamReader ( System.in)) ;

	// L'entree standard

		long start = System.currentTimeMillis();



        //try ta3i
        try {


	do {
		// on lit ce que l'utilisateur a tape sur l'entree standard

		chaine = entree_standard.readLine () ;


		// et on l'envoie au serveur
		flux_sortie.println (chaine) ;

		// on lit ce qu'a envoye le serveur
		chaine = flux_entree.readLine () ;

		// et on l'affiche a l'ecran
		System.out.println ("Le serveur m'a repondu -------- " + chaine) ;


		//long elapsedTimeMillis = System.currentTimeMillis()-start;

	     // Get elapsed time in seconds
	    // float elapsedTimeSec = elapsedTimeMillis/1000F;
	  //   System.out.println("raniiiiiiiiiiiiii hnaaaaaaaaaaaaaaaaaaaaa"+elapsedTimeSec);



		 } while (chaine != null) ;
        } catch (Exception e) {
	    	//hna lazm

        		System.out.println(" serveur sortie");
            	System.exit (1) ;

		}



	flux_sortie.close () ;
    flux_entree.close () ;
    entree_standard.close () ;
    socket.close () ;








    }
}
