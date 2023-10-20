/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.awt.print.PrinterException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.*;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * FXML Controller class
 *
 * @author Mr_Abdelhake
 */
public class BillPointController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    TableView<Prodect_Type> prodTBL;
    @FXML
    TableColumn<Prodect_Type,Integer> coID;
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
    TableView<Bill_Type> sellTBL;
    @FXML
    TableColumn<Bill_Type,Integer> coIdS;
    @FXML
    TableColumn<Bill_Type,String >  coNameS;
    @FXML
    TableColumn<Bill_Type,Integer >  coQuantityS;
    @FXML
    TableColumn<Bill_Type,Double >  coTotalS;
    @FXML
    Label b1,b2,b3;
    @FXML
    TextField Id,Name,Quantity;
    @FXML
    Label Total;
    @FXML
    AnchorPane anchorBill,AnchorBillP;
    @FXML
    TextArea BillTXT;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            SellList.clear();
           loadData();
           if (GoConnection.getRole()!=0){
               notAdmin();
           }
        }catch(Exception e){
            
        }
    }
    //  *************************** DataBase traitment *******************
    String query = null;
    Connection connection = null;
    PreparedStatement Pstatment = null;
    ResultSet result = null;
    ObservableList<Prodect_Type> ProdectList = FXCollections.observableArrayList();
    ObservableList<Bill_Type> SellList = FXCollections.observableArrayList();
    public void loadData() throws SQLException {

        connection = GoConnection.getConnection();
        refreshTable();
        coID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        coBarCode.setCellValueFactory(new PropertyValueFactory<>("BarCode"));
        coName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        coCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        coQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        coPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        coIdS.setCellValueFactory(new PropertyValueFactory<>("Id"));
        coNameS.setCellValueFactory(new PropertyValueFactory<>("Name"));
        coQuantityS.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        coTotalS.setCellValueFactory(new PropertyValueFactory<>("Total"));


    }
    private void notAdmin(){
        b1.setDisable(true);
        b2.setDisable(true);
        b3.setDisable(true);
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
    // cherche
    private String getNameById() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT NAME FROM prodects_tblL where ID="+Id.getText());
        result.next();
        return result.getString(1);
    }
    private String getIdByName() throws SQLException{

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT ID FROM prodects_tbl where NAME='"+Name.getText().toUpperCase()+"'");
        result.next();
        return result.getString(1);
    }
    private Double getpriceById() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet reslt = statement.executeQuery("SELECT PRICE FROM prodects_tbl where ID="+Id.getText());
        reslt.next();
        return Double.parseDouble(reslt.getString(1));
    }
    private int getquantityById() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet reslt = statement.executeQuery("SELECT QUANTITY FROM prodects_tbl where ID="+Id.getText());
        reslt.next();
        return Integer.parseInt(reslt.getString(1));
    }
    private int getIndexbyId(){
        int nbrow = sellTBL.getItems().size();
        for(int i=0;i<nbrow;i++){
            if(sellTBL.getItems().get(i).getId()==Integer.parseInt(Id.getText())){
                return i;
            }
        }
        return -1;
    }
    //traitment
    @FXML
    private  void clear(){
        Id.setText("");
        Name.setText("");
        Quantity.setText("");
    }
    private void editProduct(int val) throws SQLException{
        String Query = "Update prodects_tbl set QUANTITY="+val+
                " where ID="+Id.getText();
        Statement add = connection.createStatement();
        add.execute(Query);
    }
    private void sell(){
        try {
            if(getquantityById()<Integer.parseInt(Quantity.getText())){

            } else {
                int getId = getIndexbyId();
                if(getId == -1){
                    int id = Integer.parseInt(Id.getText());
                    String name = Name.getText();
                    int quantity = Integer.parseInt(Quantity.getText());
                    Double total = getpriceById()*quantity;
                    SellList.add(new Bill_Type(id,name,quantity,total));
                    editProduct(getquantityById()-Integer.parseInt(Quantity.getText()));
                    fillSellTbl();
                    clear();
                }else{
                    editProduct(getquantityById()-Integer.parseInt(Quantity.getText()));
                    int quantity = Integer.parseInt(Quantity.getText())+SellList.get(getId).getQuantity();
                    Double total = quantity*getpriceById();
                    SellList.get(getId).setQuantity(quantity);
                    SellList.get(getId).setTotal(total);
                    fillSellTbl();
                    clear();
                }

            }

        } catch (SQLException ex) {

        }
    }
    @FXML
    private void AddSell(){
        if(Id.getText().isEmpty()||Name.getText().isEmpty()||Quantity.getText().isEmpty()){

        }else{
            try {
                if(Integer.parseInt(Quantity.getText()) <= getquantityById()){
                    sell();
                }
            } catch (SQLException ex) {
            }

        }
    }
    @FXML
    private void selectFromDB(){
        Prodect_Type item = prodTBL.getSelectionModel().getSelectedItem();
        Id.setText(String.valueOf(item.getId()));
        Name.setText(item.getName());
        Quantity.setText("1");
    }
    @FXML
    private void selectFromSellTbl(){
        Bill_Type item = sellTBL.getSelectionModel().getSelectedItem();
        Id.setText(String.valueOf(item.getId()));
        Name.setText(item.getName());
        Quantity.setText(String.valueOf(item.getQuantity()));
    }
    private void fillTotal(){
        Double total = 0.0;
      for(Bill_Type item: SellList){
          total+= item.getTotal();
      }
      Total.setText(String.valueOf(total));
    }
    private void fillSellTbl() throws SQLException {
        sellTBL.setItems(SellList);
        sellTBL.refresh();
        refreshTable();
        fillTotal();
    }
    private void remove() throws SQLException{
        int myindex=getIndexbyId();
        if(myindex!=-1){
            editProduct(getquantityById()+sellTBL.getItems().get(myindex).getQuantity());
            sellTBL.getItems().remove(myindex);
            fillSellTbl();
            clear();
        }
    }
    @FXML
    private void RemoveSell(){
        if(Id.getText().isEmpty()||Name.getText().isEmpty()||Quantity.getText().isEmpty()){

        }else{
            try {
                remove();
            } catch (SQLException ex) {
            }
        }
    }
    private void edit() throws SQLException{
        int myindex=getIndexbyId();
        if(myindex!= -1){
            if(sellTBL.getItems().get(myindex).getQuantity()==Integer.parseInt(Quantity.getText())){

            }else{
                if(Integer.parseInt(Quantity.getText())==0){
                    if(JOptionPane.showConfirmDialog(null, "")==0){
                        remove();
                    }else{
                    }
                }else{
                    if(Integer.parseInt(Quantity.getText()) < getquantityById()){
                        editProduct(getquantityById()+(sellTBL.getItems().get(myindex).getQuantity()-Integer.parseInt(Quantity.getText())));
                        int quantity = Integer.parseInt(Quantity.getText());
                        Double total = quantity*getpriceById();
                        SellList.get(myindex).setQuantity(quantity);
                        SellList.get(myindex).setTotal(total);
                        fillSellTbl();
                    } else {

                    }
                }
            }
        }

    }
    @FXML
    private void EditSell(){
        if(Id.getText().isEmpty()||Name.getText().isEmpty()||Quantity.getText().isEmpty()){

        }else{
            try {
                edit();

            } catch (SQLException ex) {
            }
        }
    }
    @FXML
    private void getBill(){
        AnchorBillP.setDisable(true);
        anchorBill.setVisible(true);
        fillBillTxt();
    }
    private void savetxt(String str){
        try {
            FileWriter myWriter;

                myWriter = new FileWriter("filename.txt");
                myWriter.write(str);
                myWriter.close();

        } catch (IOException e) {
        }

    }
    private void fillBillTxt(){
        int i = 0;
        String bill =  "================ FAMILY POINT ================\n"+
                       " NUM PRODUCT                QUANTITIY    TOTAL";
        i = 1;
        for(Bill_Type item : SellList){
            String id = String.format("%4s ", i);
            String name = String.format("%-21s ",item.getName());
            String quantity = String.format("%10s ", item.getQuantity());
            String total = String.format("%8s", item.getTotal());
            bill +="\n"+ id + name + quantity + total;
            i++;
        }
        bill+= "\n";
        String total = String.format("%30S","")+"Total : "+Total.getText();
        bill+= total;
        savetxt(bill);
        BillTXT.setText(bill);
    }
    @FXML
    private void printB(){
        try {
            PrinterJob job = PrinterJob.createPrinterJob();
            // Montre la boite de dialogue
            boolean proceed = job.showPrintDialog(null);
                // Imprime la zone texte
                boolean printed = job.printPage(BillTXT);

                if (printed) {
                    job.endJob();
                }
            }
        catch (Exception ex) {

        }
    }

    @FXML
    private void close(){
        AnchorBillP.setDisable(false);
        anchorBill.setVisible(false);
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
    public void callCat(MouseEvent event)throws IOException{
        exit(event);
        callStage(new Stage(),"Category.fxml");
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
