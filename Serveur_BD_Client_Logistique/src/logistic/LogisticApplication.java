package logistic;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import database.DatabaseConnection;
import java.sql.SQLException;

/**
 * Point d'entrée de l'application logistique
 */
public class LogisticApplication extends Application
{
    private LogisticController controller;
    private ObservableList<String> addresses;
    private ObservableList<String> employees;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/logistic.fxml"));
        controller = new LogisticController();
        loader.setController(controller);
        Parent root = loader.load();

        primaryStage.setTitle("TP4 - Programmation Réseau - Nadir & Alix");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
        primaryStage.setResizable(false);

        addresses = FXCollections.observableArrayList();
        employees = FXCollections.observableArrayList();
        populate_addresses();
        populate_employees();
    }

    /**
     * Prend les adresses contenues dans la DB et les envois au controlleur de l'interface graphique
     */
    private void populate_addresses()
    {
        try
        {
            addresses.addAll(DatabaseConnection.get_instance().get_all_addresses().values());

            controller.set_waypoints_list(addresses);
        }
        catch (SQLException e)
        {
            Logger.Log(Logger.Severity.ERROR, this.getClass(), e.getMessage());
        }
    }

    /**
     * Prend les employés contenus dans la DB et les envois au controlleur de l'interface graphique
     */
    private void populate_employees()
    {
        try
        {
            employees.addAll(DatabaseConnection.get_instance().get_all_employees());

            controller.set_employees_list(employees);
            controller.select_first_employee();
        }
        catch (SQLException e)
        {
            Logger.Log(Logger.Severity.ERROR, this.getClass(), e.getMessage());
        }
    }

    /**
     * Point d'entré
     * @param args Argument de la ligne de commande
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
