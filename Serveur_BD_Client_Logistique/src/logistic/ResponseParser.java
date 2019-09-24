package logistic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ResponseParser
{
    /**
     * Parse une réponse de l'api de Google Drive Directions et en extrait l'ORDRE, pas les adresses en soit, dans
     * laquelle les points d'arrêts devront être visité.
     * @param response
     * @return Liste de l'ORDRE par laquelle les adresses doivent être visitées.
     * @throws ParseException
     */
    public static List<Integer> ExtractWaypointsOrder(String response) throws ParseException
    {
        ArrayList<Integer> wayoints_order = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(response);

        JSONArray   node_routes = (JSONArray) object.get("routes");
        JSONObject  node_routes_zero = (JSONObject) node_routes.get(0);
        JSONArray   node_routes_zero_waypoint_order = (JSONArray) node_routes_zero.get("waypoint_order");

        Iterator<String> iterator = node_routes_zero_waypoint_order.iterator();
        while (iterator.hasNext())
        {
            int order_index = Integer.parseInt(String.valueOf(iterator.next()));
            wayoints_order.add(order_index);
        }

        return wayoints_order;
    }
}
