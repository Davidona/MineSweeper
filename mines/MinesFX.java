package mines;


import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.GridPane;

import javafx.stage.Stage;

import java.io.IOException;


public class MinesFX extends Application {

    private Controller controller;
    private Stage myStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        myStage = primaryStage;
        myStage.setTitle("The Amazing Mines Sweeper");
        myStage.setScene(new Scene(createRoot()));
        controller.setMyStage(myStage);
        myStage.show();
    }

    private Parent createRoot() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        GridPane grid = new GridPane();
        loader.setLocation(getClass().getResource("MineFx.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.getHBox().getChildren().add(grid);
        myStage.resizableProperty();
        return root;

    }


    public static void main(String[] args) {
        launch(args);
    }


}







