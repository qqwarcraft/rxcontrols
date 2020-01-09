package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import rx.controls.RXSVGView;

public class TestSVG extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane root = new AnchorPane();
        RXSVGView sv = new RXSVGView("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!-- Generator: Adobe Illustrator 23.0.1, SVG Export Plug-In . SVG Version: 6.00 Build 0)  -->\n" +
                "<svg version=\"1.1\" id=\"图层_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
                "\t viewBox=\"0 0 64 64\" enable-background=\"new 0 0 64 64\" xml:space=\"preserve\">\n" +
                "<path d=\"M19,55.6l-4.7,4l-1.8-2.2c-0.5-0.6-0.9-1.3-1-2.2c0-0.4,0-0.9,0.1-1.3l1.2-14.5c0.2-1.3,0.6-2.4,1.4-3.2l3.3-3.1l3.3,3.4\n" +
                "\tL19,55.6z\"/>\n" +
                "<path d=\"M21.5,27.3l-4,3.5l-1.9-2c-0.8-0.9-1.4-2-1.5-2.9c0-0.5,0-1.1,0-1.7l1.2-13c0.1-1.8,0.5-2.6,1.9-3.9l2.4-2.5l3.6,4.1\n" +
                "\tL21.5,27.3z\"/>\n" +
                "<path d=\"M39.6,56.3l3.9,4.5l-2,1.7c-1.3,1.2-1.9,1.4-4.4,1.4h-16c-2.4,0-3.4-0.3-4.3-1.4l-1.5-1.6l5.2-4.6L39.6,56.3z\"/>\n" +
                "<path d=\"M24.6,7.7l-3.8-4l1.9-2c1.1-1.2,2-1.6,3.6-1.6h17.4c1.5,0,2.4,0.4,3.4,1.6l1.8,1.9l-5,4.1H24.6z\"/>\n" +
                "<path d=\"M42.4,37.1l4.3-3.8l2.1,2.2c0.6,0.7,1,1.5,1.1,2.5c0.1,0.6,0,1.2,0,1.8L48.6,54c0,0.8-0.2,1.3-0.2,1.6\n" +
                "\tc-0.1,0.6-0.5,1.1-1.5,1.9l-2.4,2.2l-3.7-4.1L42.4,37.1z\"/>\n" +
                "<path d=\"M44.9,8.8l4.9-4.1l1.6,1.8c0.6,0.8,1,1.5,1.1,2.3c0,0.5,0,1-0.1,1.5l-1.2,14c-0.3,2.4-0.8,3.5-2.1,4.6l-2.3,2.1l-3.6-3.7\n" +
                "\tL44.9,8.8z\"/>\n" +
                "</svg>\n");

        root.getChildren().add(sv);
        stage.setScene(new Scene(root, 500, 500));
        stage.show();
        SVGPath path = (SVGPath) sv.lookup("#p-id2");
        path.setFill(Color.BLACK);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
