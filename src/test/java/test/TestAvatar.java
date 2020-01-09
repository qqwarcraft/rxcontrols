package test;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rx.controls.RXAvatar;
import rx.enums.ShapeType;

public class TestAvatar extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        HBox hbox = new HBox(30);
        hbox.setAlignment(Pos.CENTER);
        RXAvatar avatar1 = getAvatar("/img/2.jpg", 100, 100,ShapeType.CIRCLE);
        RXAvatar avatar2 = getAvatar("/img/1.jpg", 100, 100,ShapeType.SQUARE);
        RXAvatar avatar3 = getAvatar("/img/3.jpg", 100, 100,ShapeType.HEXAGON_H);
        RXAvatar avatar4 = getAvatar("/img/6.jpg", 100, 100,ShapeType.HEXAGON_V);
        RXAvatar avatar5 = getAvatar("/img/9.jpg", 100, 100,ShapeType.SQUARE);
        avatar5.setArcHeight(20);
        avatar5.setArcWidth(20);
        
        hbox.getChildren().addAll(avatar1,avatar2,avatar3,avatar4,avatar5);
        stage.setScene(new Scene(hbox, 680, 180));
        stage.setTitle("头像");
        stage.show();
        
    }

    private RXAvatar getAvatar(String path,double w,double h,ShapeType type){
        RXAvatar avatar = new RXAvatar();
        avatar.setPrefSize(w,h);
        avatar.setImage(new Image(getClass().getResource(path).toExternalForm()));
        avatar.setShapeType(type);
        avatar.setEffect(new DropShadow());
        return  avatar;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
