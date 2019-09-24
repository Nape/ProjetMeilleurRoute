import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Nadir Pelletier
 * For : Serveur_REST_TP4
 * Date : 2019-09-13
 * Time : 08:07
 */
public class JavaApp
{
    public static void main(String[] args) throws IOException
    {
        String urlString = "http://localhost:4444/Serveur_REST_TP4_war/route/";


        Scanner scanner = new Scanner(System.in);

        System.out.println("Entrez votre nom de livreur");
        String nomLivreur = scanner.nextLine();
        urlString+= nomLivreur;

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        System.out.println("======================INFOS========================");
        System.out.println("Sending 'GET' request to URL:\n" + url);
        System.out.println("Response Code : " + status);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;

        System.out.println("======================ROUTES========================");
        while ((line = in.readLine()) != null)
        {
            System.out.println(explode(line));
        }
        System.out.println("====================================================");
        in.close();
    }

    /**
     * Remplace le caratère "&" par " ".
     * @param string Chaine de caractère.
     * @return String libre de "&"
     */
    private static String explode(String string)
    {

        String[] split = string.split("&");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++)
        {
            sb.append(split[i]);
            if (i != split.length - 1)
            {
                sb.append(" ");
            }
        }
       return sb.toString();
    }
}