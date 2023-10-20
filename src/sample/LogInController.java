package sample;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;

/**
 *
 * @author Mr_Abdelhake
 */
public class LogInController implements Initializable {
    @FXML
    ChoiceBox<String> Crole;
    @FXML
    TextField user,pw;
    Connection connection;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ObservableList<String> role = FXCollections.observableArrayList("Admin","Seller");
            Crole.setItems(role);
            Crole.getSelectionModel().select(1);
            // con = new Connection_derby();
        } catch (Exception ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void exit(javafx.scene.input.MouseEvent event) throws IOException{
        ((Node) event.getSource()).getScene ().getWindow ().hide ();

    }
    public void login(javafx.scene.input.MouseEvent event) throws IOException, SQLException {
//        connection = GoConnection.getConnection();
        callStage(new Stage(), "BillPoint.fxml");
        exit(event);
//        if ("Admin".equals(Crole.getSelectionModel().getSelectedItem())){
//            GoConnection.setRole(0);
//            Login(event);
//        }else {
//            GoConnection.setRole(1);
//            Login(event);
//        }

    }
    public void Login(javafx.scene.input.MouseEvent event) throws SQLException{
        String query = "select * from sellers_tbl where NAME='"+user.getText()+"'"
                + "and PASSWORD='"+pw.getText()+"' and ROLE = '"+Crole.getSelectionModel().getSelectedItem()+"'";

        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.next()){
                callStage(new Stage(), "BillPoint.fxml");
                exit(event);
            }else{
                JOptionPane.showMessageDialog(null, "your user name or password isn't corecte");
            }

        }catch(Exception e){
        }

    }

    public void callStage(Stage stage, String fxmlName) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        stage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed((javafx.scene.input.MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((javafx.scene.input.MouseEvent event) -> {
            stage.setX(event.getScreenX()-xOffset);
            stage.setY(event.getScreenY()-yOffset);
        });
        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }


}