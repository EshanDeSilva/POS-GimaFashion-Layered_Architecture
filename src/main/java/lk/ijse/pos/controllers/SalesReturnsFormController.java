package lk.ijse.pos.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.OrderDetailsDto;
import lk.ijse.pos.model.SalesReturnDetailsDto;
import lk.ijse.pos.model.SalesReturnDto;
import lk.ijse.pos.model.tm.OrderTm;
import lk.ijse.pos.dao.custom.impl.ItemDaoImpl;
import lk.ijse.pos.dao.custom.impl.OrderDetailsDaoImpl;
import lk.ijse.pos.dao.custom.impl.SalesReturnDaoImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesReturnsFormController {

    public BorderPane returnPane;
    public JFXTreeTableView<OrderTm> tblOrders;
    public TreeTableColumn colItemCode;
    public TreeTableColumn colDescription;
    public TreeTableColumn colQty;
    public TreeTableColumn colUnitPrice;
    public TreeTableColumn colDate;
    public TreeTableColumn colDiscount;
    public TreeTableColumn colType;
    public TreeTableColumn colSize;
    public TreeTableColumn colAmount;
    public TreeTableColumn colOption;
    public JFXTextField txtOrderId;
    public Label lblTotal;
    public JFXTextField txtOrderQuantity;
    public JFXButton btnUpdate;

    public void initialize(){
        getOrder();
        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        colDate.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        colDiscount.setCellValueFactory(new TreeItemPropertyValueFactory<>("discount"));
        colType.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        colSize.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        btnUpdate.setDisable(true);

        tblOrders.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setQty(newValue);
        });
    }

    String code = null;
    private void setQty(TreeItem<OrderTm> newValue) {
        if (newValue!=null) {
            btnUpdate.setDisable(false);
            txtOrderQuantity.setText(String.valueOf(newValue.getValue().getQty()));
            code = newValue.getValue().getItemCode();
            txtOrderQuantity.setOnAction(event -> {
                update(code);
            });
        }
    }

    private void update(String itemCode){
        for (OrderTm tm:tmList) {
            if (tm.getItemCode().equals(itemCode)){
                if (!txtOrderQuantity.getText().isEmpty() && txtOrderQuantity.getText().matches("[0-9]") && Integer.parseInt(txtOrderQuantity.getText())>0 && Integer.parseInt(txtOrderQuantity.getText())<=tm.getQty()) {
                    tm.setQty(Integer.parseInt(txtOrderQuantity.getText()));
                    tm.setAmount(((tm.getUnitPrice() - ((tm.getUnitPrice() / 100) * tm.getDiscount())) * tm.getQty()));
                    refreshTable();
                }else{
                    new Alert(Alert.AlertType.ERROR,"Something went Wrong..!").show();
                }
            }
        }
    }

    List<OrderDetailsDto> list = new ArrayList<>();
    ObservableList<OrderTm> tmList = FXCollections.observableArrayList();
    private void getOrder() {
        txtOrderId.setOnAction(event -> {
            if (txtOrderId.getText().matches("^ORD-\\d{8,20}$")){
                tmList.clear();
                list.clear();
                txtOrderQuantity.clear();
                lblTotal.setText("");
                double total=0;
                try {
                    list = OrderDetailsDaoImpl.getAll(txtOrderId.getText());
                    for (int i = 0; i < list.size(); i++) {
                        JFXButton btn = new JFXButton("Delete");
                        btn.setCursor(Cursor.HAND);
                        btn.setBackground(Background.fill(Color.rgb(255, 121, 121)));
                        tmList.add(new OrderTm(
                                list.get(i).getItemCode(),
                                ItemDaoImpl.find(list.get(i).getItemCode()).getDescription(),
                                list.get(i).getOrderQty(),
                                list.get(i).getUnitPrice(),
                                LocalDate.now().toString(),
                                list.get(i).getDiscRate(),
                                ItemDaoImpl.find(list.get(i).getItemCode()).getCategoryDto().getType(),
                                ItemDaoImpl.find(list.get(i).getItemCode()).getCategoryDto().getSize(),
                                ((list.get(i).getUnitPrice()-((list.get(i).getUnitPrice()/100)*list.get(i).getDiscRate()))*list.get(i).getOrderQty()),
                                btn
                        ));
                        for (OrderTm tm:tmList) {
                            total += tm.getAmount();
                        }
                        lblTotal.setText(String.valueOf(total));
                    }
                    for (OrderTm tm: tmList) {
                        JFXButton btn = tm.getBtn();

                        btn.setOnAction(actionEvent -> {
                            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this item ?", ButtonType.NO, ButtonType.YES).showAndWait();
                            if (buttonType.get()==ButtonType.YES){
                                tmList.remove(tm);
                                refreshTable();
                            }
                        });
                    }
                    TreeItem<OrderTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
                    tblOrders.setRoot(treeItem);
                    tblOrders.setShowRoot(false);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }else{
                new Alert(Alert.AlertType.ERROR,"Invalid order ID format. use - \"ORD-********\"").show();
            }
        });
    }

    private void refreshTable() {
        double total = 0;
        tblOrders.refresh();
        for (OrderTm tm:tmList) {
            total += tm.getAmount();
        }
        lblTotal.setText(String.valueOf(total));
    }

    public void backButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) returnPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/DashBoardForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Gima Fashion");
        stage.centerOnScreen();
        stage.show();
    }

    public void updateButtonOnAction(ActionEvent event) {
        update(code);
    }

    public void clearButtonOnAction(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {
        tmList.clear();
        //list.clear();
        lblTotal.setText("");
        txtOrderQuantity.clear();
        btnUpdate.setDisable(true);
        txtOrderId.clear();
        tblOrders.setRoot(null);
    }

    public void placeReturnButtonOnAction(ActionEvent event) {
        try {
            String returnId = SalesReturnDaoImpl.getId();
            List<SalesReturnDetailsDto> returnItemList = new ArrayList<>();
            for (OrderTm tm:tmList) {
                returnItemList.add(new SalesReturnDetailsDto(
                        returnId,
                        tm.getItemCode(),
                        tm.getQty(),
                        tm.getDiscount(),
                        tm.getUnitPrice(),
                        tm.getQty()*(tm.getUnitPrice()-((tm.getUnitPrice()/100)*tm.getDiscount()))
                ));
            }
            Boolean isSaved = SalesReturnDaoImpl.save(new SalesReturnDto(
                    returnId,
                    txtOrderId.getText(),
                    LocalDate.now().toString(),
                    Double.parseDouble(lblTotal.getText()),
                    returnItemList
            ));
            if (isSaved) {
                Boolean allAdded = true;
                for (OrderTm tm:tmList) {
                    Boolean isAdded = ItemDaoImpl.addStock(tm.getItemCode(),tm.getQty());
                    if (!isAdded){
                        allAdded = false;
                    }
                }
                if (allAdded) {
                    new Alert(Alert.AlertType.INFORMATION, "Return Saved..!").show();
                    try {
                        JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Return.jrxml");
                        JRDesignQuery query = new JRDesignQuery();
                        query.setText("SELECT * FROM salesReturn INNER JOIN salesReturnDetails ON salesReturn.returnId = salesReturnDetails.returnId INNER JOIN item ON salesReturnDetails.itemCode=item.itemCode WHERE salesReturn.returnId = '"+returnId+"'");
                        jDesign.setQuery(query);

                        JasperReport compileReport = JasperCompileManager.compileReport(jDesign);
                        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DBConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException | SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    clearForm();
                }
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
