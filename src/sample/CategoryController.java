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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
public class CategoryController implements Initializable {
    @FXML
    TextField CatId,CatName;
    @FXML
    TextArea CatDescription;
    @FXML
    TableView<Category_Type> CatTbl;
    @FXML
    TableColumn<Category_Type,Integer> IdCo;
    @FXML
    TableColumn<Category_Type,String> NameCo;
    @FXML
    TableColumn<Category_Type,String> DescrptionCo;
    private double xOffset = 0;
    private double yOffset = 0;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadData();
        } catch (Exception ex) {
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //  *************************** DataBase traitment *******************

    String query = null;
    Connection connection = null;
    PreparedStatement Pstatment = null;
    ResultSet result = null;
    ObservableList<Category_Type> Categorys_list = FXCollections.observableArrayList();

    public void clear(){
        CatId.setText("");
        CatName.setText("");
        CatDescription.setText("");
    }

    public void loadData() throws SQLException {
        connection = GoConnection.getConnection();
        refreshTable();
        IdCo.setCellValueFactory(new PropertyValueFactory<>("ID"));
        NameCo.setCellValueFactory(new PropertyValueFactory<>("Name"));
        DescrptionCo.setCellValueFactory(new PropertyValueFactory<>("Description"));

    }

    private void refreshTable() throws SQLException {
        Categorys_list.clear();
        query = "SELECT * FROM categorys_tbl" ;
        Pstatment = connection.prepareStatement(query);
        result = Pstatment.executeQuery();
        while(result.next()) {
            Categorys_list.add(new Category_Type(
                    Integer.parseInt(result.getString("id")),
                    result.getString("name"),
                    result.getString("description")
            ));
            CatTbl.setItems(Categorys_list);
        }
    }

    public void selectedItem(){
        Category_Type cat = CatTbl.getSelectionModel().getSelectedItem();
        CatId.setText(String.valueOf(cat.getID()));
        CatName.setText(cat.getName());
        CatDescription.setText(cat.getDescription());
    }

    public void AddCategory() throws SQLException {
        if (CatId.getText().isEmpty()||CatName.getText().isEmpty()||CatDescription.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            Pstatment = connection.prepareStatement("insert into categorys_tbl values(?,?,?)");
            Pstatment.setInt(1,Integer.valueOf(CatId.getText()));
            Pstatment.setString(2, CatName.getText());
            Pstatment.setString(3, CatDescription.getText());
            int row = Pstatment.executeUpdate();
            JOptionPane.showMessageDialog(null, "Seller Added Successfully");
            loadData();
            clear();
        }
    }

    public void EditCategory() throws SQLException {
        if (CatId.getText().isEmpty()||CatName.getText().isEmpty()||CatDescription.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            String Query = "Update categorys_tbl set NAME='"+CatName.getText()+"' , DESCRIPTION='"+CatDescription.getText()+"'"+
                   "where ID="+CatId.getText();
            Statement statment =  connection.createStatement();
            statment.execute(Query);
            JOptionPane.showMessageDialog(null, "Seller Edit Successfully");
            loadData();
            clear();
        }
    }

    public void DeleteCategory() throws SQLException {
        if (CatId.getText().isEmpty()){
            System.out.println("fill all ");
        }else {
            String SId = CatId.getText();
            String Query = "Delete from categorys_tbl where ID ="+SId;
            Statement statment =connection.createStatement();
            statment.execute(Query);
            loadData();
            JOptionPane.showMessageDialog(null, "Deleted Successfully");
            clear();
        }
    }


    //  *************************** Call *********************************
    public void exit(MouseEvent event){
        ((Node) event.getSource()).getScene ().getWindow ().hide ();
    }
    public void callSel(MouseEvent event) throws IOException{
        exit(event);
        callStage(new Stage(),"Sellers.fxml");
    }
    public void callProd(MouseEvent event)throws IOException{
        exit(event);
        callStage(new Stage(),"Prodect.fxml");
    }
    public void logout(MouseEvent event)throws IOException{
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
