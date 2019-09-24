package database;

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

    public String getOrigine() throws SQLException
    {
        Statement statement = get_connection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT adresseOrigine FROM origine WHERE id = 1");
        String adresseOrigine = "";
        if (resultSet.next())
        {
            adresseOrigine = resultSet.getString(1);
        }
        return adresseOrigine;
    }

    public String getDestination() throws SQLException
    {
        Statement statement = get_connection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT adresseDestination FROM destination WHERE id  = 1");
        String adresseDestination = "";
        if (resultSet.next())
        {
            adresseDestination = resultSet.getString(1);
        }

        return adresseDestination;
    }

    /**
     * Obtain a map of pairs (id, address) for every entry in the 'address' table.
     * @return A map of every address in the database paired to its id
     * @throws SQLException
     */
    public Map<Integer, String> get_all_addresses() throws SQLException
    {
        Statement statement = get_connection().createStatement();

        ResultSet results = statement.executeQuery("SELECT * FROM address");

        Map<Integer, String> addresses = new HashMap<Integer, String>();
        int     id;
        String  address;
        while (results.next())
        {
            id =        results.getInt("id");
            address =   results.getString("address");
            addresses.put(id, address);
        }

        return addresses;
    }

    /**
     * Insert an address to the table 'address' in the database
     * @param address The address as a string
     * @return The number of rows that were affected. It should always be one.
     * @throws SQLException
     */
    public int insert_address(String address) throws SQLException
    {
        PreparedStatement statement = get_connection().prepareStatement("INSERT INTO address(address) VALUES(?)");
        statement.setString(1, address);

        System.out.println("Executing the following query:");
        System.out.println(statement.toString());

        return statement.executeUpdate();
    }

    /**
     * @return A list of every entry in the 'employee' table.
     * @throws SQLException
     */
    public ArrayList<String> get_all_employees() throws SQLException
    {
        Statement statement = get_connection().createStatement();

        ResultSet results = statement.executeQuery("SELECT * FROM employee");

        ArrayList<String> employees = new ArrayList<>();
        String  username;
        while (results.next())
        {
            username =  results.getString("username");
            employees.add(username);
        }

        return employees;
    }

    /**
     * Insère une route dans la base de donnée.
     * Une route a une origine, une destination et appartient à un employé.
     * En cas d'erreur, rien n'est enregistré grâce aux transactions.
     * @param origin
     * @param destination
     * @param employee
     * @param ordered_waypoints Points d'arrêts
     * @throws SQLException
     */
    public void insert_route(String origin, String destination, String employee, List<String> ordered_waypoints) throws SQLException
    {
        try
        {
            connection.setAutoCommit(false);

            PreparedStatement statement = get_connection().prepareStatement(
                    "INSERT INTO route(origin, destination, employee_id) VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, origin);
            statement.setString(2, destination);
            statement.setString(3, employee);

            System.out.println("Executing the following query:");
            System.out.println(statement.toString());

            statement.executeUpdate();
            ResultSet results = statement.getGeneratedKeys();
            if (results.next())
            {
                Integer route_id = results.getInt(1);
                Integer next_waypoint_id = null;
//                Collections.reverse(ordered_waypoints);
                for (String waypoint: ordered_waypoints)
                {
                    statement = get_connection().prepareStatement(
                            "INSERT INTO waypoint(route_id, next_waypoint_id, info) VALUES (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, route_id.toString());
                    statement.setString(3,waypoint);

                    if (next_waypoint_id == null)
                    {
                        statement.setNull(2, Types.INTEGER);
                    }
                    else
                    {
                        statement.setInt(2, next_waypoint_id);
                    }

                    System.out.println("Executing the following query:");
                    System.out.println(statement.toString());
                    statement.executeUpdate();
                    results = statement.getGeneratedKeys();
                    if (results.next())
                    {
                        next_waypoint_id = results.getInt(1);
                    }
                    else
                    {
                        throw new SQLException("Cannot extract the id of the previously inserted waypoint.");
                    }
                }

                connection.commit();
            }
            else
            {
                throw new SQLException("Cannot extract the route id.");
            }
        }
        catch (SQLException e)
        {
            connection.rollback();
            throw e;
        }
        finally
        {
            connection.setAutoCommit(true);
        }
    }

    public static void main(String[] args)
    {
        try
        {
            Map<Integer, String> addresses = DatabaseConnection.get_instance().get_all_addresses();

            addresses.forEach((id, address) ->
                    System.out.println(id + " -> " + address)
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
