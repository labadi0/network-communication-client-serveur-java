package serveurtcp;


import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServeurTCP {

	public static void main(String argv[]) throws IOException , Exception {
		ServerSocket serverSocket = null;
		try {

			if (argv.length == 0) {
				serverSocket = new ServerSocket(5000);
			}
			else {
				int port = Integer.parseInt(argv[0]);
				serverSocket = new ServerSocket(port);

			}




			//serverSocket = new ServerSocket(5000);

		} catch (Exception e) {
			System.out.println("il ya seul serveur");
		}



	//	serverSocket.setSoTimeout(2000);
		String chaine;

		String[] tab;
		String x1;
		String x2;
		String id_utili = "";
		String id_chanson = "";
		System.out.println("serveur est Lancer :");
		Socket clientSocket = null;
		ResultSet rs;
		int util_occurs = 1;
		boolean id_utill =  false ;
		boolean chanson_add  =  false ;

		try {
			clientSocket = serverSocket.accept();
		} catch (NullPointerException e) {
			//System.err.println("Accept echoue.");
			System.out.println("il ya un seul serveur");
			System.exit(1);
		}

		PrintWriter flux_sortie = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader flux_entree = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		String chaine_entree, chaine_sortie;

		// flux_sortie.println ("clic <connect> to connect to the serveur ") ;

		try {
			while ((chaine_entree = flux_entree.readLine()) != null) {

				// hna la conx est la premiere fois
				// System.out.println ("J'ai recu la premiere fois de client
				// ---------------------- " + chaine_entree) ;
				if (chaine_entree.contentEquals("connect")) {


					flux_sortie.println("your are connected   , manual utilisation :     id_util:  <numero utilisateur> , nom_chans:  <nom de la chanson> , add_comm:  <commantaire>  " );





				} else {
					flux_sortie.println("clic <connect> to connect to the serveur ");
					continue;

				}

				do {
					// hna serveur ye9ra wach jah men client w yheto f variable
					chaine = flux_entree.readLine();
					// hna serveur yehkem yafichi wach jah men client
					if (chaine.contentEquals("dico")) {

						System.out.println("client sortie");
						System.exit(1);
					}
					System.out.println("Le client m'a repondu  ----- :  " + chaine);
					if (!chaine.contains(":")) {
						flux_sortie.println("jaccepte des commande  < <truc> : truc >");

					} else if (countChar(chaine, ':') > 1) {
						flux_sortie.println("jaccepte des commande  < <truc> : truc > , pas plusieurs de :");

					}

					else {

						if (chaine.length() > 50) {
							flux_sortie.println("  caractere too long or too small ");

						} else {

							// chaine = chaine.substring(0, 13);
							// System.out.println ("Le client m'a repondu ----- : " + chaine) ;
							tab = chaine.split(":");
							x1 = tab[0];
							x2 = tab[1];
							if ( !x1.contentEquals("id_util") && !x1.contentEquals("nom_chans") && !x1.contentEquals("add_comm")  ) {
								flux_sortie.println("commande nexiste pas ");

							}
							else {


							if (x1.contentEquals("id_util")  && id_utill == false ) {
								String s = "";

								s = utilisateur(x1, x2);
								if (s.contains("utilisateur dosen't exist")) {

									flux_sortie.println("utilisateur nexiste pas");
								} else {
									id_utili = x2;
									flux_sortie.println(s);
									util_occurs++;
									id_utill =  true ;

								}

							}

							else 	if (x1.contentEquals("id_util")  && id_utill == true ) {


									flux_sortie.println("you have inserted un utilisateur ");


							}

							else if (x1.contentEquals("nom_chans") && chanson_add == false  ) {
								String s = "";
								s = chanson(x1, x2);
								if (s.contains("chanson dosen't exist")) {

									flux_sortie.println("chason nexiste pas");
								} else {
									id_chanson = s;
									flux_sortie.println(s);

									chanson_add = true;
								}

							}

							else if (x1.contentEquals("nom_chans") && chanson_add == true  ) {


									flux_sortie.println("ta choisi une chanson");


							}

							else if (x1.contentEquals("add_comm") && chanson_add == true &&   id_utill == true) {
								boolean inserted;

								inserted = commant(x1, x2, id_utili, id_chanson);

								if (inserted = true) {
									flux_sortie.println("comment has been inserted ");

								} else {
									flux_sortie.println("commment hasn't been inserted");

								}

							}
							else if (x1.contentEquals("add_comm") && chanson_add == false  &&   id_utill == true) {
								flux_sortie.println("ta oublier d'ajouter la chanson ");


							}
							else if (x1.contentEquals("add_comm") && chanson_add == true  &&   id_utill == false ) {
								flux_sortie.println("ta oublier d'ajouter utilisateur ");


							}
							else if (x1.contentEquals("add_comm") && chanson_add == false   &&   id_utill == false ) {
								flux_sortie.println("il y a une manque de utilisateur et la chanson ");


							}

						}

						}
					}

				} while (chaine != null);

				// ------------------------------------------------------------------------------------------------------------------

			}

		} catch (Exception e) {
			System.out.println("client sortie");
			// flux_sortie.println("srat haja hnnnnna");
			// System.exit (1) ;
		}

		flux_sortie.close();
		flux_entree.close();
		clientSocket.close();
		serverSocket.close();
	}

	public static int countChar(String str, char c) {
		int count = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c)
				count++;
		}

		return count;
	}
//-----------------------------
	public static boolean isNumeric( String str) {

        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        return str.chars().allMatch(Character::isDigit);

    }




	//--------------------------------------------------------------------
	public static String utilisateur(String x1, String x2) {
		ResultSet rs;
		String error = "utilisateur dosen't exist";
		String x = "";

		try {
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/Bdmusic", "postgres",
					"A123456");

			if (x1.contentEquals("id_util")) {
				boolean f =  isNumeric (x2);

					if (f == true ){
				// rs = conn.createStatement().executeQuery("SELECT id_utilisateur FROM
				// utilisateur Where no_compo like " + "'" + x2 + "'");
				rs = conn.createStatement().executeQuery(
						"SELECT nom_utilisateur FROM utilisateur Where id_utilisateur = " + "'" + x2 + "'");

				if (rs.next()) {

					x = rs.getString(1);

				} else {
					x = error;
				}

			}



					else {
						x = error;

					}


		}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		return x;
	}


	// -------------------------------------------------------------------------------------------------
	public static String chanson(String x1, String x2) {
		ResultSet rs;
		String error = "chanson dosen't exist";
		String x = "";

		try {
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/Bdmusic", "postgres",
					"A123456");
			if (x1.contentEquals("nom_chans")) {
				boolean f =  isNumeric (x2);
				if (f == false ){

				rs = conn.createStatement()
						.executeQuery("SELECT id_chanson FROM chanson Where nom_chanson like " + "'" + x2 + "'");
				// rs = conn.createStatement().executeQuery("SELECT nom_chanson FROM chanson
				// Where id_utilisateur = " + "'" + x2 + "'");

				if (rs.next()) {

					x = rs.getString(1);

				} else {
					x = error;
				}


				}
				else {
					x = error;

				}





			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return x;
	}

	// -------------------------------------------------------------------

	public static boolean commant(String x1, String x2, String id_utilisateur, String id_chanson) {
		ResultSet rs;
		String error = "chanson dosen't exist";
		String x = "";
		boolean insert = true;

		try {
			String time = time();
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/Bdmusic", "postgres",
					"A123456");
			if (x1.contentEquals("add_comm")) {
				String sql = "insert into avis values (DEFAULT," + id_chanson + "," + id_utilisateur + "," + "'" + time
						+ "','" + x2 + "')";

				conn.createStatement().executeUpdate(sql);
//rs = conn.createStatement().executeQuery("INSERT INTO  avis (id_avis_chanson, id_avis_no_util_cli, date_avis, commentaire) VALUES ("+id_chanson +","+ id_utilisateur +","+ time()+","+x2+")");
				// x = rs.toString() ;
				// rs = conn.createStatement().executeQuery("SELECT nom_chanson FROM chanson
				// Where id_utilisateur = " + "'" + x2 + "'");

			}

		} catch (Exception e) {
			System.out.println("i cant insert");
			insert = false;
			return insert;
		}

		// return insert;
		return insert;
	}

	// -----------------------------------------------------------------------------------------------------------
	public static String time() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now).toString();

	}




}
