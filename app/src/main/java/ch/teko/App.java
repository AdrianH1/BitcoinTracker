/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ch.teko;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bitcoin Tracker");
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15, 12, 15, 12));
        HBox addressHBox = new HBox();
        addressHBox.setPadding(new Insets(15, 0, 15, 0));
        addressHBox.setSpacing(10);
        Label addressLabel = new Label("Wallet Adresse:");
        addressLabel.setPrefWidth(100);
        TextField addressField = new TextField();
        addressField.setPrefWidth(500);
        addressHBox.getChildren().addAll(addressLabel, addressField);

        HBox depthHBox = new HBox();
        depthHBox.setPadding(new Insets(15, 0, 15, 0));
        depthHBox.setSpacing(10);
        Label depthLabel = new Label("Suchtiefe:");
        depthLabel.setPrefWidth(100);
        TextField depthField = new TextField();
        depthField.setPrefWidth(50);
        Button runButton = new Button("Los!");
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        depthHBox.getChildren().addAll(depthLabel, depthField, runButton, errorLabel);
        

        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setPrefHeight(1000);
        vBox.getChildren().addAll(addressHBox, depthHBox, resultsArea);

        Scene scene = new Scene(vBox, 900, 500);

        primaryStage.setScene(scene);
        primaryStage.show();

         //Button Click Handler
         runButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if (checkDepthValidity(depthField.getText())) {
                    errorLabel.setText("");
                    Tracker tracker = new Tracker();
                    tracker.startTracking(addressField.getText(), Integer.parseInt(depthField.getText()));
                }
                else {
                    errorLabel.setText("Ung\u00fcltige Suchtiefe!");
                }
                
            }
        });
    }
    public boolean checkDepthValidity(String depth){
        try {
            int depthValue = Integer.parseInt(depth);
            if (depthValue > 0 && depthValue <= 100) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
