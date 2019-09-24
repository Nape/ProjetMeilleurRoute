package logistic;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente et sert d'interface à l'API de Google Maps Directions
 */
public class Directions
{
    private static final String API_KEY = "***************************************";
    private static final String API_URL = "https://maps.googleapis.com/maps/api/directions/json";

    /**
     * Typiquement, l'adresse de départ et de destination seront la même.
     * Fonction privée utilisé par une autre fonction qui envoie la requête.
     * @param origin L'adresse de départ
     * @param destination L'adresse de destination
     * @param waypoints Une liste des arrêts à faire en chemin
     * @return Un URL correctement contruit pour envoi à G.Maps Directions
     */
    private static String craft_request(String origin, String destination, List<String> waypoints)
    {
        StringBuilder builder = new StringBuilder(API_URL);
        builder.append("?");

        builder.append("origin=");
        builder.append(origin.replace(' ', '+'));

        builder.append("&");
        builder.append("destination=");
        builder.append(destination.replace(' ', '+'));

        builder.append("&");
        builder.append("waypoints=optimize:true|");
        for (int index = 0; index < waypoints.size(); index++)
        {
            builder.append(waypoints.get(index).replace(' ', '+'));

            if (index < waypoints.size() - 1)
            {
                builder.append('|');
            }
        }

        builder.append("&");
        builder.append("key=");
        builder.append(API_KEY);

        return builder.toString();
    }

    /**
     * Point d'entrée entre l'application et l'API de G.Maps.
     * Typiquement, l'adresse de départ et de destination seront la même.
     * @param origin L'adresse de départ
     * @param destination L'adresse de destination
     * @param waypoints Une liste des arrêts à faire en chemin
     * @return Un URL correctement contruit pour envoi à G.Maps Directions
     */
    public static String make_request(String origin, String destination, List<String> waypoints) throws IOException
    {
        URL url = new URL(craft_request(origin, destination, waypoints));

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();

        System.out.println("\nSending 'GET' request to URL:\n" + url);
        System.out.println("Response Code : " + status);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = in.readLine()) != null)
        {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static void main(String[] args)
    {
        ArrayList<String> waypoints = new ArrayList<>();
        waypoints.add("5200+Boulevard+Henri-Bourassa");
        waypoints.add("5655+Boulevard+Henri-Bourassa");
        waypoints.add("7900+Boulevard+Henri-Bourassa+Quebec");
        try
        {
            System.out.println(make_request(
                    "1715+Chemin+de+la+Canardière+Québec",
                    "1715+Chemin+de+la+Canardière+Québec",
                    waypoints
            ));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
