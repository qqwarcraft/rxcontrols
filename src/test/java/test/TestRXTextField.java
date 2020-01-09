package test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rx.controls.RXTextField;
import rx.controls.RXTextFieldCopy;
import rx.controls.RXTextFieldDelete;
import rx.enums.DisplayMode;
import rx.event.RXActionEvent;

import java.io.File;

public class TestRXTextField extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(10);

        Button btn = new Button("---");



        RXTextField rtext = new RXTextField("aaabbb");
        rtext.setOnClickButton(new EventHandler<RXActionEvent>() {
            @Override
            public void handle(RXActionEvent event) {
                System.out.println("haha");
            }
        });

        rtext.setButtonDisplayMode(DisplayMode.SHOW);
        RXTextFieldDelete dd = new RXTextFieldDelete("afdasfa");

        RXTextFieldCopy cc = new RXTextFieldCopy();
        cc.setText("123");
        cc.setStyle("-rx-button-display:show");
        RXTextField rt = new RXTextField("文件夹");
        rt.setOnClickButton(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(stage);

            rt.setText(file == null ? "" : file.getAbsolutePath());
        });

        rt.setStyle("-rx-button-display:auto");

        root.getChildren().addAll( btn, rtext, dd, cc, rt);

        // root.getStylesheets().add(getClass().getResource("/css/test2.css").toExternalForm());
        stage.setScene(new Scene(root, 300, 300));
        stage.show();
        // rt.setButtonDisplayMode(DisplayMode.AUTO);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
