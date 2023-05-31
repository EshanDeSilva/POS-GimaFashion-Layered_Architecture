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
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.SupplierDto;
import lk.ijse.pos.model.tm.SupplierTm;
import lk.ijse.pos.model.tm.SuppliesTm;
import lk.ijse.pos.dao.custom.impl.SupplierDaoImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SuppliersFormController {
    public BorderPane supplierPane;
    public JFXTreeTableView<SupplierTm> suppliersTable;
    public JFXTreeTableView<SuppliesTm> suppliesTable;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXComboBox cmbTitle;
    public JFXTextField txtCompany;
    public JFXTextField txtContact;
    public JFXButton btnSave;
    public JFXTextField txtSearch;
    public TreeTableColumn colId;
    public TreeTableColumn colTitle;
    public TreeTableColumn colName;
    public TreeTableColumn colCompany;
    public TreeTableColumn colContact;
    public TreeTableColumn colOption;
    public TreeTableColumn colItemCode;
    public TreeTableColumn colDescription;
    public TreeTableColumn colQty;

    public void initialize(){

        colId.setCellValueFactory(new TreeItemPropertyValueFactory<>("supplierId"));
        colTitle.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new TreeItemPropertyValueFactory<>("contact"));
        colCompany.setCellValueFactory(new TreeItemPropertyValueFactory<>("company"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));

        ObservableList<String> title = FXCollections.observableArrayList("Mr", "Mrs", "Miss");
        cmbTitle.setItems(title);
        loadId();
        loadSupplierTable();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) { {
                    suppliersTable.setPredicate(new Predicate<TreeItem<SupplierTm>>() {
                        @Override
                        public boolean test(TreeItem<SupplierTm> treeItem) {
                            boolean flag = treeItem.getValue().getSupplierId().contains(newValue) ||
                                    treeItem.getValue().getName().contains(newValue) ||
                                    treeItem.getValue().getCompany().contains(newValue);
                            return flag;
                        }
                    });
                }
            }
        });

        suppliersTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        } );

    }

    private void setData(TreeItem<SupplierTm> newValue) {
        txtId.setText(newValue.getValue().getSupplierId());
        cmbTitle.setValue(newValue.getValue().getTitle());
        txtName.setText(newValue.getValue().getName());
        txtCompany.setText(newValue.getValue().getCompany());
        txtContact.setText(newValue.getValue().getContact());
        btnSave.setText("Update");
        txtId.setDisable(true);
        setSupplies(newValue.getValue().getSupplierId());
    }

    private void setSupplies(String id) {
        try {
            ObservableList<SuppliesTm> list = SupplierDaoImpl.getItems(id);
            TreeItem<SuppliesTm> treeItem = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
            suppliesTable.setRoot(treeItem);
            suppliesTable.setShowRoot(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadId() {
        try {
            txtId.setText(SupplierDaoImpl.getId());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSuppliesColumns(){
        /*List<JFXTreeTableColumn> columns = new ArrayList<>();
        columns.add(new JFXTreeTableColumn("Code"));
        columns.add(new JFXTreeTableColumn("Description"));
        columns.add(new JFXTreeTableColumn("Qty"));
        columns.add(new JFXTreeTableColumn("UnitPrice"));
        columns.get(0).setPrefWidth(80);
        columns.get(1).setPrefWidth(180);
        columns.get(2).setPrefWidth(80);
        columns.get(3).setPrefWidth(110);

        suppliesTable.getColumns().addAll(columns);*/
    }
    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) supplierPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/DashBoardForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Gima Fashion");
        stage.centerOnScreen();
        stage.show();
    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        if (btnSave.getText().equals("Save") && !txtId.getText().isEmpty() && !txtName.getText().isEmpty() && !txtCompany.getText().isEmpty() && !txtContact.getText().isEmpty()) {
            try {
                Boolean isSaved = SupplierDaoImpl.save(new SupplierDto(
                        txtId.getText(), cmbTitle.getValue().toString(),
                        txtName.getText(), txtCompany.getText(), txtContact.getText()
                ));
                if (isSaved) {
                    loadId();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Saved..!").show();
                    loadSupplierTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }else if (btnSave.getText().equals("Update") && !txtId.getText().isEmpty() && !txtName.getText().isEmpty() && !txtCompany.getText().isEmpty() && !txtContact.getText().isEmpty()){
            try {
                Boolean isUpdated = SupplierDaoImpl.update(new SupplierDto(
                        txtId.getText(), cmbTitle.getValue().toString(),
                        txtName.getText(), txtCompany.getText(), txtContact.getText()
                ));
                if (isUpdated) {
                    loadId();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Updated..!").show();
                    loadSupplierTable();
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

    private void loadSupplierTable() {
        ObservableList<SupplierTm> tmList = FXCollections.observableArrayList();
        try {
            List<SupplierDto> suppliers = SupplierDaoImpl.findAll();
            for (SupplierDto dto:suppliers) {
                JFXButton btn = new JFXButton("Delete");
                btn.setBackground(Background.fill(Color.rgb(255, 121, 121)));
                btn.cursorProperty().set(Cursor.HAND);

                btn.setOnAction(actionEvent -> {
                    Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete "+dto.getSupplierId()+" ?", ButtonType.NO, ButtonType.YES).showAndWait();
                    if (buttonType.get()==ButtonType.YES){
                        try {
                            Boolean isDeleted = SupplierDaoImpl.delete(dto.getSupplierId());
                            if (isDeleted) {
                                new Alert(Alert.AlertType.INFORMATION, "Deleted..!").show();
                                loadSupplierTable();
                                loadId();
                                clearFields();
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                            }
                        }catch (SQLException | ClassNotFoundException e){
                            e.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                });

                tmList.add(new SupplierTm(
                        dto.getSupplierId(),
                        dto.getTitle(),
                        dto.getName(),
                        dto.getCompany(),
                        dto.getContact(),
                        btn
                ));
            }
            TreeItem<SupplierTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            suppliersTable.setRoot(treeItem);
            suppliersTable.setShowRoot(false);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
        }
    }

    private void clearFields() {
        loadId();
        cmbTitle.setValue(null);
        txtName.clear();
        txtCompany.clear();
        txtContact.clear();
        txtSearch.clear();
        btnSave.setText("Save");
        loadSupplierTable();
        txtId.setDisable(false);
        suppliesTable.setRoot(null);
    }

    public void clearButtonOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void printButtonOnAction(ActionEvent event) {
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Suppliers.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT * FROM supplier");
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
