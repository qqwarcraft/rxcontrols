package test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import rx.controls.RXHighlightLabel;

public class TestRXHighlightLabel extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        TextField textField = new TextField();
        Button btn = new Button("清空");
        btn.setOnAction(e->textField.setText(""));
        HBox hbox = new HBox(textField,btn);
        root.getChildren().add(hbox);
        ObservableList<String> items= FXCollections.observableArrayList(
                "skyhawk@163.com","warcraft@126.com","startstudy@xyz.com","heis@sina.com",
                "13593213112","13232100453","12322113533","13213243450488","97451874135"
        );
        ListView<String> listView = new ListView<>(items);
        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item==null||empty){
                            setGraphic(null);
                            return;
                        }
                        RXHighlightLabel label = new RXHighlightLabel(item);

                        label.keywordsProperty().bind(textField.textProperty());
                        label.setMouseTransparent(true);
                        setGraphic(label);
                    }
                };
            }
        });
        root.getChildren().add(listView);

        textField.textProperty().addListener(ob->{
            String temp = textField.getText();
            FilteredList<String> mySubList = items.filtered(item ->
                    item.toUpperCase().contains(temp.toUpperCase())
            );
            listView.setItems(mySubList);
        });

        root.getStylesheets().add(getClass().getResource("/css/hlabel.css").toExternalForm());
        primaryStage.setScene(new Scene(root,500,460));
        primaryStage.setTitle("高亮文本");
        primaryStage.show();

    }
    public static void main(String[] args){
        launch(args);
    }
}
