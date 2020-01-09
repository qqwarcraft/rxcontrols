package test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import rx.controls.RXCarousel;
import rx.controls.RXToggleButton;
import rx.enums.ScaleType;
import rx.enums.TransitionType;

public class TestCarouselPane extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/carouselmain.fxml"));
        RXCarousel carousel = (RXCarousel) root.lookup("#pane");
        AnchorPane home = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        home.setStyle("-fx-background-color: #ff9ccd");
        AnchorPane b = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        b.setStyle("-fx-background-color: #b7fcff");
        AnchorPane c = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        c.setStyle("-fx-background-color: #6ab7ff");
        AnchorPane a = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        a.setStyle("-fx-background-color: #fff020");
        RXCarousel.CarouselData dat1 = new RXCarousel.CarouselData(home, "home");
        RXCarousel.CarouselData dat2 = new RXCarousel.CarouselData(b, "bu");
        RXCarousel.CarouselData dat3 = new RXCarousel.CarouselData(c, "c");
        RXCarousel.CarouselData dat4 = new RXCarousel.CarouselData(a, "a");
        ObservableList<RXCarousel.CarouselData> list = FXCollections.observableArrayList(dat1, dat2, dat3, dat4);
        carousel.setDataList(list);
        carousel.setTransitionType(TransitionType.LINE_TO_LEFT);
        carousel.setScaleType(ScaleType.CENTER_INSIDE);

        RXToggleButton bh = (RXToggleButton) root.lookup("#bh");
        RXToggleButton bb = (RXToggleButton) root.lookup("#bb");
        RXToggleButton bc = (RXToggleButton) root.lookup("#bc");
        RXToggleButton ba = (RXToggleButton) root.lookup("#ba");

        bh.setOnAction(e -> carousel.setSelectedItemIndex(0));
        bb.setOnAction(e -> carousel.setSelectedItemIndex(1));
        bc.setOnAction(e -> carousel.setSelectedItemIndex(2));
        ba.setOnAction(e -> carousel.setSelectedItemIndex(3));


        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
