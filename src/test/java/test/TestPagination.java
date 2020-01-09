package test;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TestPagination extends Application {

    //测试默认的Pagination,并且展示默认的分页组件如果拓展会出现什么问题

    TextField tf;
    Pagination pn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        pn = new Pagination();
        pn.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                return new Label("abc Page" + (param + 1));
            }
        });
        pn.setPageCount(10);

        root.setCenter(pn);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 450, 275));
        primaryStage.show();


        //当HBox元素发生变化的时候,后面添加的tf和Button都会消失
        HBox box = (HBox) pn.lookup(".control-box");
        tf = new TextField();
        tf.setPrefWidth(50);
        Button btn = new Button("Go");
        btn.setOnAction(e -> gotoPage());
        tf.setOnAction(e -> gotoPage());
        box.getChildren().addAll(tf, btn);

        //pn.setPageCount(20);
    }

    private void gotoPage() {

        int n;
        try {
            n = Integer.parseInt(tf.getText());
            if (n < 1) {
                n = 1;
            } else if (n > pn.getPageCount()) {
                n = pn.getPageCount();
            }
        } catch (Exception e1) {
            n = 1;
        }
        pn.setCurrentPageIndex(n - 1);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
