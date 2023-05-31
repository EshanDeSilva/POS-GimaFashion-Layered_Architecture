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
import javafx.util.StringConverter;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.EmployerDto;
import lk.ijse.pos.model.tm.EmployerTm;
import lk.ijse.pos.dao.custom.impl.EmployerDaoImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class EmployersFormController {

    public BorderPane employerPane;
    public JFXTreeTableView<EmployerTm> tblEmployee;
    public TreeTableColumn colId;
    public TreeTableColumn colTitle;
    public TreeTableColumn colName;
    public TreeTableColumn colNIC;
    public TreeTableColumn colDOB;
    public TreeTableColumn colAddress;
    public TreeTableColumn colContact;
    public TreeTableColumn colBankAcc;
    public TreeTableColumn colBankBranch;
    public TreeTableColumn colOption;
    public JFXTextField txtSearch;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXComboBox cmbTitle;
    public JFXTextField txtAddress;
    public JFXTextField txtNIC;
    public JFXButton btnSave;
    public DatePicker datePickerDob;
    public JFXTextField txtBankAcc;
    public JFXTextField txtBankBranch;
    public JFXTextField txtContact;

    EmployerDaoImpl employerDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.EMPLOYER);

    public void initialize(){

        colId.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        colNIC.setCellValueFactory(new TreeItemPropertyValueFactory<>("nic"));
        colDOB.setCellValueFactory(new TreeItemPropertyValueFactory<>("dob"));
        colAddress.setCellValueFactory(new TreeItemPropertyValueFactory<>("address"));
        colBankAcc.setCellValueFactory(new TreeItemPropertyValueFactory<>("bankAcc"));
        colBankBranch.setCellValueFactory(new TreeItemPropertyValueFactory<>("bankBranch"));
        colContact.setCellValueFactory(new TreeItemPropertyValueFactory<>("contact"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        dateFormatting(datePickerDob);
        loadTable();
        loadId();

        ObservableList<String> title = FXCollections.observableArrayList("Mr", "Mrs", "Miss");
        cmbTitle.setItems(title);

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                tblEmployee.setPredicate(new Predicate<TreeItem<EmployerTm>>() {
                    @Override
                    public boolean test(TreeItem<EmployerTm> treeItem) {
                        boolean flag = treeItem.getValue().getId().contains(newValue) ||
                                treeItem.getValue().getName().contains(newValue) ||
                                treeItem.getValue().getBankAcc().contains(newValue) ||
                                treeItem.getValue().getNic().contains(newValue);
                        return flag;
                    }
                });
            }
        });

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        } );
    }

    private void setData(TreeItem<EmployerTm> newValue) {
        txtId.setText(newValue.getValue().getId());
        cmbTitle.setValue(newValue.getValue().getTitle());
        txtName.setText(newValue.getValue().getName());
        txtNIC.setText(newValue.getValue().getNic());
        datePickerDob.setValue(date(newValue.getValue().getDob()));
        txtAddress.setText(newValue.getValue().getAddress());
        txtBankAcc.setText(newValue.getValue().getBankAcc());
        txtBankBranch.setText(newValue.getValue().getBankBranch());
        txtContact.setText(newValue.getValue().getContact());
        btnSave.setText("Update");
        txtId.setDisable(true);
    }

    private LocalDate date(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    private void dateFormatting(DatePicker datePicker){
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if(dateString==null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) employerPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/DashBoardForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Gima Fashion");
        stage.centerOnScreen();
        stage.show();
    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        if (btnSave.getText().equals("Save") && !txtId.getText().isEmpty() && !txtName.getText().isEmpty() && !txtAddress.getText().isEmpty() && !txtContact.getText().isEmpty() &&
        !txtNIC.getText().isEmpty() && !cmbTitle.getValue().toString().isEmpty() && !datePickerDob.getValue().toString().isEmpty()) {
            try {
                boolean isSaved = employerDao.save(new EmployerDto(
                        txtId.getText(), cmbTitle.getValue().toString(), txtName.getText(),
                        txtNIC.getText(), datePickerDob.getValue().toString(), txtAddress.getText(),
                        txtBankAcc.getText(), txtBankBranch.getText(), txtContact.getText()
                ));
                if (isSaved) {
                    loadId();
                    clearDataFields();
                    new Alert(Alert.AlertType.INFORMATION, "Employer Saved..!").show();
                    loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }else if (btnSave.getText().equals("Update") && !txtId.getText().isEmpty() && !txtName.getText().isEmpty() && !txtAddress.getText().isEmpty() && !txtContact.getText().isEmpty() &&
                !txtNIC.getText().isEmpty() && !cmbTitle.getValue().toString().isEmpty() && !datePickerDob.getValue().toString().isEmpty()) {
            try {
                boolean isUpdated = employerDao.update(new EmployerDto(
                        txtId.getText(), cmbTitle.getValue().toString(), txtName.getText(),
                        txtNIC.getText(), datePickerDob.getValue().toString(), txtAddress.getText(),
                        txtBankAcc.getText(), txtBankBranch.getText(), txtContact.getText()
                ));
                if (isUpdated) {
                    loadId();
                    clearDataFields();
                    new Alert(Alert.AlertType.INFORMATION, "Employer Updated..!").show();
                    loadTable();
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

    public void clearButtonOnAction(ActionEvent actionEvent) {
        clearDataFields();
    }

    private void clearDataFields() {
        loadId();
        txtName.clear();
        txtNIC.clear();
        txtAddress.clear();
        cmbTitle.setValue(null);
        txtContact.clear();
        datePickerDob.setValue(null);
        txtBankAcc.clear();
        txtBankBranch.clear();
        txtSearch.clear();
        loadTable();
        btnSave.setText("Save");
        txtId.setDisable(false);
    }

    private void loadTable() {
        ObservableList<EmployerTm> tmList = FXCollections.observableArrayList();
        try {
            List<EmployerDto> employers = employerDao.findAll();
            for (EmployerDto dto:employers) {
                JFXButton btn = new JFXButton("Delete");
                btn.setBackground(Background.fill(Color.rgb(255, 121, 121)));
                btn.cursorProperty().set(Cursor.HAND);

                btn.setOnAction(actionEvent -> {
                    Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete "+dto.getId()+" ?", ButtonType.NO, ButtonType.YES).showAndWait();
                    if (buttonType.get()==ButtonType.YES){
                        try {
                            boolean isDeleted = employerDao.delete(dto.getId());
                            if (isDeleted) {
                                new Alert(Alert.AlertType.INFORMATION, "Deleted..!").show();
                                loadTable();
                                loadId();
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

                tmList.add(new EmployerTm(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getName(),
                        dto.getNic(),
                        dto.getDob(),
                        dto.getAddress(),
                        dto.getBankAcc(),
                        dto.getBankBranch(),
                        dto.getContact(),
                        btn
                ));
            }
            TreeItem<EmployerTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            tblEmployee.setRoot(treeItem);
            tblEmployee.setShowRoot(false);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
        }
    }

    private void loadId() {
        try {
            txtId.setText(employerDao.getId());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void printButtonOnAction(ActionEvent event) {
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Employers.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT * FROM employer");
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
