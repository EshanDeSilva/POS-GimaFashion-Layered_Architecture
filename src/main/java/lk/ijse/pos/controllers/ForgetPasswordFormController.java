package lk.ijse.pos.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.impl.UserDaoImpl;
import lk.ijse.pos.email.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Random;

public class ForgetPasswordFormController {

    public JFXTextField txtOTP;
    public JFXTextField txtNewPswrd;
    public JFXPasswordField pswrdFieldNew;
    public JFXTextField txtConfirmPswrd;
    public JFXPasswordField pswrdFieldConfirm;
    public JFXButton btnOk;
    public JFXButton btnSave;
    public JFXCheckBox checkBoxShow;
    public static String userName;
    private int OTP = -1;

    UserDaoImpl userDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.USER);

    public void initialize(){
        txtNewPswrd.setVisible(false);
        txtConfirmPswrd.setVisible(false);
        btnOk.setDisable(true);
        btnSave.setDisable(true);
        pswrdFieldNew.setDisable(true);
        pswrdFieldConfirm.setDisable(true);
        txtNewPswrd.setDisable(true);
        txtConfirmPswrd.setDisable(true);

        pswrdFieldNew.setOnKeyTyped(keyEvent -> {
            txtNewPswrd.setText(pswrdFieldNew.getText());
        });
        pswrdFieldConfirm.setOnKeyTyped(keyEvent -> {
            txtConfirmPswrd.setText(pswrdFieldConfirm.getText());
        });
        txtNewPswrd.setOnKeyTyped(keyEvent -> {
            pswrdFieldNew.setText(txtNewPswrd.getText());
        });
        txtConfirmPswrd.setOnKeyTyped(keyEvent -> {
            pswrdFieldConfirm.setText(txtConfirmPswrd.getText());
        });
        checkBoxShow.setOnAction(event -> {
            if (checkBoxShow.isSelected()){
                pswrdFieldNew.setVisible(false);
                pswrdFieldConfirm.setVisible(false);
                txtNewPswrd.setVisible(true);
                txtConfirmPswrd.setVisible(true);
            }else{
                pswrdFieldNew.setVisible(true);
                pswrdFieldConfirm.setVisible(true);
                txtNewPswrd.setVisible(false);
                txtConfirmPswrd.setVisible(false);
            }
        });

        txtOTP.setOnAction(event -> {
            okButtonFunctions();
        });
    }

    private int sendEmail(){
        int otp = -1;
        try {
            String senderEmail = "hasindu363@gmail.com";
            String recipientEmail = userDao.getEmail(userName);
            Session session = Email.getInstance().getSession();

            try {
                // Create a message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipientEmail));
                message.setSubject("EonTech Global Group (pvt).Ltd");
                otp = new Random().nextInt(10000 - 1000) + 1000;
                //num = String.format("%14d", otp);
                message.setText(String.valueOf(otp));

                // Send the message
                Transport.send(message);

                System.out.println("Email sent successfully.");
                btnOk.setDisable(false);
            } catch (MessagingException e) {
                System.out.println("Failed to send email. Error: " + e.getMessage());
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return otp;
    }

    public void sendOtpButtonOnAction(ActionEvent event) {
        OTP = sendEmail();
    }

    private void okButtonFunctions(){
        if (txtOTP.getText().equals(String.valueOf(OTP))){
            btnSave.setDisable(false);
            pswrdFieldNew.setDisable(false);
            pswrdFieldConfirm.setDisable(false);
            txtNewPswrd.setDisable(false);
            txtConfirmPswrd.setDisable(false);
        }else{
            btnSave.setDisable(true);
            pswrdFieldNew.setDisable(true);
            pswrdFieldConfirm.setDisable(true);
            txtNewPswrd.setDisable(true);
            txtConfirmPswrd.setDisable(true);
        }
    }
    public void okButtonOnAction(ActionEvent event) {
        okButtonFunctions();
    }

    public void saveButtonOnAction(ActionEvent event) {
        try {
            if (pswrdFieldNew.getText().equals(pswrdFieldConfirm.getText())) {
                Boolean isSaved = userDao.updatePassword(userName, pswrdFieldConfirm.getText());
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Saved..!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went Wrong..!").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Password does not match..!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtOTP.clear();
        txtConfirmPswrd.clear();
        txtNewPswrd.clear();
        pswrdFieldNew.clear();
        pswrdFieldConfirm.clear();
        btnOk.setDisable(true);
        btnSave.setDisable(true);
        pswrdFieldNew.setDisable(true);
        pswrdFieldConfirm.setDisable(true);
        txtNewPswrd.setDisable(true);
        txtConfirmPswrd.setDisable(true);
    }
}
