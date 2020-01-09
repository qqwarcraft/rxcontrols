package test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import rx.controls.RXCarousel;
import rx.controls.RXCarousel.CarouselData;
import rx.controls.RXToggleButton;
import rx.enums.DisplayMode;
import rx.enums.ScaleType;
import rx.enums.TransitionType;

import java.util.Random;

public class TestRXCarousel extends Application {

    /*
     * 在来一种效果, currentIndex < nextIndex 就向下; 如果currentIndex >nextIndex就向上
     * currentIndex<nextIndex就向左, 如果 >就向右
     */

    /*
     * 任务一: 所有自定义组件添加class (包含子组件) 任务二: LFXCarousel
     * 添加一个方法getUserAgentStylesheet() 用于指定一个默认的外观( 用于修改 导航box的皮肤,但是不修改偏移量??
     * 偏移量还是留给CSS修改) 任务三: 设置一个导航box的布局的指定方案. enum nagivateOrientation{horizontal
     * Vertical} hor-->HBox ver->VBox HBOX ,VBOX添加不同的类名 ,方便默认皮肤进行初步修饰
     */
    int index = 0;

    //支持CarouselData(Node,Node)

    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();
        ObservableList<CarouselData> list = FXCollections.observableArrayList();
        // 第一张图片的缩放问题, 或者只有一张图片时候的缩放问题


        list.addAll(new CarouselData(getClass().getResource("/img/1.jpg").toExternalForm(), "宸汐缘"),
                new CarouselData(getIV("7.jpg"), "春花秋月"),
                new CarouselData(getIV("9.jpg"), "九州缥缈录"),
                new CarouselData(getIV("5.jpg"), "陈情令")
        );
//		new CarouselData(getIV("5.jpg"), "陈情令"),
//		new CarouselData(getIV("6.jpg"), "为美貌不可辜负"),
//		new CarouselData(getIV("7.jpg"), "春花秋月")

        RXCarousel carousel = new RXCarousel();
        carousel.setDataList(list);
        carousel.setPrefSize(480, 208);
        carousel.displayTimeProperty().set(Duration.seconds(2.8));
        carousel.transitionTimeProperty().set(Duration.seconds(0.6));
        root.setCenter(carousel);

        Label label07 = new Label("改变轮播条目长度");
        Button btn071 = new Button("增加");
        btn071.setOnAction(e -> {

        });
        Button btn072 = new Button("减少");
        btn072.setOnAction(e -> carousel.getDataList().remove(0));
        HBox hbox07 = new HBox(btn071, btn072);

        Label label06 = new Label("改变组件大小");
        Button btn061 = new Button("随机大小");
        Random r = new Random();
        btn061.setOnAction(e -> {
            int w = r.nextInt(420) + 180;
            int h = r.nextInt(300) + 80;
            carousel.setPrefSize(w, h);
        });
        Button btn062 = new Button("默认大小");
        btn062.setOnAction(e -> carousel.setPrefSize(480, 208));
        HBox hbox06 = new HBox(btn061, btn062);

        Button btn10 = new Button("选择末尾");
        btn10.setOnAction(e -> {
            carousel.setSelectedItemIndex(carousel.getDataList().size() - 1);
        });
        Label label10 = new Label("正在播放第:");
        carousel.selectedIndexProperty().addListener((ob, ov, nv) -> label10.setText("正在播放第:" + carousel.getSelectedIndex()));
        HBox hbox10 = new HBox(btn10, label10);
        hbox10.setAlignment(Pos.CENTER_LEFT);

        Label label05 = new Label("鼠标移入是否暂停");
        RXToggleButton btn051 = new RXToggleButton("自动暂停");
        btn051.setOnAction(e -> carousel.setHoverPause(true));
        RXToggleButton btn052 = new RXToggleButton("关闭自动暂停");
        btn052.setOnAction(e -> carousel.setHoverPause(false));
        ToggleGroup group05 = new ToggleGroup();
        group05.getToggles().addAll(btn051, btn052);
        HBox hbox05 = new HBox(btn051, btn052);
        btn051.setSelected(true);

        Label label04 = new Label("是否自动播放");
        RXToggleButton btn041 = new RXToggleButton("自动播放");
        btn041.setOnAction(e -> carousel.setAutoSwitch(true));
        RXToggleButton btn042 = new RXToggleButton("关闭自动播放");
        btn042.setOnAction(e -> carousel.setAutoSwitch(false));
        ToggleGroup group04 = new ToggleGroup();
        group04.getToggles().addAll(btn041, btn042);
        HBox hbox04 = new HBox(btn041, btn042);
        btn041.setSelected(true);

        Label label08 = new Label("自动播放时间间隔");
        Button btn081 = new Button("增加时间");
        btn081.setOnAction(e -> {
            carousel.setDisplayTime(carousel.getDisplayTime().add(Duration.millis(200)));
        });
        Button btn082 = new Button("减少时间");
        btn082.setOnAction(e -> {
            carousel.setDisplayTime(carousel.getDisplayTime().subtract(Duration.millis(200)));
        });
        HBox hbox08 = new HBox(btn081, btn082);

        Label label03 = new Label("是否显示导航");
        ObservableList<DisplayMode> list03 = FXCollections.observableArrayList(DisplayMode.values());
        ComboBox<DisplayMode> comboBox03 = new ComboBox<DisplayMode>(list03);
        comboBox03.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
            carousel.setNavDisplayMode(nv);
        });
        comboBox03.getSelectionModel().select(DisplayMode.SHOW);

        Label label02 = new Label("选择导航方向");
        RXToggleButton btn1 = new RXToggleButton("垂直");
        btn1.setOnAction(e -> carousel.setNavOrientation(Orientation.VERTICAL));
        RXToggleButton btn2 = new RXToggleButton("水平");
        btn2.setOnAction(e -> carousel.setNavOrientation(Orientation.HORIZONTAL));
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(btn1, btn2);
        HBox hbox02 = new HBox(btn1, btn2);
        btn2.setSelected(true);

        Label labelScale = new Label("缩放模式");
        ObservableList<ScaleType> listScale = FXCollections.observableArrayList(ScaleType.values());
        ComboBox<ScaleType> comboBoxScale = new ComboBox<ScaleType>(listScale);
        comboBoxScale.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
            carousel.setScaleType(nv);
        });
        comboBoxScale.getSelectionModel().select(ScaleType.CENTER_INSIDE);

        Label lbelTT = new Label("过渡动画");
        ObservableList<TransitionType> listType = FXCollections.observableArrayList(TransitionType.values());
        ComboBox<TransitionType> comboBoxTT = new ComboBox<TransitionType>(listType);
        comboBoxTT.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
            carousel.setTransitionType(nv);
        });
        comboBoxTT.getSelectionModel().select(TransitionType.MOVE_TO_LEFT);

        Label label09 = new Label("过渡动画时间");
        Button btn091 = new Button("增加时间");
        btn091.setOnAction(e -> {
            carousel.setTransitionTime(carousel.getTransitionTime().add(Duration.millis(200)));
        });
        Button btn092 = new Button("减少时间");
        btn092.setOnAction(e -> {
            carousel.setTransitionTime(carousel.getTransitionTime().subtract(Duration.millis(200)));
        });
        HBox hbox09 = new HBox(btn091, btn092);

        Label label01 = new Label("箭头显示");
        ObservableList<DisplayMode> list01 = FXCollections.observableArrayList(DisplayMode.values());
        ComboBox<DisplayMode> comboBox01 = new ComboBox<DisplayMode>(list01);
        comboBox01.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
            carousel.setArrowDisplayMode(nv);
        });
        comboBox01.getSelectionModel().select(DisplayMode.AUTO);

        VBox boxR = new VBox(hbox10, label07, hbox07, label06, hbox06, label05, hbox05, label04, hbox04, label08, hbox08, label03, comboBox03, label02, hbox02, label01, comboBox01,
                labelScale, comboBoxScale, lbelTT, comboBoxTT, label09, hbox09);
        boxR.getStyleClass().add("vbox");
        boxR.setPadding(new Insets(0, 0, 0, 50));
        root.setRight(boxR);

        root.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());
        Scene scene = new Scene(root, 1000, 690);
        PerspectiveCamera c = new PerspectiveCamera();
        scene.setCamera(c);
        stage.setScene(scene);
        stage.show();
        // nl.add(0,new Pair<Node, String>(getIV("2.png"), "新增"));

    }

    public static void main(String[] args) {
        launch(args);
    }

    private static ImageView getIV(String path) {
        ImageView imageView = new ImageView(TestRXCarousel.class.getResource("/img/" + path).toExternalForm());
        imageView.setPreserveRatio(true);
        HBox hbox = new HBox(imageView);
        hbox.setAlignment(Pos.CENTER);
        imageView.setOnMouseClicked(e -> {
            System.out.println(path);
        });
        return imageView;
    }

    private static ImageView getTestIV(String path) {
        ImageView imageView = new ImageView(TestRXCarousel.class.getResource("/image/" + path).toExternalForm());
        imageView.setPreserveRatio(true);
        HBox hbox = new HBox(imageView);
        hbox.setAlignment(Pos.CENTER);
        imageView.setOnMouseClicked(e -> {
            System.out.println(path);
        });
        return imageView;
    }
}
