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
public class ProdectController implements Initializable {
    @FXML
    TextField prodId,prodCodeBar,prodName,prodPrice,prodQuantity;
    @FXML
    TableView<Prodect_Type> prodTBL;
    @FXML
    TableColumn<Prodect_Type,Integer>  coID;
    @FXML
    TableColumn<Prodect_Type,String >  coBarCode;
    @FXML
    TableColumn<Prodect_Type,String >  coName;
    @FXML
    TableColumn<Prodect_Type,String >  coCategory;
    @FXML
    TableColumn<Prodect_Type,Integer >  coQuantity;
    @FXML
    TableColumn<Prodect_Type,Double >  coPrice;
    @FXML
    ChoiceBox<String> prodCategory;

    private double xOffset = 0;
    private double yOffset = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
           loadData();
           fillChoiceBox();
        } catch (Exception ex) {
            Logger.getLogger(ProdectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //  *************************** DataBase traitment *******************
    String query = null;
    Connection connection = null;
    PreparedStatement Pstatment = null;
    ResultSet result = null;
    ObservableList<Prodect_Type> ProdectList = FXCollections.observableArrayList();

    public void clear(){
        prodId.setText("");
        prodCodeBar.setText("");
        prodName.setText("");
        prodPrice.setText("");
        prodQuantity.setText("");
        prodCategory.getSelectionModel().select(0);
    }
    public void loadData() throws SQLException {

        connection = GoConnection.getConnection();
        refreshTable();
        coID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        coBarCode.setCellValueFactory(new PropertyValueFactory<>("BarCode"));
        coName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        coCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        coQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        coPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

    }
    private void refreshTable() throws SQLException {
        ProdectList.clear();
        query = "SELECT * FROM prodects_tbl" ;
        Pstatment = connection.prepareStatement(query);
        result = Pstatment.executeQuery();
        while(result.next()) {
            ProdectList.add(new Prodect_Type(
                    Integer.parseInt(result.getString("id")),
                    result.getString("code_bar"),
                    Integer.parseInt(result.getString("quantity")),
                    result.getString("name"),
                    result.getString("category"),
                    Double.parseDouble(result.getString("price"))
                    ));
            prodTBL.setItems(ProdectList);
        }
    }
    public void selectedItem(){
        Prodect_Type prod = prodTBL.getSelectionModel().getSelectedItem();
        prodId.setText(String.valueOf(prod.getId()));
        prodName.setText(prod.getName());
        prodCodeBar.setText(prod.getBarCode());
        prodPrice.setText(String.valueOf(prod.getPrice()));
        prodQuantity.setText(String.valueOf(prod.getQuantity()));
        prodCategory.getSelectionModel().select(prod.getCategory());
    }
    public void fillChoiceBox() throws SQLException {
        ObservableList<String>  categorys = FXCollections.observableArrayList();
        categorys.clear();
        query = "SELECT NAME FROM categorys_tbl" ;
        Pstatment = connection.prepareStatement(query);
        result = Pstatment.executeQuery();
        while (result.next()){
            categorys.add(result.getString("name"));
        }
        prodCategory.setItems(categorys);
        prodCategory.getSelectionModel().select(0);
    }
    public void AddProdect() throws SQLException {
        if (prodId.getText().isEmpty()||prodName.getText().isEmpty()||prodCodeBar.getText().isEmpty()
                ||prodPrice.getText().isEmpty()||prodQuantity.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            Pstatment = connection.prepareStatement("insert into prodects_tbl values(?,?,?,?,?,?)");
            Pstatment.setInt(1,Integer.valueOf(prodId.getText()));
            Pstatment.setString(2, prodCodeBar.getText());
            Pstatment.setString(3, prodName.getText());
            Pstatment.setString(4, prodCategory.getSelectionModel().getSelectedItem());
            Pstatment.setDouble(5, Double.parseDouble(prodPrice.getText()));
            Pstatment.setInt(6, Integer.valueOf(prodQuantity.getText()));
            int row = Pstatment.executeUpdate();
            JOptionPane.showMessageDialog(null, "Seller Added Successfully");
            loadData();
            clear();
        }
    }
    public void EditProdect() throws SQLException {
        if (prodId.getText().isEmpty()||prodName.getText().isEmpty()||prodCodeBar.getText().isEmpty()
                ||prodPrice.getText().isEmpty()||prodQuantity.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            String Query = "Update prodects_tbl set CODE_BAR='"+prodCodeBar.getText()+"' , NAME='"+prodName.getText()+"'"+
                    ", CATEGORY='"+prodCategory.getSelectionModel().getSelectedItem()+"'"+
                    ", PRICE='"+prodPrice.getText()+"'"+", QUANTITY='"+prodQuantity.getText()+"'"+"where ID="+prodId.getText();
            Statement statment =  connection.createStatement();
            statment.execute(Query);
            JOptionPane.showMessageDialog(null, "Seller Edit Successfully");
            loadData();
            clear();
        }
    }
    public void DeleteProdect() throws SQLException {
        if (prodId.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            String SId = prodId.getText();
            String Query = "Delete from prodects_tbl where ID ="+SId;
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
    public void callSel(MouseEvent event) throws IOException{
        exit(event);
        callStage(new Stage(),"Sellers.fxml");
    }
     public void callCat(MouseEvent event) throws IOException{
        exit(event);
        callStage(new Stage(),"Category.fxml");
    }
    public void logout(MouseEvent event) throws IOException{
        exit(event);
        callStage(new Stage(),"LogIn.fxml");
    }
     public void callBil(MouseEvent event) throws IOException{
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
