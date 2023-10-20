/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;

/**
 * FXML Controller class
 *
 * @author Mr_Abdelhake
 */

public class SellersController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;
    /**
     * Initializes the controller class.
     */
    @FXML
    TextField SelID,NameSel,PwSel;
    @FXML
    TableView<Seller_Type> TblSel;
    @FXML
    TableColumn<Seller_Type,Integer> id;
    @FXML
    TableColumn<Seller_Type,String> name;
    @FXML
    TableColumn<Seller_Type,String> pw;
    @FXML
    TableColumn<Seller_Type,String> ge;
    @FXML
    TableColumn<Seller_Type,String> ro;
    @FXML
    ChoiceBox RoleSel,GenderSel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadData();
            fillChoiceBox();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //  *************************** DataBase traitment *******************
    String query = null;
    Connection connection = null;
    PreparedStatement Pstatment = null;
    ResultSet result = null;
    ObservableList<Seller_Type> sellerList = FXCollections.observableArrayList();

    public void clear(){
        SelID.setText("");
        NameSel.setText("");
        PwSel.setText("");
        RoleSel.getSelectionModel().select(1);
        GenderSel.getSelectionModel().select(0);
    }
    public void loadData() throws SQLException {

        connection = GoConnection.getConnection();
        refreshTable();
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        pw.setCellValueFactory(new PropertyValueFactory<>("Password"));
        ge.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        ro.setCellValueFactory(new PropertyValueFactory<>("Role"));
    }
    private void refreshTable() throws SQLException {
        sellerList.clear();
        query = "SELECT * FROM sellers_tbl" ;
        Pstatment = connection.prepareStatement(query);
        result = Pstatment.executeQuery();
        while(result.next()) {
            sellerList.add(new Seller_Type(Integer.parseInt(result.getString("id")),
                    result.getString("name"),
                    result.getString("password"),
                    result.getString("gender"),
                    result.getString("role")));
            TblSel.setItems(sellerList);
        }
    }
    public void selectedItem(){
        Seller_Type s = TblSel.getSelectionModel().getSelectedItem();
        SelID.setText(String.valueOf(s.getId()));
        NameSel.setText(s.getName());
        PwSel.setText(s.getPassword());
        switch (s.getGender()){
            case "Male":
                GenderSel.getSelectionModel().select(0);
                break;
            case "Female":
                GenderSel.getSelectionModel().select(1);
                break;
        }
        switch (s.getRole()){
            case "Admin":
                RoleSel.getSelectionModel().select(0);
                break;
            case "Seller":
                RoleSel.getSelectionModel().select(1);
                break;
        }
    }
    public void fillChoiceBox(){
        ObservableList<String>  role = FXCollections.observableArrayList("Admin","Seller");
        ObservableList<String>  gender = FXCollections.observableArrayList("Male","Female");
        RoleSel.setItems(role);
        GenderSel.setItems(gender);
        RoleSel.getSelectionModel().select(1);
        GenderSel.getSelectionModel().select(0);
    }
    public void AddSeller() throws SQLException {
        if (SelID.getText().isEmpty()||NameSel.getText().isEmpty()||PwSel.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            Pstatment = connection.prepareStatement("insert into sellers_tbl values(?,?,?,?,?)");
            Pstatment.setInt(1,Integer.valueOf(SelID.getText()));
            Pstatment.setString(3, PwSel.getText());
            Pstatment.setString(4, GenderSel.getSelectionModel().getSelectedItem().toString());
            Pstatment.setString(2, NameSel.getText());
            Pstatment.setString(5, RoleSel.getSelectionModel().getSelectedItem().toString());
            int row = Pstatment.executeUpdate();
            JOptionPane.showMessageDialog(null, "Seller Added Successfully");
            loadData();
            clear();
        }
    }
    public void EditSeller() throws SQLException {
        if (SelID.getText().isEmpty()||NameSel.getText().isEmpty()||PwSel.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            String Query = "Update sellers_tbl set NAME='"+NameSel.getText()+"' , PASSWORD='"+PwSel.getText()+"'"+
                    ", GENDER='"+GenderSel.getSelectionModel().getSelectedItem().toString()+"'"+
                    ", ROLE='"+RoleSel.getSelectionModel().getSelectedItem().toString()+"'"+"where ID="+SelID.getText();
            Statement statment =  connection.createStatement();
            statment.execute(Query);
            JOptionPane.showMessageDialog(null, "Seller Edit Successfully");
            loadData();
            clear();
        }
    }
    public void DeleteSeller() throws SQLException {
        if (SelID.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            String SId = SelID.getText();
            String Query = "Delete from sellers_tbl where ID ="+SId;
            Statement statment =connection.createStatement();
            statment.execute(Query);
            loadData();
            JOptionPane.showMessageDialog(null, "Deleted Successfully");
            clear();
        }
    }
    // *************************** Call *********************************
    public void exit(MouseEvent event){
        ((Node) event.getSource()).getScene ().getWindow ().hide ();
    }
    public void callCAT(MouseEvent event) throws IOException{
        callStage(new Stage(),"Category.fxml");
        exit(event);

    }
    public void callProd(MouseEvent event) throws IOException{
        exit(event);
        callStage(new Stage(),"Prodect.fxml");
    }
    public void logout(MouseEvent event) throws IOException{
        exit(event);
        callStage(new Stage(),"LogIn.fxml");
    }
    public void callBil(MouseEvent event)throws IOException{
        exit(event);
        callStage(new Stage(),"BillPoint.fxml");
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
