package lk.ijse.pos.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.stage.Modality;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.model.UserDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lk.ijse.pos.dao.custom.impl.UserDaoImpl;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    public JFXCheckBox showHideCheckBox;
    public JFXTextField txtUserName;
    public BorderPane loginPane;
    public JFXPasswordField pswrd;
    public JFXTextField txtPassword;

    UserDaoImpl userDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.USER);


    public void initialize(){
        txtPassword.setVisible(false);
        pswrd.setOnKeyTyped(actionEvent1 -> {
            txtPassword.setText(pswrd.getText());
        });
        txtPassword.setOnKeyTyped(keyEvent -> {
            pswrd.setText(txtPassword.getText());
        });
        showHideCheckBox.setOnAction(actionEvent -> {
            if (showHideCheckBox.isSelected()){
                txtPassword.setVisible(true);
                pswrd.setVisible(false);
            }else{
                txtPassword.setVisible(false);
                pswrd.setVisible(true);
            }
        });
    }

    public void btnLogInOnAction(ActionEvent actionEvent) {
        try {
            if (userDao.exists(new UserDto(txtUserName.getText(), pswrd.getText(),null,null))){
                try {
                    if (userDao.getUserType(txtUserName.getText()).equals("Admin")) {
                        DashBoardFormController.setIsAdmin(true);
                    }else{
                        DashBoardFormController.setIsAdmin(false);
                    }
                    Stage stage = (Stage) loginPane.getScene().getWindow();
                    stage.hide();
                    stage.setMaximized(false);
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/DashBoardForm.fxml"))));
                    stage.setMaximized(true);
                    stage.centerOnScreen();
                    stage.show();
                }catch (IOException ex){
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,ex.getMessage()).show();
                }
            }else{
                new Alert(Alert.AlertType.ERROR,"Invalid Username or Password..!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void signInButtonOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) loginPane.getScene().getWindow();
            stage.hide();
            stage.setMaximized(false);
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/SignInForm.fxml"))));
            stage.centerOnScreen();
            stage.show();
        }catch (IOException ex){
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,ex.getMessage()).show();
        }
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        btnLogInOnAction(actionEvent);
    }

    public void pswrdOnAction(ActionEvent actionEvent) {
        btnLogInOnAction(actionEvent);
    }

    public void forgetButtonOnAction(ActionEvent event) {
        if (!txtUserName.getText().isEmpty() && txtUserName.getText()!=null) {
            try {
                ForgetPasswordFormController.userName = txtUserName.getText();
                Stage mainStage = (Stage) loginPane.getScene().getWindow();
                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(mainStage.getScene().getWindow());
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../../../view/ForgetPasswordForm.fxml"))));
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Enter Username..!").show();
        }
    }
}
