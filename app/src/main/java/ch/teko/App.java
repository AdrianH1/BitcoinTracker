/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ch.teko;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        TextField addressField = new TextField("bc1q44fptqxn6mw0jgp0gh8lm42hs4mxdv8zrh96qq");
        addressField.setPrefWidth(500);
        addressHBox.getChildren().addAll(addressLabel, addressField);

        HBox depthHBox = new HBox();
        depthHBox.setPadding(new Insets(15, 0, 15, 0));
        depthHBox.setSpacing(10);
        Label depthLabel = new Label("Suchtiefe:");
        depthLabel.setPrefWidth(100);
        TextField depthField = new TextField("1");
        depthField.setPrefWidth(50);
        Button runButton = new Button("Los!");
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);       

        File file =new File("D:/Daten/Teko HF/Programmieren2_Repo/BitcoinTracker/app/src/main/java/ch/teko/ajax-loader.gif");
        String localUrl =null;
        try {
            localUrl = file.toURI().toURL().toString();

        }catch (Exception e) {
            e.printStackTrace();
        }
        Image image =new Image(localUrl,20,20,false,true);
        ImageView imageView =new ImageView(image);
        imageView.setVisible(false);
        
        depthHBox.getChildren().addAll(depthLabel, depthField, runButton, imageView, errorLabel);

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
                    scene.setCursor(Cursor.WAIT);
                    imageView.setVisible(true);

                    Runnable task = () -> {
                        Tracker tracker = new Tracker();
                        List<MarkedAddress> result = tracker.startTracking(addressField.getText(), Integer.parseInt(depthField.getText()));
                        for (MarkedAddress markedAddress : result) {
                            resultsArea.appendText("Suchtiefe: " + String.valueOf(markedAddress.getSearchDepth()) + "\n\t" + markedAddress.getMarkedAddress() + "\n");
                        }
                        scene.setCursor(Cursor.DEFAULT);
                        imageView.setVisible(false);
                    };

                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    try {
                        // Runnable Task dem Executorservice zuweisen
                        executor.submit(task);
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
                    // Executorservice beenden, damit die Threads gekillt werden. 
                    executor.shutdown();
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
