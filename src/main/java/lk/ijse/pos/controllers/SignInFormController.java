package lk.ijse.pos.controllers;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.email.Email;
import lk.ijse.pos.model.UserDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lk.ijse.pos.dao.custom.impl.UserDaoImpl;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class SignInFormController {

    public BorderPane signInPane;
    public JFXTextField txtAdminUserName;
    public JFXTextField txtPswrdAdmin;
    public JFXPasswordField pswrdAdmin;
    public JFXCheckBox adminPasswordCheckBox;
    public JFXTextField txtOtp;
    public ImageView adminConfirmImg;
    public ImageView otpConfirmImg;
    public JFXTextField txtNewUserName;
    public JFXTextField txtPswrdNewUser;
    public JFXPasswordField pswrdNewUser;
    public JFXTextField txtConfirmNewUserPassword;
    public JFXPasswordField pswrdConfirmNewUserPassword;
    public ImageView newUserPasswordConfirmImg;
    public JFXCheckBox newUserPasswordCheckBox;
    public JFXButton btnCreate;
    public JFXButton btnSend;
    public JFXComboBox cmbUserType;
    public JFXTextField txtNewUserEmail;

    UserDaoImpl userDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.USER);

    public void initialize(){
        txtPswrdAdmin.setVisible(false);
        txtPswrdNewUser.setVisible(false);
        txtConfirmNewUserPassword.setVisible(false);
        adminConfirmImg.setVisible(false);
        otpConfirmImg.setVisible(false);
        newUserPasswordConfirmImg.setVisible(false);
        txtConfirmNewUserPassword.setDisable(true);
        pswrdConfirmNewUserPassword.setDisable(true);
        btnCreate.setDisable(true);
        txtNewUserName.setDisable(true);
        txtPswrdNewUser.setDisable(true);
        pswrdNewUser.setDisable(true);
        btnSend.setDisable(true);
        txtNewUserEmail.setDisable(true);

        adminPasswordShowAndHide();
        newUserPasswordShowAndHide();

        ObservableList<String> userTypes = FXCollections.observableArrayList("Default","Admin");
        cmbUserType.setItems(userTypes);
    }

    private boolean checkAdmin() {
        if (txtAdminUserName.getText().equals("hasindueshandesilva@gmail.com")&&pswrdAdmin.getText().equals("admin")){
            return true;
        }
        return false;
    }

    private void newUserPasswordShowAndHide() {
        txtPswrdNewUser.setOnKeyTyped(keyEvent -> {
            if (!pswrdNewUser.getText().isEmpty()){
                txtConfirmNewUserPassword.setDisable(false);
                pswrdConfirmNewUserPassword.setDisable(false);
            }else{
                txtConfirmNewUserPassword.setDisable(true);
                pswrdConfirmNewUserPassword.setDisable(true);
            }
            pswrdNewUser.setText(txtPswrdNewUser.getText());
        });
        pswrdNewUser.setOnKeyTyped(keyEvent -> {
            if (!pswrdNewUser.getText().isEmpty()){
                txtConfirmNewUserPassword.setDisable(false);
                pswrdConfirmNewUserPassword.setDisable(false);
            }else{
                txtConfirmNewUserPassword.setDisable(true);
                pswrdConfirmNewUserPassword.setDisable(true);
            }
            txtPswrdNewUser.setText(pswrdNewUser.getText());
        });
        txtConfirmNewUserPassword.setOnKeyTyped(keyEvent -> {
            pswrdConfirmNewUserPassword.setText(txtConfirmNewUserPassword.getText());

            if (txtConfirmNewUserPassword.getText().equals(txtPswrdNewUser.getText())){
                newUserPasswordConfirmImg.setVisible(true);
                if (otpConfirmImg.isVisible()){
                    btnCreate.setDisable(false);
                }
            }else{
                newUserPasswordConfirmImg.setVisible(false);
                btnCreate.setDisable(true);
            }
        });
        pswrdConfirmNewUserPassword.setOnKeyTyped(keyEvent -> {
            txtConfirmNewUserPassword.setText(pswrdConfirmNewUserPassword.getText());
            if (txtConfirmNewUserPassword.getText().equals(txtPswrdNewUser.getText())){
                newUserPasswordConfirmImg.setVisible(true);
                if (otpConfirmImg.isVisible()){
                    btnCreate.setDisable(false);
                }
            }else{
                newUserPasswordConfirmImg.setVisible(false);
                btnCreate.setDisable(true);
            }
        });
        newUserPasswordCheckBox.setOnAction(actionEvent -> {
            if (newUserPasswordCheckBox.isSelected()){
                txtPswrdNewUser.setVisible(true);
                txtConfirmNewUserPassword.setVisible(true);
                pswrdNewUser.setVisible(false);
                pswrdConfirmNewUserPassword.setVisible(false);
            }else{
                txtPswrdNewUser.setVisible(false);
                txtConfirmNewUserPassword.setVisible(false);
                pswrdNewUser.setVisible(true);
                pswrdConfirmNewUserPassword.setVisible(true);
            }
        });
    }

    private void adminPasswordShowAndHide() {
        txtPswrdAdmin.setOnKeyTyped(keyEvent -> {
            pswrdAdmin.setText(txtPswrdAdmin.getText());
        });
        pswrdAdmin.setOnKeyTyped(keyEvent -> {
            txtPswrdAdmin.setText(pswrdAdmin.getText());
        });
        adminPasswordCheckBox.setOnAction(actionEvent -> {
            if (adminPasswordCheckBox.isSelected()){
                txtPswrdAdmin.setVisible(true);
                pswrdAdmin.setVisible(false);
            }else{
                txtPswrdAdmin.setVisible(false);
                pswrdAdmin.setVisible(true);
            }
        });
    }

    public void sendButtonOnAction(ActionEvent actionEvent) {
        int otp = sendOtpByEmail();
        txtOtp.setOnKeyTyped(keyEvent -> {
            if (txtOtp.getText().equals(String.valueOf(otp))){
                otpConfirmImg.setVisible(true);
                txtNewUserName.setDisable(false);
                txtPswrdNewUser.setDisable(false);
                pswrdNewUser.setDisable(false);
                txtNewUserEmail.setDisable(false);
            }else{
                otpConfirmImg.setVisible(false);
                txtNewUserName.setDisable(true);
                txtPswrdNewUser.setDisable(true);
                pswrdNewUser.setDisable(true);
                txtNewUserEmail.setDisable(true);
            }
        });

    }

    private int sendOtpByEmail() {
        int otp=-1;

        String senderEmail = "hasindu363@gmail.com";
        String recipientEmail = "hasindueshandesilva@gmail.com";
        Session session = Email.getInstance().getSession();

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail));
            message.setSubject("EonTech Global Group (pvt).Ltd");
            otp = new Random().nextInt(10000-1000)+1000;
            //num = String.format("%14d", otp);
            message.setText(String.valueOf(otp));

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
        }
        return otp;
    }

    public void checkButtonOnAction(ActionEvent actionEvent) {
        if (txtAdminUserName.getText().matches("[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+(?:[c]*[o]*[m])?")){
            if (checkAdmin()) {
                adminConfirmImg.setVisible(true);
                btnSend.setDisable(false);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please Enter Valid Admin Email and Password..!").show();
                adminConfirmImg.setVisible(false);
                btnSend.setDisable(true);
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Please Enter Valid Email..!").show();
            adminConfirmImg.setVisible(false);
            btnSend.setDisable(true);
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) signInPane.getScene().getWindow();
            stage.hide();
            stage.setMaximized(false);
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/LoginForm.fxml"))));
            stage.centerOnScreen();
            stage.show();
        }catch (IOException ex){
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,ex.getMessage()).show();
        }
    }

    public void createButtonOnAction(ActionEvent actionEvent) {
        if (txtNewUserEmail.getText().matches("[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+(?:[c]*[o]*[m])?")) {
            if (cmbUserType.getValue()!=null&&(cmbUserType.getValue().equals("Default")||cmbUserType.getValue().equals("Admin"))) {
                try {
                    boolean isSaved = userDao.save(new UserDto(txtNewUserName.getText(), pswrdConfirmNewUserPassword.getText(), txtNewUserEmail.getText(), cmbUserType.getValue().toString()));
                    if (isSaved) {
                        new Alert(Alert.AlertType.INFORMATION, "New User Created Successfully..!").show();
                        clearFields();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Select User Type..!").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Please Enter Valid Email..!").show();
        }
    }

    private void clearFields() {
        txtOtp.clear();
        txtAdminUserName.clear();
        txtPswrdAdmin.clear();
        pswrdAdmin.clear();
        txtNewUserName.clear();
        txtPswrdNewUser.clear();
        pswrdNewUser.clear();
        txtConfirmNewUserPassword.clear();
        pswrdConfirmNewUserPassword.clear();
        txtNewUserEmail.clear();
        cmbUserType.setValue("");
    }
}
