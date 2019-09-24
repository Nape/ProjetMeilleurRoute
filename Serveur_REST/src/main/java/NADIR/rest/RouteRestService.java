package NADIR.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by Nadir Pelletier
 * For : Serveur_REST_TP4
 * Date : 2019-09-10
 * Time : 07:35
 */
@Path("/")
public class RouteRestService
{
    private final String ARRET = "Arrêt #";
    private final String NEW_LINE = "\n";

    //localhost:4444/Serveur_REST_TP4_war/route/Alix
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("route/{idEmploye}")
    public String getRoutes(@PathParam("idEmploye") String idEmploye)
    {
        String route = "";
        try
        {
            LinkedList linkedListRoute = DatabaseConnection.get_instance().getRoute(idEmploye);
            for (int i = 0; i < linkedListRoute.size(); i++)
            {
                route += (ARRET+i+"&"+linkedListRoute.get(i)+NEW_LINE);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return route;
    }

}
