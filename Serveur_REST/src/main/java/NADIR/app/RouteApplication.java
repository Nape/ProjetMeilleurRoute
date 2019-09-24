package NADIR.app;

import NADIR.rest.RouteRestService;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nadir Pelletier
 * For : Serveur_REST_TP4
 * Date : 2019-09-10
 * Time : 07:39
 */
public class RouteApplication extends Application
{
    private Set<Object> singletons = new HashSet<Object>();

    public RouteApplication()
    {
        // Register RouteRestServices
        singletons.add(new RouteRestService());
    }

    @Override
    public Set<Object> getSingletons()
    {
        return singletons;
    }
}