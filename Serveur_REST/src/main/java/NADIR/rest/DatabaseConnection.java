package NADIR.rest;

import java.sql.*;
import java.util.*;

/**
 * Singleton qui représente la connection à la base de données.
 */
public class DatabaseConnection
{
    private static DatabaseConnection instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/pr-tp-4";
    private String urlNadir = "jdbc:mysql://localhost:8889/pr-tp-4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=true";
    private String username = "root";
    private String password = "root";

    private DatabaseConnection() throws SQLException
    {
        connection = DriverManager.getConnection(urlNadir, username, password);
    }

    public static DatabaseConnection get_instance() throws SQLException
    {
        if (instance == null || instance.get_connection().isClosed())
        {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    private Connection get_connection()
    {
        return connection;
    }

    /**
     * Obtient les informations des waypoint à partir de l'id de la dernière route ajouté à la BD.
     * @param idEmploye
     * @return Liste en ordre des arrets (String) à faire.
     */
    public LinkedList getRoute(String idEmploye)
    {
        LinkedList routeLinkedList = new LinkedList();

        try
        {
            PreparedStatement statement = get_connection().prepareStatement(
                    "SELECT * from waypoint where route_id = (select id from route where employee_id = ? order by id DESC LIMIT 1)"
            );
            statement.setString(1, idEmploye);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                routeLinkedList.add(resultSet.getString(4));
            }


        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


        return routeLinkedList;
    }



    public static void main(String[] args)
    {

        try
        {
            DatabaseConnection.get_instance().getRoute("Alix");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
