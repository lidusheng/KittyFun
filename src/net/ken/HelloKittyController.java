/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ken;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HelloKittyController implements Initializable{

	@FXML
    private Menu bt_mentFile;

    @FXML
    private HBox bt_hboxTop;

    @FXML
    private TabPane bt_tabPane;

    @FXML
    private Menu bt_menuEdit;

    @FXML
    private MenuItem bt_MenuItemAbout;

    @FXML
    private TextField searchfield;

    @FXML
    private Label bt_labelTT;

    @FXML
    private BorderPane mainwindow;

    @FXML
    private MenuItem bt_menuItemDelete;

    @FXML
    private Tab btkittydsc;

    @FXML
    private MenuBar bt_menuBar;

    @FXML
    private MenuItem bt_menuItemClose;

    @FXML
    private VBox bt_vboxMain;

    @FXML
    private ImageView bt_imageViewLogo;

    @FXML
    private Menu bt_menuHelp;




    @FXML
    private void ExitAction(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    private void ExitActionV(Event event) {
    	System.out.println(event);
    }

    @FXML
    private void CleanAllTabs(ActionEvent event) {
    	bt_tabPane.getTabs().clear();
    }

    @FXML
    private void CleanAllTabsV(Event event) {
    	System.out.println(event);
    }

    @FXML
    private void AboutAction(ActionEvent event) {
    	bt_tabPane.getTabs().add(btkittydsc);
    }

    @FXML
    private void AboutActionV(Event event) {
    	System.out.println(event);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
    
}
