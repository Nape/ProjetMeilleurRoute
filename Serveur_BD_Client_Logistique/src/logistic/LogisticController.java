package logistic;

import database.DatabaseConnection;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlleur FXML
 */
public class LogisticController implements Initializable
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnGenerate;

    @FXML
    private Button btnQuitter;

    @FXML
    private ChoiceBox<String> ddEmployees;

    @FXML
    private Label lblFlashMessage;

    @FXML
    private Button btnSend;

    @FXML
    private ListView<String> lstvWaypoints;

    @FXML
    private TextField txtDestination;

    @FXML
    private TextField txtOrigin;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lstvWaypoints.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        btnGenerate.setOnAction(event -> btn_generate_clicked());
        btnQuitter.setOnAction(event -> quiter());
        try
        {
            txtOrigin.setText(DatabaseConnection.get_instance().getOrigine());
            txtDestination.setText(DatabaseConnection.get_instance().getDestination());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void btn_generate_clicked()
    {
        String origin = get_origin();
        String destination = get_destination();
        String employee = get_employee();
        List<String> waypoints = get_waypoints();

        if (!origin.isEmpty() && !destination.isEmpty() && waypoints.size() > 0)
        {
            try
            {
                // request the webservice
                String response = Directions.make_request(origin, destination, waypoints);

                // parse response & get the order in which the waypoints must be traveled
                List<Integer> waypoints_order = ResponseParser.ExtractWaypointsOrder(response);

                // reorder the waypoints according to the data returned from the webservice
                List<String> ordered_waypoints = new ArrayList<>(waypoints_order.size());
                for (Integer index:  waypoints_order)
                {
                    ordered_waypoints.add(waypoints.get(index));
                }

                // persist the route
                DatabaseConnection.get_instance().insert_route(origin, destination, employee, ordered_waypoints);

                // success! build a message for the log
                StringBuilder builder = new StringBuilder("Successfully inserted new route:\n");
                builder.append("\tOrig.: ").append(origin).append("\n");
                builder.append("\tDest.: ").append(destination).append("\n");
                builder.append("\tEmpl.: ").append(employee).append("\n");
                int index = 1;
                for (String waypoint: ordered_waypoints)
                {
                    builder.append("\t"+(index++)+"#   :").append(waypoint).append("\n");
                }

                Logger.Log(Logger.Severity.INFORMAL, LogisticApplication.class, builder.toString());
                set_flash("La route a bien été optimizée et insérée!");
            }
            catch ( IOException | SQLException | org.json.simple.parser.ParseException e)
            {
                Logger.Log(Logger.Severity.ERROR, LogisticApplication.class, e.getMessage());
                set_flash("Une erreur s'est produite. La route n'a pas été enregistrée!");
            }
        }
        else
        {
            set_flash("Veuillez entrer une origine, une destination et au moins un point d'arrêt");
        }
    }
    private void quiter()
    {
        Platform.exit();
    }

    public String get_origin()
    {
        return txtOrigin.getText();
    }

    public String get_destination()
    {
        return txtDestination.getText();
    }

    public String get_employee()
    {
        return ddEmployees.getValue();
    }

    public List<String> get_waypoints()
    {
        return lstvWaypoints.getSelectionModel().getSelectedItems();
    }

    public void clear_flash()
    {
        lblFlashMessage.setText("");
    }

    public void set_flash(String message)
    {
        lblFlashMessage.setText(message);
    }


    public void set_waypoints_list(ObservableList<String> items)
    {
        lstvWaypoints.setItems(items);
    }

    public void set_employees_list(ObservableList<String> items)
    {
        ddEmployees.setItems(items);
    }

    public void select_first_employee()
    {
        ddEmployees.getSelectionModel().selectFirst();
    }
}
