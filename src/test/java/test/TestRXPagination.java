package test;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import rx.controls.RXPagination;

public class TestRXPagination extends Application {
    TextField tf;
    Pagination pn;
    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root=new BorderPane();
        pn=new RXPagination(100,0);
        pn.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                return new Label("abc Page"+(param+1));
            }
        });
        root.setCenter(pn);
       // Button button = new Button("改变pages");
       // root.setBottom(button);
        primaryStage.setScene(new Scene(root,500,380));
        primaryStage.setTitle("测试窗口");
        primaryStage.show();

//        button.setOnAction(e->{
//            int n = (int) (Math.random()*5);
//            System.out.println("n = " + n);
//            pn.setPageCount(n);
//        });

    }
    public static void main(String[] args){
        launch(args);
    }

}
