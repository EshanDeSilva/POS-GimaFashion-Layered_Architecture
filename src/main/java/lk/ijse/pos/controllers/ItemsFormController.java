package lk.ijse.pos.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import lk.ijse.pos.bo.BoFactory;
import lk.ijse.pos.bo.custom.SupplierBo;
import lk.ijse.pos.bo.custom.SupplierInvoiceBo;
import lk.ijse.pos.bo.custom.impl.SupplierInvoiceBoImpl;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.impl.CategoryDaoImpl;
import lk.ijse.pos.dao.custom.impl.ItemDaoImpl;
import lk.ijse.pos.dao.custom.impl.SupplierDaoImpl;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.*;
import lk.ijse.pos.model.tm.ItemTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ItemsFormController {
    public BorderPane itemPane;
    public JFXTextField txtSearch;
    public JFXTextField txtCode;
    public JFXTextField txtDescription;
    public JFXTextField txtQty;
    public JFXTextField txtSellingPrice;
    public JFXComboBox cmbId;
    public JFXComboBox cmbSupplierName;
    public JFXButton btnSave;
    public JFXTextField txtBuyingPrice;
    public JFXTextField txtSize;
    public JFXTextField txtType;
    public JFXComboBox cmbSize;
    public JFXComboBox cmbType;
    public Label lblProfit;
    public JFXTreeTableView<ItemTm> tblItem;
    public TreeTableColumn colCode;
    public TreeTableColumn colDescription;
    public TreeTableColumn colQty;
    public TreeTableColumn colSellingPrice;
    public TreeTableColumn colBuyingPrice;
    public TreeTableColumn colSize;
    public TreeTableColumn colType;
    public TreeTableColumn colProfit;
    public TreeTableColumn colSupplierID;
    public TreeTableColumn colOption;
    public JFXButton btnAddStock;
    public JFXButton btnAdd;
    public JFXTextField txtAddQty;

//    SupplierDaoImpl supplierDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SUPPLIER);
//    SupplierInvoiceDaoImpl supplierInvoiceDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SUPPLIER_INVOICE);
    ItemDaoImpl itemDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ITEM);
    CategoryDaoImpl categoryDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.CATEGORY);

    SupplierInvoiceBo supplierInvoiceBo = BoFactory.getInstance().getBoType(BoFactory.BoType.SUPPLIER_INVOICE_BO);
    SupplierBo supplierBo = BoFactory.getInstance().getBoType(BoFactory.BoType.SUPPLIER_BO);

    public void initialize(){

        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colSellingPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("sellingPrice"));
        colBuyingPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("buyingPrice"));
        colSize.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));
        colType.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        colProfit.setCellValueFactory(new TreeItemPropertyValueFactory<>("profit"));
        colSupplierID.setCellValueFactory(new TreeItemPropertyValueFactory<>("supplierId"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        loadCode();
        loadSuppliers();
        txtSize.setDisable(true);
        txtType.setDisable(true);
        btnAddStock.setDisable(true);
        txtAddQty.setDisable(true);
        loadProfit();
        loadTypes();
        loadCategories();
        loadTable();

        txtAddQty.setOnKeyReleased(keyEvent -> {
            if (txtAddQty.getText()!=null && !txtAddQty.getText().isEmpty() && txtAddQty.getText().matches("[0-9]")) {
                btnAddStock.setDisable(false);
            } else {
                btnAddStock.setDisable(true);
                new Alert(Alert.AlertType.WARNING, "Please Enter only 0-9 values..!").show();
            }
        });
        txtQty.setOnKeyReleased(keyEvent -> {
            if (txtQty.getText()!=null && !txtQty.getText().isEmpty() && txtQty.getText().matches("[0-9]")) {

            } else {
                new Alert(Alert.AlertType.WARNING, "Please Enter only 0-9 values..!").show();
            }
        });

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                tblItem.setPredicate(new Predicate<TreeItem<ItemTm>>() {
                    @Override
                    public boolean test(TreeItem<ItemTm> treeItem) {
                        boolean flag = treeItem.getValue().getCode().contains(newValue) ||
                                treeItem.getValue().getDescription().contains(newValue) ||
                                treeItem.getValue().getSupplierId().contains(newValue) ||
                                treeItem.getValue().getType().contains(newValue);
                        return flag;
                    }
                });
            }
        });

        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        } );
    }

    private void setData(TreeItem<ItemTm> newValue) {
        btnSave.setText("Update");
        txtCode.setEditable(false);
        txtAddQty.setDisable(false);
        txtCode.setText(newValue.getValue().getCode());
        cmbId.setValue(newValue.getValue().getSupplierId());
        txtDescription.setText(newValue.getValue().getDescription());
        txtQty.setText(newValue.getValue().getQty());
        txtSellingPrice.setText(newValue.getValue().getSellingPrice());
        txtBuyingPrice.setText(newValue.getValue().getBuyingPrice());
        cmbType.setValue(newValue.getValue().getType());
        cmbSize.setValue(newValue.getValue().getSize());
        lblProfit.setText(String.valueOf(Double.parseDouble(txtSellingPrice.getText())-Double.parseDouble(txtBuyingPrice.getText())));
    }

    private void loadCategories() {
        txtType.setDisable(true);
        txtSize.setDisable(true);
        btnAdd.setDisable(true);
        cmbType.setOnAction(event -> {
            if (cmbType.getValue()!=null){
                if (!cmbType.getValue().equals("Custom") && !cmbType.getValue().toString().isEmpty()){
                    cmbSize.getItems().clear();
                    loadSizes();
                    txtType.setDisable(true);
                    txtSize.clear();
                    txtSize.setDisable(true);
                }else if (cmbType.getValue().equals("Custom")){
                    cmbSize.getItems().clear();
                    txtType.setDisable(false);
                    txtSize.setDisable(false);
                }
            }else{
                cmbSize.getItems().clear();
                txtType.setDisable(true);
                txtSize.clear();
                txtSize.setDisable(true);
            }
        });
        cmbSize.setOnAction(event1 -> {
            if (cmbSize.getValue()!=null) {
                if (cmbSize.getValue().equals("Custom")) {
                    //txtType.setDisable(false);
                    txtSize.setDisable(false);
                } else {
                    //txtType.setDisable(true);
                    txtSize.clear();
                    txtSize.setDisable(true);
                }
            }else{
                txtSize.clear();
                txtSize.setDisable(true);
            }
        });
        txtSize.setOnKeyReleased(keyEvent -> {
            if (!txtSize.getText().isEmpty()){
                btnAdd.setDisable(false);
            }else{
                btnAdd.setDisable(true);
            }
        });
        txtType.setOnKeyReleased(keyEvent -> {
            if (!txtSize.getText().isEmpty()){
                btnAdd.setDisable(false);
            }else{
                btnAdd.setDisable(true);
            }
        });
    }

    private void loadTypes() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.add("Custom");
        try {
            List<CategoryDto> categories = categoryDao.findAllTypes();
            for (CategoryDto dto:categories) {
                typeList.add(dto.getType());
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        cmbType.setItems(typeList);
    }

    private void loadSizes() {
        ObservableList<String> sizeList = FXCollections.observableArrayList();
        sizeList.add("Custom");
        try {
            List<CategoryDto> categories = categoryDao.findAllSize(cmbType.getValue().toString());
            for (CategoryDto dto:categories) {
                sizeList.add(dto.getSize());
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        cmbSize.setItems(sizeList);
    }

    private void loadProfit() {
        txtSellingPrice.setOnKeyReleased(ke -> {
            if (txtSellingPrice.getText()!=null && txtSellingPrice.getText().matches("^-?\\d+(\\.\\d{2}+)?$")) {
                txtSellingPrice.setOnKeyTyped(actionEvent -> {
                    if (!txtSellingPrice.getText().isEmpty() && !txtBuyingPrice.getText().isEmpty()) {
                        String profit = String.format("%8.2f", Double.parseDouble(txtSellingPrice.getText()) - Double.parseDouble(txtBuyingPrice.getText()));
                        lblProfit.setText(profit);
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "Please Enter only 0-9 values..!").show();
            }
        });
        txtBuyingPrice.setOnKeyReleased(ke -> {
            if (txtBuyingPrice.getText()!=null && txtBuyingPrice.getText().matches("^-?\\d+(\\.\\d{2}+)?$")) {
                txtBuyingPrice.setOnKeyTyped(actionEvent -> {
                    if (!txtSellingPrice.getText().isEmpty() && !txtBuyingPrice.getText().isEmpty()) {
                        String profit = String.format("%8.2f", Double.parseDouble(txtSellingPrice.getText()) - Double.parseDouble(txtBuyingPrice.getText()));
                        lblProfit.setText(profit);
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "Please Enter only 0-9 values..!").show();
            }
        });
    }

    private void loadSuppliers() {
        try {
            List<SupplierDto> suppliers = supplierBo.findAllSuppliers();
            ObservableList<String> idList = FXCollections.observableArrayList();
            ObservableList<String> nameList = FXCollections.observableArrayList();
            for (SupplierDto dto:suppliers) {
                idList.add(dto.getSupplierId());
                nameList.add(dto.getName());
            }
            cmbId.setItems(idList);
            cmbSupplierName.setItems(nameList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbId.setOnAction(actionEvent -> {
            if (cmbId.getValue()!=null) {
                try {
                    cmbSupplierName.setValue(supplierBo.findSupplier(cmbId.getValue().toString()).getName());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        cmbSupplierName.setOnAction(actionEvent -> {
            if (cmbSupplierName.getValue()!=null) {
                try {
                    cmbId.setValue(supplierBo.findSupplierByName(cmbSupplierName.getValue().toString()).getSupplierId());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadCode() {
        try {
            txtCode.setText(itemDao.getId());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) itemPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/DashBoardForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Gima Fashion");
        stage.centerOnScreen();
        stage.show();
    }

    private void loadTable(){
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();
        try {
            List<ItemDto> items = itemDao.findAll();
            for (ItemDto dto:items) {
                JFXButton btn = new JFXButton("Delete");
                btn.setBackground(Background.fill(Color.rgb(255, 121, 121)));
                btn.cursorProperty().set(Cursor.HAND);

                btn.setOnAction(actionEvent -> {
                    Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete "+dto.getCode()+" ?", ButtonType.NO, ButtonType.YES).showAndWait();
                    if (buttonType.get()==ButtonType.YES){
                        try {
                            boolean isDeleted = itemDao.delete(dto.getCode());
                            if (isDeleted) {
                                new Alert(Alert.AlertType.INFORMATION, "Deleted..!").show();
                                //loadTable();
                                //loadId();
                                clearDataFields();
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                            }
                        }catch (SQLException | ClassNotFoundException e){
                            e.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                });

                tmList.add(new ItemTm(
                        dto.getCode(),
                        dto.getDescription(),
                        dto.getQty(),
                        dto.getSellingPrice(),
                        dto.getBuyingPrice(),
                        dto.getCategoryDto().getSize(),
                        dto.getCategoryDto().getType(),
                        String.valueOf(Double.parseDouble(dto.getSellingPrice())-Double.parseDouble(dto.getBuyingPrice())),
                        dto.getSupplierId(),
                        btn
                ));
            }
            TreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            tblItem.setRoot(treeItem);
            tblItem.setShowRoot(false);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
        }
    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        if (btnSave.getText().equals("Save") && !txtCode.getText().isEmpty() && cmbId.getValue()!=null &&
                !txtDescription.getText().isEmpty() && !txtQty.getText().isEmpty() &&
                !txtBuyingPrice.getText().isEmpty() && !txtSellingPrice.getText().isEmpty() &&
                cmbType.getValue()!=null && cmbSize.getValue()!=null && !cmbSize.getValue().equals("Custom")) {
            //if (cmbSize.getValue()!=null && !cmbSize.getValue().toString().isEmpty() && !cmbSize.getValue().toString().equals("Custom")) {
                try {
                    boolean isSaved = supplierInvoiceBo.saveSupplierInvoice(new SupplierInvoiceDto(supplierInvoiceBo.getId(),
                            cmbId.getValue().toString(), txtCode.getText(), LocalDateTime.now().
                            format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), txtQty.getText(), new ItemDto(
                            txtCode.getText(), cmbId.getValue().toString(), txtDescription.getText(),
                            txtQty.getText(), txtSellingPrice.getText(), txtBuyingPrice.getText(),
                            categoryDao.find(cmbType.getValue().toString(), cmbSize.getValue().toString()))
                    ));
                    if (isSaved) {
                        //loadCode();
                        clearDataFields();
                        new Alert(Alert.AlertType.INFORMATION, "Item Saved..!").show();
                        //loadTable();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            /*}else{
                new Alert(Alert.AlertType.ERROR, "Please Select Type and Size..!").show();
            }*/
        }else if (btnSave.getText().equals("Update") && !txtCode.getText().isEmpty() && cmbId.getValue()!=null &&
                !txtDescription.getText().isEmpty() && !txtQty.getText().isEmpty() &&
                !txtBuyingPrice.getText().isEmpty() && !txtSellingPrice.getText().isEmpty() &&
                cmbType.getValue()!=null && cmbSize.getValue()!=null && !cmbSize.getValue().equals("Custom")) {
            try {
                boolean isUpdated = itemDao.update(new ItemDto(
                        txtCode.getText(), cmbId.getValue().toString(), txtDescription.getText(),
                        txtQty.getText(), txtSellingPrice.getText(), txtBuyingPrice.getText(),
                        categoryDao.find(cmbType.getValue().toString(), cmbSize.getValue().toString())
                ));
                if (isUpdated) {
                    //loadCode();
                    clearDataFields();
                    new Alert(Alert.AlertType.INFORMATION, "Item Updated..!").show();
                    //loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
        }
    }

    public void addStockButtonOnAction(ActionEvent actionEvent) {
        if (btnSave.getText().equals("Update") && !txtCode.getText().isEmpty() && cmbId.getValue()!=null &&
                !txtDescription.getText().isEmpty() && !txtQty.getText().isEmpty() &&
                !txtBuyingPrice.getText().isEmpty() && !txtSellingPrice.getText().isEmpty() &&
                !cmbType.getValue().toString().isEmpty() && !cmbSize.getValue().toString().isEmpty() && !btnAddStock.isDisable()) {
            try {
                boolean isUpdated = supplierInvoiceBo.addInvoiceStock(
                        new SupplierInvoiceDto(supplierInvoiceBo.getId(),cmbId.getValue().toString(),
                                txtCode.getText(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),txtAddQty.getText(),new ItemDto(
                                txtCode.getText(), cmbId.getValue().toString(), txtDescription.getText(),
                                String.valueOf(Integer.parseInt(txtQty.getText())+Integer.parseInt(txtAddQty.getText())), txtBuyingPrice.getText(), txtSellingPrice.getText(),
                                categoryDao.find(cmbType.getValue().toString(), cmbSize.getValue().toString())
                        )));
                if (isUpdated) {
                    //loadCode();
                    clearDataFields();
                    new Alert(Alert.AlertType.INFORMATION, "Stock Added..!").show();
                    //loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    public void clearButtonOnAction(ActionEvent actionEvent) {
        clearDataFields();
    }

    private void clearDataFields() {
        loadCode();
        loadSuppliers();
        txtAddQty.setDisable(true);
        txtCode.setEditable(true);
        txtCode.setDisable(false);
        cmbId.setValue(null);
        cmbSupplierName.setValue(null);
        txtDescription.clear();
        txtQty.clear();
        txtAddQty.clear();
        txtBuyingPrice.clear();
        txtSellingPrice.clear();
        cmbType.setValue(null);
        txtType.clear();
        txtSize.clear();
        lblProfit.setText(null);
        loadTypes();
        if (cmbType.getValue()!=null&&!cmbType.getValue().toString().isEmpty()) {
            cmbSize.getItems().clear();
            loadSizes();
        }
        btnAddStock.setDisable(true);
        btnAdd.setDisable(true);
        btnSave.setText("Save");
        loadTable();
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        String type=cmbType.getValue().toString();
        if (!txtType.isDisable()){
            type=txtType.getText();
        }
        try{
            Boolean isSaved = categoryDao.save(new CategoryDto(categoryDao.getId(), txtSize.getText(), type));
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Saved..!").show();
                loadSizes();
                //clearDataFields();
                cmbType.setValue(null);
                cmbSize.setValue(null);
                txtType.clear();
                txtSize.clear();
                loadTypes();
                if (cmbType.getValue()!=null&&!cmbType.getValue().toString().isEmpty()) {
                    cmbSize.getItems().clear();
                    loadSizes();
                }
                btnAdd.setDisable(true);
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went Wrong..!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went Wrong..!").show();
        }
    }

    public void printButtonOnAction(ActionEvent event) {
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Items.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT * FROM item INNER JOIN category ON category.categoryId=item.categoryId");
            jDesign.setQuery(query);

            JasperReport compileReport = JasperCompileManager.compileReport(jDesign);
            java.util.Map params = new java.util.HashMap();
            params.put( "Date", String.valueOf(LocalDate.now()));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, params, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
