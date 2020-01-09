package test;

import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.controls.RXToggleButton;

public class TabPaneDemo extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        AnchorPane p1 = new AnchorPane();
        p1.setStyle("-fx-background-color: red");
        AnchorPane p2 = new AnchorPane();
        p2.setStyle("-fx-background-color: blue");
        Tab tb1 = new Tab("",p1);
        Tab tb2 = new Tab("",p2);
        TabPane tp = new TabPane(tb1,tb2);
        tp.setSide(Side.LEFT);
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        RXToggleButton rt1=new RXToggleButton("主页");
        rt1.setOnAction(e->{
            tp.getSelectionModel().select(0);
        });

        RXToggleButton rt2=new RXToggleButton("简介");
        rt2.setOnAction(e->{
            tp.getSelectionModel().select(1);
        });

        ToggleGroup group=new ToggleGroup();
        group.getToggles().addAll(rt1,rt2);
        VBox box=new VBox(rt1,rt2);
        rt1.setSelected(true);
        root.setLeft(box);
        root.setCenter(tp);
        root.getStylesheets().add(getClass().getResource("/css/tp.css").toExternalForm());
        primaryStage.setScene(new Scene(root, 500, 380));
        primaryStage.setTitle("测试");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
