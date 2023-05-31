package lk.ijse.pos.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.dao.custom.impl.OrderDetailsDaoImpl;
import lk.ijse.pos.dao.custom.impl.OrderDaoImpl;
import lk.ijse.pos.dao.custom.impl.SalesReturnDaoImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;

public class SalesReportsFormController {
    public JFXComboBox cmbDuration;
    public Label lblIncome;
    public Label lblSalesCount;
    public LineChart chart;
    public BorderPane salesReportPane;
    public Label lblSales;

    SalesReturnDaoImpl salesReturnDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SALES_RETURN);

    public void initialize(){
        ObservableList<String> list = FXCollections.observableArrayList("Today","This Month","This Year");
        cmbDuration.setItems(list);

        cmbDuration.setValue("Today");
        loadToday();
        cmbDuration.setOnAction(event -> {
            switch (cmbDuration.getValue().toString()){
                case "This Month" : loadThisMonth();break;
                case "This Year" : loadThisYear();break;
                case "Today" : loadToday();
            }
        });
        chart.setTitle("Sales Chart");
    }

    private void loadThisYear() {
        chart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName("Sales");
        //chart.setTitle("Annual Chart "+ Year.now());
        try {
            series.getData().add(new XYChart.Data("Jan", OrderDetailsDaoImpl.getMonthlySalesCount("january")));
            series.getData().add(new XYChart.Data("Feb", OrderDetailsDaoImpl.getMonthlySalesCount("february")));
            series.getData().add(new XYChart.Data("Mar", OrderDetailsDaoImpl.getMonthlySalesCount("march")));
            series.getData().add(new XYChart.Data("Apr", OrderDetailsDaoImpl.getMonthlySalesCount("april")));
            series.getData().add(new XYChart.Data("May", OrderDetailsDaoImpl.getMonthlySalesCount("may")));
            series.getData().add(new XYChart.Data("Jun", OrderDetailsDaoImpl.getMonthlySalesCount("june")));
            series.getData().add(new XYChart.Data("Jul", OrderDetailsDaoImpl.getMonthlySalesCount("july")));
            series.getData().add(new XYChart.Data("Aug", OrderDetailsDaoImpl.getMonthlySalesCount("august")));
            series.getData().add(new XYChart.Data("Sep", OrderDetailsDaoImpl.getMonthlySalesCount("september")));
            series.getData().add(new XYChart.Data("Oct", OrderDetailsDaoImpl.getMonthlySalesCount("october")));
            series.getData().add(new XYChart.Data("Nov", OrderDetailsDaoImpl.getMonthlySalesCount("november")));
            series.getData().add(new XYChart.Data("Dec", OrderDetailsDaoImpl.getMonthlySalesCount("december")));

            lblIncome.setText(String.format("%.2f", OrderDetailsDaoImpl.getAnnualIncome()));
            lblSalesCount.setText(String.valueOf(OrderDetailsDaoImpl.getAnnualSalesCount()));
            lblSales.setText(String.format("%.2f", OrderDaoImpl.getAnnualSalesTotal()));
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        chart.getData().add(series);
    }

    private void loadThisMonth() {
        chart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName("Sales");
        //chart.setTitle("Monthly Chart "+ Month.of(YearMonth.now().getMonthValue()));
        try {
            for (int i = 1; i <= YearMonth.now().lengthOfMonth(); i++) {
                series.getData().add(new XYChart.Data(i+"", OrderDetailsDaoImpl.getDailySalesCount(i)));
            }
            lblIncome.setText(String.format("%.2f", OrderDetailsDaoImpl.getMonthlyIncome()));
            lblSalesCount.setText(String.valueOf(OrderDetailsDaoImpl.getMonthlySalesCount()));
            lblSales.setText(String.format("%.2f", OrderDaoImpl.getMonthlySalesTotal()));
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        chart.getData().add(series);
    }

    private void loadToday() {
        loadThisMonth();
        try {
            lblIncome.setText(String.format("%.2f", OrderDetailsDaoImpl.getDailyIncome()));
            lblSalesCount.setText(String.valueOf(OrderDetailsDaoImpl.getDailySalesCount()));
            lblSales.setText(String.format("%.2f", OrderDaoImpl.getDailySalesTotal()));
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void backButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) salesReportPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/DashBoardForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Gima Fashion");
        stage.centerOnScreen();
        stage.show();
    }

    public void dailyButtonOnAction(ActionEvent event) {
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Report.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT orderId,date,total FROM orders WHERE DAY(date)=DAY(CURDATE())");
            jDesign.setQuery(query);

            JasperReport compileReport = JasperCompileManager.compileReport(jDesign);
            java.util.Map params = new java.util.HashMap();
            params.put( "Total", String.format("%.2f", OrderDaoImpl.getDailySalesTotal()) );
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,params , DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void annualButtonOnAction(ActionEvent event) {
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Report.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT orderId,date,total FROM orders WHERE YEAR(date)=YEAR(CURDATE())");
            jDesign.setQuery(query);

            JasperReport compileReport = JasperCompileManager.compileReport(jDesign);
            java.util.Map params = new java.util.HashMap();
            params.put( "Total", String.format("%.2f", OrderDaoImpl.getMonthlySalesTotal()) );
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,params , DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void monthlyButtonOnAction(ActionEvent event) {
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Report.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT orderId,date,total FROM orders WHERE MONTH(date)=MONTH(CURDATE())");
            jDesign.setQuery(query);

            JasperReport compileReport = JasperCompileManager.compileReport(jDesign);
            java.util.Map params = new java.util.HashMap();
            params.put( "Total", String.format("%.2f", OrderDaoImpl.getAnnualSalesTotal()) );
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,params , DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dailyReturnsButtonOnAction(ActionEvent event) {
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/DailyReturns.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT * FROM salesReturn INNER JOIN salesReturnDetails ON salesReturn.returnId = salesReturnDetails.returnId INNER JOIN item ON salesReturnDetails.itemCode=item.itemCode WHERE salesReturn.date = '"+LocalDate.now()+"'");
            jDesign.setQuery(query);

            JasperReport compileReport = JasperCompileManager.compileReport(jDesign);
            java.util.Map params = new java.util.HashMap();
            params.put( "Total", String.format("%.2f", salesReturnDao.getDailyReturnTotal()) );
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,params , DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
