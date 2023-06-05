package lk.ijse.pos.controllers;

import com.jfoenix.controls.*;
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
import javafx.util.StringConverter;
import lk.ijse.pos.bo.BoFactory;
import lk.ijse.pos.bo.custom.ItemBo;
import lk.ijse.pos.bo.custom.OrderBo;
import lk.ijse.pos.bo.custom.PaymentBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.email.Email;
import lk.ijse.pos.model.*;
import lk.ijse.pos.model.tm.OrderTm;
import lk.ijse.pos.dao.custom.impl.EmployerDaoImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrdersFormController {
    public BorderPane orderPane;
    public JFXTreeTableView<OrderTm> tblOrders;
    public TreeTableColumn colItemCode;
    public TreeTableColumn colDescription;
    public TreeTableColumn colQty;
    public TreeTableColumn colUnitPrice;
    public TreeTableColumn colDate;
    public TreeTableColumn colDiscount;
    public TreeTableColumn colSize;
    public TreeTableColumn colType;
    public TreeTableColumn colOption;
    public JFXComboBox cmbEmployerId;
    public JFXTextField txtEmployerName;
    public JFXTextField txtCustomerName;
    public JFXTextField txtContact;
    public JFXTextField txtCustomerEmail;
    public DatePicker datePicker;
    public JFXCheckBox checkBoxCash;
    public JFXCheckBox checkBoxCard;
    public JFXTextField txtItemCode;
    public JFXComboBox cmbItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtOrderQuantity;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtSellingPrice;
    public JFXTextField txtProfit;
    public JFXTextField txtDiscount;
    public JFXButton btnUpdate;
    public JFXTextField txtCash;
    public JFXButton btnPlaceOrder;
    public JFXTextField txtOrderId;
    public JFXTextField txtType;
    public JFXTextField txtSize;
    public TreeTableColumn colAmount;
    public Label lblTotal;
    public Label lblDiscount;
    public Label lblBalance;

//    PaymentDaoImpl paymentDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.PAYMENT);
//    OrderDaoImpl orderDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ORDER);
//    ItemDaoImpl itemDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ITEM);
    EmployerDaoImpl employerDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.EMPLOYER);

    PaymentBo paymentBo = BoFactory.getInstance().getBoType(BoFactory.BoType.PAYMENT_BO);
    OrderBo orderBo = BoFactory.getInstance().getBoType(BoFactory.BoType.ORDER_BO);
    ItemBo itemBo = BoFactory.getInstance().getBoType(BoFactory.BoType.ITEM_BO);

    ObservableList<OrderTm> cartList = FXCollections.observableArrayList();

    public void initialize(){

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
        loadOrderId();
        loadEmployers();
        loadItem();
        dateFormat(datePicker);
        datePicker.setValue(LocalDate.parse(loadDate()));

        txtCash.setOnKeyReleased(keyEvent ->{
            setBalance();
        });

        tblOrders.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
                btnUpdate.setDisable(false);
            }
        } );
    }

    private void setData(TreeItem<OrderTm> newValue) {
        cmbItemCode.setValue(newValue.getValue().getItemCode());
        txtDescription.setText(newValue.getValue().getDescription());
        txtOrderQuantity.setText(String.valueOf(newValue.getValue().getQty()));
        try {
            txtQtyOnHand.setText(String.valueOf(itemBo.findItem(txtItemCode.getText()).getQty()));
            txtSellingPrice.setText(String.valueOf(newValue.getValue().getUnitPrice()));
            txtProfit.setText(String.valueOf(newValue.getValue().getUnitPrice()-Double.parseDouble(itemBo.findItem(txtItemCode.getText()).getBuyingPrice())));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        txtType.setText(newValue.getValue().getType());
        txtSize.setText(newValue.getValue().getSize());
        txtDiscount.setText(String.valueOf(newValue.getValue().getDiscount()));
    }

    private void setBalance() {
        lblBalance.setText(String.format("%.2f",Double.parseDouble(txtCash.getText())-Double.parseDouble(lblTotal.getText())));
    }

    private String loadDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private void dateFormat(DatePicker datePicker) {
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

    private void loadItem() {
        try {
            List<ItemDto> items = itemBo.findAllItems();
            ObservableList<String> codeList = FXCollections.observableArrayList();
            for (ItemDto dto:items) {
                codeList.add(dto.getCode());
            }
            cmbItemCode.setItems(codeList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbItemCode.setOnAction(event -> {
            setItem();
        });
        txtItemCode.setOnAction(event -> {
            cmbItemCode.setValue(txtItemCode.getText());
        });
    }

    private void setItem() {
        if (cmbItemCode.getValue()!=null) {
            try {
                txtItemCode.setText(cmbItemCode.getValue().toString());
                txtDescription.setText(itemBo.findItem(txtItemCode.getText()).getDescription());
                txtQtyOnHand.setText(itemBo.findItem(txtItemCode.getText()).getQty());
                txtSellingPrice.setText(String.format("%.2f",Double.parseDouble(itemBo.findItem(txtItemCode.getText()).getSellingPrice())));
                txtProfit.setText(String.format("%.2f",Double.parseDouble(itemBo.findItem(txtItemCode.getText()).getSellingPrice())-Double.parseDouble(itemBo.findItem(txtItemCode.getText()).getBuyingPrice())));
                txtType.setText(itemBo.findItem(txtItemCode.getText()).getCategoryDto().getType());
                txtSize.setText(itemBo.findItem(txtItemCode.getText()).getCategoryDto().getSize());
            } catch (SQLException | ClassNotFoundException | NullPointerException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Something went Wrong..! Please Select Valid Code.").show();
            }
        }
    }


    private void loadEmployers() {
        try {
            List<EmployerDto> employers = employerDao.findAll();
            ObservableList<String> idList = FXCollections.observableArrayList();
            for (EmployerDto dto:employers) {
                idList.add(dto.getId());
            }
            cmbEmployerId.setItems(idList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbEmployerId.setOnAction(actionEvent -> {
            if (cmbEmployerId.getValue()!=null) {
                try {
                    txtEmployerName.setText(employerDao.find(cmbEmployerId.getValue().toString()).getName());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            List<EmployerDto> employers = employerDao.findAll();
            ObservableList<String> nameList = FXCollections.observableArrayList();
            for (EmployerDto dto : employers) {
                nameList.add(dto.getName());
            }
            txtEmployerName.setOnAction(actionEvent -> {
                if (!txtEmployerName.getText().isEmpty()) {
                    boolean notSet=true;
                    for (String name:nameList) {
                        if (name.toLowerCase().startsWith(txtEmployerName.getText().toLowerCase())){
                            txtEmployerName.setText(name);
                            notSet=false;
                            try {
                                cmbEmployerId.setValue(employerDao.findByName(txtEmployerName.getText()).getId());
                            } catch (SQLException | ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    if (notSet){
                        new Alert(Alert.AlertType.ERROR,"Something went Wrong..! Please Input Valid Name.").show();
                        cmbEmployerId.setValue(null);
                    }
                }else{
                    cmbEmployerId.setValue(null);
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadOrderId() {
        try {
            txtOrderId.setText(orderBo.getId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) orderPane.getScene().getWindow();
        stage.hide();
        stage.setMaximized(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/DashBoardForm.fxml"))));
        stage.setMaximized(true);
        stage.setTitle("Gima Fashion");
        stage.centerOnScreen();
        stage.show();
    }

    public void updateButtonOnAction(ActionEvent event) {
        for (OrderTm tm:cartList) {
            if (tm.getItemCode().equals(txtItemCode.getText())){
                tm.setQty(Integer.parseInt(txtOrderQuantity.getText()));
                tm.setDiscount(Double.parseDouble(txtDiscount.getText()));
                tm.setAmount(tm.getQty()*(tm.getUnitPrice()-((tm.getUnitPrice()/100)*tm.getDiscount())));
            }
        }
        refreshTable();
        clearItemFields();
    }

    public void clearButtonOnAction(ActionEvent event) {
        clearItemFields();
    }

    private void clearFields() {
        clearItemFields();
        loadOrderId();
        cmbEmployerId.setValue(null);
        txtEmployerName.clear();
        txtCustomerName.clear();
        txtCustomerEmail.clear();
        txtContact.clear();
        datePicker.setValue(LocalDate.parse(loadDate()));
        checkBoxCard.setSelected(false);
        checkBoxCash.setSelected(false);
    }

    public void placeOrderButtonOnAction(ActionEvent event) {
        if (txtCustomerEmail.getText().matches("[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+(?:[c]*[o]*[m])?")||txtCustomerEmail.getText().isEmpty()||txtCustomerEmail.getText()==null) {
            if (!cartList.isEmpty()) {
                if (!txtCash.getText().isEmpty()) {
                    try {
                        List<OrderDetailsDto> dtoList = new ArrayList<>();
                        for (OrderTm tm : cartList) {
                            dtoList.add(new OrderDetailsDto(
                                    txtOrderId.getText(),
                                    tm.getItemCode(),
                                    tm.getQty(),
                                    tm.getUnitPrice(),
                                    (tm.getAmount()) - (Double.parseDouble(itemBo.findItem(tm.getItemCode()).getBuyingPrice()) * tm.getQty()),
                                    tm.getDiscount()
                            ));
                        }
                        List<PaymentDto> payments = new ArrayList<>();
                        payments.add(new PaymentDto(
                                paymentBo.getId(),
                                Double.parseDouble(txtCash.getText()),
                                checkBoxCash.isSelected(),
                                Double.parseDouble(lblBalance.getText()),
                                datePicker.getValue().toString(),
                                txtOrderId.getText()
                        ));
                        boolean isSaved = orderBo.saveOrder(new OrderDto(
                                txtOrderId.getText(),
                                datePicker.getValue().toString(),
                                Double.parseDouble(lblDiscount.getText()),
                                Double.parseDouble(lblTotal.getText()),
                                cmbEmployerId.getValue().toString(),
                                txtCustomerName.getText(),
                                txtCustomerEmail.getText(),
                                txtContact.getText(),
                                dtoList,
                                payments
                        ));
                        if (isSaved) {
                            new Alert(Alert.AlertType.INFORMATION, "Order Placed..!").show();
                            updateStock();
                            print();
                            sendMessage();
                            cartList.clear();
                            tblOrders.refresh();
                            lblTotal.setText("0.00");
                            lblBalance.setText("0.00");
                            lblDiscount.setText("0.00");
                            txtCash.clear();
                            clearFields();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Please Enter Cash Amount..!").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Minimum 1 Item Required..!").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Please Enter Valid Email..!").show();
        }
    }

    private void updateStock() {
        for (OrderTm tm:cartList) {
            try {
                itemBo.updateQtyOfItem(new ItemDto(
                        tm.getItemCode(),
                        itemBo.findItem(tm.getItemCode()).getSupplierId(),
                        tm.getDescription(),
                        String.valueOf(Integer.parseInt(itemBo.findItem(tm.getItemCode()).getQty())-tm.getQty()),
                        String.valueOf(tm.getUnitPrice()),
                        itemBo.findItem(tm.getItemCode()).getBuyingPrice(),
                        itemBo.findItem(tm.getItemCode()).getCategoryDto()
                ));
            }catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addToCartButtonOnAction(ActionEvent event) {
        if (!txtOrderId.getText().isEmpty()&&!txtEmployerName.getText().isEmpty()&&cmbEmployerId.getValue()!=null&&
                !txtItemCode.getText().isEmpty()&&cmbItemCode.getValue()!=null&&!txtDescription.getText().isEmpty()&&
                !txtOrderQuantity.getText().isEmpty()&&(checkBoxCard.isSelected()||
                checkBoxCash.isSelected())) {
            if (Integer.parseInt(txtQtyOnHand.getText()) >= Integer.parseInt(txtOrderQuantity.getText())) {
                boolean isExist = false;
                for (OrderTm tm: cartList) {
                    if (txtItemCode.getText().equals(tm.getItemCode())){
                        if ((tm.getQty()+Integer.valueOf(txtOrderQuantity.getText()))<=Double.parseDouble(txtQtyOnHand.getText())) {
                            tm.setQty(tm.getQty() + Integer.valueOf(txtOrderQuantity.getText()));
                            double amount = (Double.parseDouble(txtSellingPrice.getText())*Double.parseDouble(txtOrderQuantity.getText()))-(((Double.parseDouble(txtSellingPrice.getText())*Double.parseDouble(txtOrderQuantity.getText()))/100)*tm.getDiscount());
                            txtDiscount.setText(String.valueOf(tm.getDiscount()));
                            tm.setAmount(tm.getAmount()+amount);
                            clearItemFields();
                            refreshTable();
                        }else{
                            new Alert(Alert.AlertType.ERROR,"Out of Stock..!").show();
                        }
                        isExist = true;
                    }
                }
                if (!isExist){
                    JFXButton btn = new JFXButton("Delete");
                    btn.setBackground(Background.fill(Color.rgb(255, 121, 121)));
                    btn.cursorProperty().set(Cursor.HAND);
                    if (txtDiscount.getText().isEmpty()){
                        txtDiscount.setText("0.00");
                    }
                    double amount = (Double.parseDouble(txtSellingPrice.getText())*Double.parseDouble(txtOrderQuantity.getText()))-(((Double.parseDouble(txtSellingPrice.getText())*Double.parseDouble(txtOrderQuantity.getText()))/100)*Double.parseDouble(txtDiscount.getText()));
                    cartList.add(new OrderTm(txtItemCode.getText(),
                                        txtDescription.getText(),
                                        Integer.parseInt(txtOrderQuantity.getText()),
                                        Double.parseDouble(txtSellingPrice.getText()),
                                        String.valueOf(datePicker.getValue()),
                                        txtDiscount.getText().isEmpty()?0.00:Double.parseDouble(txtDiscount.getText()),
                                        txtType.getText(),
                                        txtSize.getText(),
                                        amount,
                                        btn));
                    clearItemFields();
                }
                for (OrderTm tm: cartList) {
                    JFXButton btn = tm.getBtn();

                    btn.setOnAction(actionEvent -> {
                        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this item ?", ButtonType.NO, ButtonType.YES).showAndWait();
                        if (buttonType.get()==ButtonType.YES){
                            cartList.remove(tm);
                            refreshTable();
                        }
                    });
                }
                TreeItem<OrderTm> treeItem = new RecursiveTreeItem<>(cartList, RecursiveTreeObject::getChildren);
                tblOrders.setRoot(treeItem);
                tblOrders.setShowRoot(false);
                refreshTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Quantity out of stock..!").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
        }
    }

    private void refreshTable() {
        tblOrders.refresh();
        double total = 0;
        double discount = 0;
        for (OrderTm tm: cartList) {
            total+=tm.getAmount();
            discount+=((tm.getQty()*tm.getUnitPrice())-tm.getAmount());
        }
        lblTotal.setText(String.format("%.2f",total));
        lblDiscount.setText(String.format("%.2f",discount));
    }

    private void clearItemFields() {
        txtItemCode.clear();
        cmbItemCode.setValue(null);
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtOrderQuantity.clear();
        txtSellingPrice.clear();
        txtProfit.clear();
        txtType.clear();
        txtSize.clear();
        txtDiscount.clear();
        btnUpdate.setDisable(true);
    }

    public void removeOrderButtonOnAction(ActionEvent event) {
        //tblOrders.setRoot(null);
        cartList.clear();
        tblOrders.refresh();
        lblTotal.setText("0.00");
        lblBalance.setText("0.00");
        lblDiscount.setText("0.00");
        txtCash.clear();
        clearFields();
    }

    public void calcButtonOnAction(ActionEvent event) {
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void print(){
        try {
            JasperDesign jDesign = JRXmlLoader.load("src/main/resources/jasper/MyReports/Bill.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("SELECT orders.orderId,item.description,orderDetails.itemCode,orders.date,orderDetails.orderQty,orderDetails.unitPrice,orderDetails.discountRate,TRUNCATE((orderDetails.unitPrice-orderDetails.unitPrice/100*orderDetails.discountRate)*orderDetails.orderQty,2) AS amount,orders.total,orders.customerName,employer.name,orders.totalDiscount FROM `orderDetails` INNER JOIN orders ON orders.orderId=orderDetails.orderId INNER JOIN item ON item.itemCode=orderDetails.itemCode INNER JOIN employer ON employer.id=orders.employerId WHERE orderDetails.orderId='"+txtOrderId.getText()+"' GROUP BY orderDetails.itemCode");
            jDesign.setQuery(query);


            JasperReport compileReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DBConnection.getInstance().getConnection());
            JasperExportManager.exportReportToPdfFile(jasperPrint,"E:/IJSE/GDSE66/1st Sem/1st_Semester Final Project/POS-Gima_Fashion/Bills/"+txtOrderId.getText()+".pdf");
            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to print bill?", ButtonType.NO, ButtonType.YES).showAndWait();
            if (buttonType.get()==ButtonType.YES) {
                JasperViewer.viewReport(jasperPrint, false);
            }


        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(){
        String senderEmail = "hasindu363@gmail.com";
        String recipientEmail = txtCustomerEmail.getText();
        Session session = Email.getInstance().getSession();

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail));
            message.setSubject("Gima Fashion");
            //num = String.format("%14d", otp);
            message.setText("Bill");

            //3) create MimeBodyPart object and set your message text

            //4) create new MimeBodyPart object and set DataHandler object to this object
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            String filename = "E:/IJSE/GDSE66/1st Sem/1st_Semester Final Project/POS-Gima_Fashion/Bills/"+txtOrderId.getText()+".pdf";//change accordingly
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);


            //5) create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            //6) set the multiplart object to the message object
            message.setContent(multipart );

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
        }
    }
}
