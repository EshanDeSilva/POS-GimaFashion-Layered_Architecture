package lk.ijse.pos.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.pos.dao.custom.impl.OrderDetailsDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DashBoardFormController {

    public Label lblTime;
    public Label lblDate;
    public BorderPane dashboardPane;
    public PieChart pieChart;
    public JFXButton btnSalesReport;

    public static Boolean isAdminLogin = false;

    public void initialize(){
        manageDateAndTime();
        loadPieChart();
        if (!isAdminLogin){
            btnSalesReport.setDisable(true);
        }else{
            btnSalesReport.setDisable(false);
        }
    }

    public static void setIsAdmin(Boolean isAmin){
        isAdminLogin = isAmin;
    }

    private void loadPieChart() {
        ObservableList<PieChart.Data> pieChartData =
                null;
        try {
            pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Gents", OrderDetailsDaoImpl.getDailyGentsSaleCount()),
                    new PieChart.Data("Ladies", OrderDetailsDaoImpl.getDailyLadiesSaleCount()),
                    new PieChart.Data("Kids", OrderDetailsDaoImpl.getDailyKidsSaleCount()),
                    new PieChart.Data("Other", OrderDetailsDaoImpl.getDailyOtherSaleCount())
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        pieChart.setData(pieChartData);
    }

    private void manageDateAndTime() {
        Timeline timeline=new Timeline(new KeyFrame(Duration.ZERO,
                e-> lblDate.setText(LocalDateTime.now().
                        format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        Timeline time=new Timeline(new KeyFrame(Duration.ZERO,
                e-> lblTime.setText(LocalDateTime.now().
                        format(DateTimeFormatter.ofPattern("HH:mm:ss")))),
                new KeyFrame(Duration.seconds(1)));

        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void calcButtonOnAction(ActionEvent mouseEvent) {
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ordersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/OrdersForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Orders Form");
        stage.centerOnScreen();
        stage.show();
    }

    public void itemsButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/ItemsForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Items Form");
        stage.centerOnScreen();
        stage.show();
    }

    public void suppliersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/SuppliersForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Suppliers Form");
        stage.centerOnScreen();
        stage.show();
    }

    public void employersButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/EmployersForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Employee Form");
        stage.centerOnScreen();
        stage.show();
    }

    public void OrderDetailsButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/OrderDetailsForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("OrderDetails Form");
        stage.centerOnScreen();
        stage.show();
    }

    public void logOutButtonOnAction(ActionEvent actionEvent) {
        try {
            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to Log out?", ButtonType.YES, ButtonType.NO).showAndWait();
            if (buttonType.get()==ButtonType.YES) {
                Stage stage = (Stage) dashboardPane.getScene().getWindow();
                stage.hide();
                stage.setMaximized(false);
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/LoginForm.fxml"))));
                stage.centerOnScreen();
                stage.show();
            }
        }catch (IOException ex){
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    /*public void settingsButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/SettingsForm.fxml"))));
        stage.setTitle("Settings Form");
        stage.centerOnScreen();
        stage.show();
    }*/

    public void salesReportsButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/SalesReportsForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Sales-Reports Form");
        stage.centerOnScreen();
        stage.show();
    }

    public void salesReturnsButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/SalesReturnsForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Sales-Returns Form");
        stage.centerOnScreen();
        stage.show();
    }
}
