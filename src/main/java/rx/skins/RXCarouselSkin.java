package rx.skins;

import java.util.Random;
import java.util.concurrent.Callable;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import rx.controls.RXCarousel;
import rx.controls.RXCarousel.CarouselData;
import rx.controls.RXToggleButton;
import rx.enums.DisplayMode;
import rx.enums.ScaleType;
import rx.enums.TransitionType;

public class RXCarouselSkin extends SkinBase<RXCarousel> {
    private static final Duration DURATION_ARROW_SHOW = Duration.millis(180);
    // 变量
    // private final Label tips = new Label("轮播组件无内容");//没有内容的提示
    private RXCarousel control;// 控件
    private StackPane contentPane;// 底层容器,用于添加其他容器/控件
    private Pane topPane;// 顶层容器,用于存放导航按钮,左右方向按键
    private StackPane leftButton;// 左方向按键
    private Region leftArrow;//左箭图形
    private StackPane rightButton;// 右方向按键
    private Region rightArrow;//右箭图形
    private FlowPane navBar;// 导航栏 ;存放每一个页面的快捷按钮
    private StackPane currentPage;// 当前页面
    private int currentIndex;// 当前索引值
    private StackPane nextPage;// 下一页面
    private int nextIndex;// 下一页索引
    private ToggleGroup btnGroup;// 每一个页面的快捷按钮
    private Timeline autoAnimation = new Timeline();// 自动切换时候的线程,用于控制比如每隔多长时间进行一次切换;
    private Timeline switchAnimation = new Timeline();// 切换动画,用于显示不同的切换效果
    private ObservableList<CarouselData> dataList;// 用于需要显示的数据
    private KeyValue[] kvs;// 存储动画关键帧的值
    private Timeline tlArrowShow = new Timeline();// 箭头自动显示
    private Timeline tlArrowHide = new Timeline();// 箭头自动隐藏
    private Timeline tlNavShow = new Timeline();// 箭头自动显示
    private Timeline tlNavHide = new Timeline();// 箭头自动隐藏
    private Random random = new Random();
    private static final int TYPE_LENGTH = TransitionType.values().length;
    private static final  TransitionType[] BOX_TYPES = {TransitionType.BOX_TO_LEFT, TransitionType.BOX_TO_RIGHT, TransitionType.BOX_TO_TOP, TransitionType.BOX_TO_BOTTOM,};
    // 监听器 Listener....
    // 尺寸变化
    private ChangeListener<Number> sizeListener = (ob, ov, nv) -> {
        setAllItemSize();
    };
    // 导航位置改变
    private InvalidationListener navDirListener = ob -> setLocationNavBar();
    // 箭头显示模式的改变
    private InvalidationListener arrowDisplayListener = ob -> arrowShowCon();
    // 箭头与边框距离改变

    // 导航显示变化
    private InvalidationListener showNavListener = ob -> showNavBarCon();
    // 数据变化
    private InvalidationListener dataListListener = ob -> {
        setAllItemSize();
        initNavBar();
        if (control.autoSwitchProperty().get()) {
            stopSwitchAnimation();
            stopAutoAnimation();
            initAutoAnimation();
            autoAnimation.play();
        }
    };
    // 缩放模式变化
    private ChangeListener<ScaleType> scaleTypeListener = (ob, ov, nv) -> setAllItemSize();

    // 自动播放变化
    private ChangeListener<Boolean> autoPlayListener = (ob, ov, nv) -> {
        if (nv) {
            playAutoAnimation();
        } else {
            stopAutoAnimation();
        }
    };

    // 显示时间变化 或者 过渡动画时间 变化
    private ChangeListener<Duration> timeChangeListener = (ob, ov, nv) -> {
        if (control.autoSwitchProperty().get()) {
            playAutoAnimation();
        } else {
            stopAutoAnimation();
        }
    };
    // 移入暂停 或者 移除时自动播放
    private ChangeListener<Boolean> hoverPasueListener = (ob, ov, nv) -> {
        if (control.autoSwitchProperty().get() && control.hoverPauseProperty().get()) {
            if (nv && autoAnimation != null) {
                autoAnimation.pause();
            } else {
                autoAnimation.play();
            }
        }
    };

    // 构造器
    public RXCarouselSkin(RXCarousel control, ReadOnlyIntegerWrapper ow) {
        super(control);
        this.control = control;

        dataList = control.dataListProperty().get();// 获取数据

        initGui();// 初始化 容器,控件,添加css类名

        IntegerBinding intBinding = Bindings.createIntegerBinding(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getTgBtnIndex(btnGroup.getSelectedToggle());
            }
        }, btnGroup.selectedToggleProperty());

        ow.bind(intBinding);// 给只读包装的类设置值

        // 尺寸变化调整
        control.widthProperty().addListener(sizeListener);
        control.heightProperty().addListener(sizeListener);
        // 导航位置的调整
        control.navOrientationProperty().addListener(navDirListener);
        // 箭头显示与否的调整
        control.arrowDisplayModeProperty().addListener(arrowDisplayListener);
        // 导航是否显示
        control.navDisplayModeProperty().addListener(showNavListener);
        // 数据变化
        control.dataListProperty().addListener(dataListListener);
        // 图片缩放模式改变
        control.scaleTypeProperty().addListener(scaleTypeListener);
        // 自动播放的改变
        control.autoSwitchProperty().addListener(autoPlayListener);
        // 显示时间的改变
        control.displayTimeProperty().addListener(timeChangeListener);
        // 过渡时间的改变
        control.transitionTimeProperty().addListener(timeChangeListener);
        // 移入自动暂停;移除根据情况是否开始
        control.hoverProperty().addListener(hoverPasueListener);

    }

    private void setAllItemSize() {
        if (isEmpty(dataList)) {
            return;
        }

        // 获取缩放类型
        ScaleType scaleType = control.scaleTypeProperty().get();
        for (int i = 0; i < dataList.size(); i++) {
            Node node = dataList.get(i).getNode();
            setItemSize(node, scaleType);
        }

    }

    private void setItemSize(Node node, ScaleType scaleType) {
        if (node == null) {
            return;
        }
        // 复原之前的绑定和缩放
        node.scaleXProperty().unbind();
        node.scaleYProperty().unbind();
        node.setScaleX(1.0);
        node.setScaleY(1.0);
        node.setClip(null);
        // 获取一个比例 页面的size/组件的size
        double dbx = control.prefWidthProperty().divide(node.prefWidth(-1)).get();
        double dby = control.prefHeightProperty().divide(node.prefHeight(-1)).get();
        if (scaleType == ScaleType.CENTER_INSIDE) {
            // 图片大小小于控件大小，就直接居中展示该图片
            if (dbx > 1 && dby > 1) {
                //continue;
            } else if (dbx > 1) {// 如果横向太大,那么缩小横向
                node.scaleXProperty().set(dby);
                node.scaleYProperty().set(dby);
            } else if (dby > 1) {// 如果垂直方向太大,那么缩小垂直方向
                node.scaleXProperty().set(dbx);
                node.scaleYProperty().set(dbx);
            } else {

                if (Double.compare(dbx, dby) > 0) {
                    node.scaleXProperty().set(dby);
                    node.scaleYProperty().set(dby);
                } else {
                    node.scaleXProperty().set(dbx);
                    node.scaleYProperty().set(dbx);
                }
            }

        } else if (scaleType == ScaleType.FIT_XY) { // 拉伸, 不成比例
            node.scaleXProperty().set(dbx);
            node.scaleYProperty().set(dby);
        } else if (scaleType == ScaleType.FIT_CENTER) {

            if (dbx > 1 && dby > 1) {
                if (Double.compare(dbx, dby) > 0) {
                    node.scaleXProperty().set(dby);
                    node.scaleYProperty().set(dby);
                } else {
                    node.scaleXProperty().set(dbx);
                    node.scaleYProperty().set(dbx);
                }
            } else if (dbx > 1) {
                node.scaleXProperty().set(dby);
                node.scaleYProperty().set(dby);
            } else if (dby > 1) {
                node.scaleXProperty().set(dbx);
                node.scaleYProperty().set(dbx);
            } else {
                if (Double.compare(dbx, dby) > 0) {
                    node.scaleXProperty().set(dby);
                    node.scaleYProperty().set(dby);
                } else {
                    node.scaleXProperty().set(dbx);
                    node.scaleYProperty().set(dbx);
                }
            }
        }
    }

    private void initNavBar() {
        // 清空之前的数据
        btnGroup.selectedToggleProperty().removeListener(selectedListener);
        btnGroup.getToggles().clear();
        navBar.getChildren().clear();
        if (isEmpty(dataList)) {
            currentPage.getChildren().clear();
            nextPage.getChildren().clear();
            return;
        }
        for (int i = 0; i < dataList.size(); i++) {
            RXToggleButton btn = new RXToggleButton(dataList.get(i).getTitle());
            btn.getStyleClass().add("nav-button");
            btn.setId("nav-button" + i);
            btn.managedProperty().bind(btn.visibleProperty());// 可见和可管理绑定
            navBar.getChildren().add(btn);
            btnGroup.getToggles().add(btn);
            btn.hoverProperty().addListener((ob, ov, nv) -> {
                if (nv) {
                    btnGroup.selectToggle(btn);
                }
            });
        }

        btnGroup.selectedToggleProperty().addListener(selectedListener);
        btnGroup.getToggles().get(0).setSelected(true);// 初始选中第一页

    }

    // 创建切换的时间轴动画
    private void fillSwitchTimeline() {
        switchAnimation.getKeyFrames().setAll(new KeyFrame(control.getTransitionTime(), kvs));
    }

    ChangeListener<? super Toggle> selectedListener = (ob, ov, nv) -> {
        if (switchAnimation != null && switchAnimation.getStatus() == Animation.Status.RUNNING) {
            switchAnimation.stop();
        }
        currentIndex = getTgBtnIndex(ov);
        nextIndex = getTgBtnIndex(nv);
        if (currentIndex == -1 && nextIndex != -1) {
            nextPage.getChildren().setAll(dataList.get(nextIndex).getNode());
            currentPage.toBack();
            return;
        }

        if (currentIndex != -1 && nextIndex == -1) {
            currentPage.getChildren().setAll(dataList.get(currentIndex).getNode());
            nextPage.toBack();
            return;
        }

        if (currentIndex == -1 && nextIndex == -1) {
            currentPage.getChildren().setAll(dataList.get(0).getNode());
            nextPage.toBack();
            return;
        }
        // 没有过度效果
        // currentPage.getChildren().setAll(dataList.get(currentIndex).getNode());
        // nextPage.getChildren().setAll(dataList.get(nextIndex).getNode());
        // currentPage.toBack();
        boolean direction = currentIndex < nextIndex;// true正方向 false逆方向

        // 从最后一个切换到第一个 不算逆序
        if (currentIndex == dataList.size() - 1 && nextIndex == 0) {
            direction = true;
        }

        // 从第一个,切换到最后一个算不算逆序
        if (currentIndex == 0 && nextIndex == dataList.size() - 1) {
            direction = false;
        }
        // 如果就两页,都算正序
        if (dataList.size() == 2) {
            direction = true;
        }

        currentPage.getChildren().setAll(dataList.get(currentIndex).getNode());
        nextPage.getChildren().setAll(dataList.get(nextIndex).getNode());

        TransitionType type = control.transitionTypeProperty().get();
        // 设置动画开始前,两个页面的初始值
        currentPage.setTranslateY(0);
        currentPage.setTranslateX(0);
        currentPage.setOpacity(1.0);
        currentPage.setEffect(null);
        currentPage.setVisible(true);
        currentPage.setClip(null);

        nextPage.setVisible(true);
        nextPage.setTranslateX(0);
        nextPage.setTranslateY(0);
        nextPage.setOpacity(1.0);
        nextPage.setEffect(null);
        nextPage.setClip(null);

        currentPage.toBack();

        kvs = new KeyValue[20];
        Bounds paneBounds = contentPane.getBoundsInParent();
        double w = paneBounds.getWidth();
        double h = paneBounds.getHeight();
        if (type == TransitionType.RANDOM) {
            while(true){
                type = TransitionType.values()[random.nextInt(TYPE_LENGTH )];
                if(type!=TransitionType.RANDOM&&type!=TransitionType.BOX_RANDOM){
                    break;
                }
            }
        } else if (type == TransitionType.BOX_RANDOM) {
            type =BOX_TYPES[random.nextInt(BOX_TYPES.length)];
        }
        // 水平方向的移动
        if (type == TransitionType.MOVE_TO_LEFT || type == TransitionType.MOVE_TO_RIGHT) {
            if (type == TransitionType.MOVE_TO_RIGHT) {
                direction = !direction;
                nextPage.toBack();
            }
            nextPage.setTranslateX(direction ? w : -w);
            kvs[0] = new KeyValue(currentPage.translateXProperty(), direction ? -w : w);
            kvs[1] = new KeyValue(nextPage.translateXProperty(), 0);
            fillSwitchTimeline();
            // 垂直方向的移动
        } else if (type == TransitionType.MOVE_TO_TOP || type == TransitionType.MOVE_TO_BOTTOM) {
            if (type == TransitionType.MOVE_TO_BOTTOM) {
                direction = !direction;
            }
            nextPage.setTranslateY(direction ? h : -h);
            kvs[0] = new KeyValue(currentPage.translateYProperty(), direction ? -h : h);
            kvs[1] = new KeyValue(nextPage.translateYProperty(), 0);
            fillSwitchTimeline();
            // 淡入淡出 // FadeTransition 也可以实现淡入淡出
        } else if (type == TransitionType.FADE) {
            currentPage.setOpacity(1.0);
            nextPage.setOpacity(0.0);
            kvs[0] = new KeyValue(currentPage.opacityProperty(), 0);
            kvs[1] = new KeyValue(nextPage.opacityProperty(), 1);
            fillSwitchTimeline();
            // 没有切换效果
        } else if (type == TransitionType.NONE) {
            currentPage.setOpacity(0.0);
            nextPage.setOpacity(1.0);
        } else if (type == TransitionType.CIRCLE) {
            Circle c1 = new Circle();
            c1.setCenterX(w / 2);
            c1.setCenterY(h / 2);
            // double maxRadius = Math.max(w, h) * Math.sqrt(2) / 2;
            double r = Math.sqrt(w * w + h * h) / 2;// 外接圆半径
            if (direction) {
                currentPage.setOpacity(1.0);
                kvs[0] = new KeyValue(currentPage.opacityProperty(), 0);
                nextPage.setClip(c1);
                kvs[1] = new KeyValue(c1.radiusProperty(), r);
                fillSwitchTimeline();
            } else {
                nextPage.setOpacity(0.0);
                kvs[0] = new KeyValue(nextPage.opacityProperty(), 1);
                nextPage.toBack();
                c1.setRadius(r);
                currentPage.setClip(c1);
                kvs[1] = new KeyValue(c1.radiusProperty(), 0);
            }
            fillSwitchTimeline();
        } else if (type == TransitionType.GAUSSIAN_BLUR) {
            nextPage.toBack();
            GaussianBlur gs = new GaussianBlur();
            gs.setRadius(0);
            currentPage.setEffect(gs);
            kvs[0] = new KeyValue(gs.radiusProperty(), 30);
            kvs[1] = new KeyValue(currentPage.opacityProperty(), 0);

            GaussianBlur gs2 = new GaussianBlur();
            gs2.setRadius(30);
            nextPage.setEffect(gs2);
            nextPage.setOpacity(0);
            kvs[2] = new KeyValue(gs2.radiusProperty(), 0);
            kvs[3] = new KeyValue(nextPage.opacityProperty(), 1);
            fillSwitchTimeline();
        } else if (type == TransitionType.MOTION_BLUR) {
            nextPage.toBack();
            MotionBlur mb = new MotionBlur();
            mb.setRadius(0);
            currentPage.setEffect(mb);
            kvs[0] = new KeyValue(mb.radiusProperty(), 63);
            kvs[1] = new KeyValue(currentPage.opacityProperty(), 0);
            fillSwitchTimeline();
        } else if (type == TransitionType.LINE_TO_LEFT || type == TransitionType.LINE_TO_RIGHT) {
            if (type == TransitionType.LINE_TO_RIGHT) {
                direction = !direction;
            }
            Rectangle showClip = new Rectangle();
            Rectangle hideClip = new Rectangle();
            showClip.setHeight(h);
            hideClip.setHeight(h);
            hideClip.setWidth(w);
            // 显示nextPage ,隐藏currentPage
            if (direction) {
                showClip.setWidth(0);
                nextPage.setClip(showClip);
                kvs[0] = new KeyValue(showClip.widthProperty(), paneBounds.getWidth());
                currentPage.setClip(hideClip);
                hideClip.translateXProperty().bind(showClip.widthProperty());
            } else {
                nextPage.toBack();
                currentPage.setClip(hideClip);
                kvs[0] = new KeyValue(hideClip.widthProperty(), 0);// 隐藏时改变它的宽度
                showClip.setWidth(paneBounds.getWidth());
                nextPage.setClip(showClip);
                showClip.translateXProperty().bind(hideClip.widthProperty());// 显示改变他的偏移量
            }
            fillSwitchTimeline();
        } else if (type == TransitionType.LINE_TO_BOTTOM || type == TransitionType.LINE_TO_TOP) {
            if (type == TransitionType.LINE_TO_TOP) {
                direction = !direction;
            }
            Rectangle showClip = new Rectangle();
            Rectangle hideClip = new Rectangle();
            showClip.setWidth(w);
            hideClip.setWidth(w);
            hideClip.setHeight(h);
            // 显示nextPage ,隐藏currentPage
            if (direction) {
                showClip.setHeight(0);
                nextPage.setClip(showClip);
                kvs[0] = new KeyValue(showClip.heightProperty(), paneBounds.getHeight());
                currentPage.setClip(hideClip);
                hideClip.translateYProperty().bind(showClip.heightProperty());
            } else {
                nextPage.toBack();
                currentPage.setClip(hideClip);
                kvs[0] = new KeyValue(hideClip.heightProperty(), 0);// 隐藏时改变它的宽度
                showClip.setHeight(paneBounds.getHeight());
                nextPage.setClip(showClip);
                showClip.translateYProperty().bind(hideClip.heightProperty());// 显示改变他的偏移量
            }
            fillSwitchTimeline();
        } else if (type == TransitionType.BOX_TO_LEFT || type == TransitionType.BOX_TO_RIGHT) {
            if (type == TransitionType.BOX_TO_RIGHT) {
                direction = !direction;
            }
            double offset = Math.sqrt(w * w / 2);
            nextPage.setTranslateX(direction ? w : -w);

            kvs[0] = new KeyValue(currentPage.translateXProperty(), direction ? -w : w);
            kvs[1] = new KeyValue(nextPage.translateXProperty(), 0);
            if (direction) {
                PerspectiveTransform pt1 = new PerspectiveTransform();
                pt1.setUlx(0.0);// 源的左上角x映射到该坐标
                pt1.setUly(0.0);// 源的左上角y映射到该坐标
                pt1.setUrx(0);// 源右上角x坐标映射到该坐标
                pt1.setUry(-offset);// 源右上角y坐标映射到该坐标
                pt1.setLrx(0);// 源的右下角x映射到该坐标。
                pt1.setLry(nextPage.heightProperty().get() - offset);// 源的右下角y映射到该坐标。
                pt1.setLlx(0.0);// 源的左下角x映射到该坐标。
                pt1.setLly(nextPage.heightProperty().get());// 源的左下角y映射到该坐标。
                nextPage.setEffect(pt1);

                kvs[2] = new KeyValue(pt1.lryProperty(), nextPage.heightProperty().get());
                kvs[3] = new KeyValue(pt1.uryProperty(), 0);
                kvs[4] = new KeyValue(pt1.urxProperty(), nextPage.widthProperty().get());
                kvs[5] = new KeyValue(pt1.lrxProperty(), nextPage.widthProperty().get());

                PerspectiveTransform pt2 = new PerspectiveTransform();
                pt2.setUlx(0.0);// 源的左上角x映射到该坐标
                pt2.setUly(0.0);// 源的左上角y映射到该坐标
                pt2.setUrx(currentPage.widthProperty().get());// 源右上角x坐标映射到该坐标
                pt2.setUry(0);// 源右上角y坐标映射到该坐标
                pt2.setLrx(currentPage.widthProperty().get());// 源的右下角x映射到该坐标。
                pt2.setLry(currentPage.heightProperty().get());// 源的右下角y映射到该坐标。
                pt2.setLlx(0.0);// 源的左下角x映射到该坐标。
                pt2.setLly(currentPage.heightProperty().get());// 源的左下角y映射到该坐标。
                currentPage.setEffect(pt2);

                kvs[6] = new KeyValue(pt2.llyProperty(), currentPage.heightProperty().get() - offset);
                kvs[7] = new KeyValue(pt2.ulyProperty(), -offset);
                kvs[8] = new KeyValue(pt2.ulxProperty(), currentPage.widthProperty().get());
                kvs[9] = new KeyValue(pt2.llxProperty(), currentPage.widthProperty().get());
            } else {
                PerspectiveTransform pt1 = new PerspectiveTransform();

                pt1.setUlx(w);// 源左上角x坐标映射到该坐标
                pt1.setUly(-offset);// 源左上角y坐标映射到该坐标
                pt1.setUrx(w);// 源的右上角x映射到该坐标
                pt1.setUry(0.0);// 源的右上角y映射到该坐标

                pt1.setLlx(w);// 源的右下角x映射到该坐标。
                pt1.setLly(h - offset);// 源的右下角y映射到该坐标。
                pt1.setLrx(w);// 源的左下角x映射到该坐标。
                pt1.setLry(h);// 源的左下角y映射到该坐标。
                nextPage.setEffect(pt1);

                kvs[2] = new KeyValue(pt1.ulxProperty(), 0);
                kvs[3] = new KeyValue(pt1.ulyProperty(), 0);
                kvs[4] = new KeyValue(pt1.llxProperty(), 0);
                kvs[5] = new KeyValue(pt1.llyProperty(), h);

                PerspectiveTransform pt2 = new PerspectiveTransform();
                pt2.setUlx(0.0);// 源的左上角x映射到该坐标
                pt2.setUly(0.0);// 源的左上角y映射到该坐标
                pt2.setUrx(w);// 源右上角x坐标映射到该坐标
                pt2.setUry(0);// 源右上角y坐标映射到该坐标
                pt2.setLrx(w);// 源的右下角x映射到该坐标。
                pt2.setLry(h);// 源的右下角y映射到该坐标。
                pt2.setLlx(0.0);// 源的左下角x映射到该坐标。
                pt2.setLly(h);// 源的左下角y映射到该坐标。
                currentPage.setEffect(pt2);

                kvs[6] = new KeyValue(pt2.uryProperty(), -offset);
                kvs[7] = new KeyValue(pt2.lryProperty(), h - offset);
                kvs[8] = new KeyValue(pt2.urxProperty(), 0);
                kvs[9] = new KeyValue(pt2.lrxProperty(), 0);
            }
            fillSwitchTimeline();

        } else if (type == TransitionType.BOX_TO_TOP || type == TransitionType.BOX_TO_BOTTOM) {
            if (type == TransitionType.BOX_TO_BOTTOM) {
                direction = !direction;
            }
            double offset = Math.sqrt(h * h / 2);
            nextPage.setTranslateY(direction ? h : -h);

            if (direction) {
                kvs[0] = new KeyValue(currentPage.translateYProperty(), -h);
                kvs[1] = new KeyValue(nextPage.translateYProperty(), 0);
                PerspectiveTransform pt1 = new PerspectiveTransform();
                pt1.setUlx(0);
                pt1.setUly(0);

                pt1.setUrx(w);
                pt1.setUry(0);

                pt1.setLrx(w);
                pt1.setLry(h);

                pt1.setLlx(0);
                pt1.setLly(h);
                currentPage.setEffect(pt1);
                kvs[2] = new KeyValue(pt1.ulxProperty(), offset);
                kvs[3] = new KeyValue(pt1.ulyProperty(), h);
                kvs[4] = new KeyValue(pt1.urxProperty(), w + offset);
                kvs[5] = new KeyValue(pt1.uryProperty(), h);

                PerspectiveTransform pt2 = new PerspectiveTransform();
                pt2.setUlx(0);
                pt2.setUly(0);

                pt2.setUrx(w);
                pt2.setUry(0);

                pt2.setLrx(w + offset);
                pt2.setLry(h - offset);

                pt2.setLlx(offset);
                pt2.setLly(h - offset);
                nextPage.setEffect(pt2);
                kvs[6] = new KeyValue(pt2.lrxProperty(), w);
                kvs[7] = new KeyValue(pt2.lryProperty(), h);
                kvs[8] = new KeyValue(pt2.llxProperty(), 0);
                kvs[9] = new KeyValue(pt2.llyProperty(), h);
            } else {
                // kvs[0] = new KeyValue(currentPage.translateYProperty(),
                // direction ? -h : h);
                kvs[1] = new KeyValue(nextPage.translateYProperty(), 0);
                PerspectiveTransform pt1 = new PerspectiveTransform();
                pt1.setUlx(0);
                pt1.setUly(0);

                pt1.setUrx(w);
                pt1.setUry(0);

                pt1.setLrx(w);
                pt1.setLry(h);

                pt1.setLlx(0);
                pt1.setLly(h);
                currentPage.setEffect(pt1);
                kvs[2] = new KeyValue(pt1.ulyProperty(), h);
                kvs[3] = new KeyValue(pt1.uryProperty(), h);
                kvs[4] = new KeyValue(pt1.llxProperty(), offset);
                kvs[5] = new KeyValue(pt1.lrxProperty(), w + offset);

                PerspectiveTransform pt2 = new PerspectiveTransform();
                pt2.setUlx(offset);
                pt2.setUly(offset);

                pt2.setUrx(w + offset);
                pt2.setUry(offset);

                pt2.setLrx(w);
                pt2.setLry(h);

                pt2.setLlx(0);
                pt2.setLly(h);
                nextPage.setEffect(pt2);
                kvs[6] = new KeyValue(pt2.ulxProperty(), 0);
                kvs[7] = new KeyValue(pt2.ulyProperty(), 0);
                kvs[8] = new KeyValue(pt2.urxProperty(), w);
                kvs[9] = new KeyValue(pt2.uryProperty(), 0);
            }
            fillSwitchTimeline();

        } else if (type == TransitionType.BLEND) {
            Blend value = new Blend(BlendMode.ADD);
            value.setOpacity(1);
            nextPage.setEffect(value);
            kvs[0] = new KeyValue(value.opacityProperty(), 0);
            fillSwitchTimeline();
        } else if (type == TransitionType.SECTOR) {
            double r = Math.sqrt(w * w + h * h) / 2.0;
            Arc arc1 = new Arc(w / 2, h / 2, r, r, 90, 0);
            arc1.setType(ArcType.ROUND);
            nextPage.setClip(arc1);
            kvs[0] = new KeyValue(arc1.lengthProperty(), direction ? -360 : 360);
            Arc arc2 = new Arc(w / 2, h / 2, r, r, 90, direction ? 360 : -360);
            arc2.setType(ArcType.ROUND);
            currentPage.setClip(arc2);
            kvs[1] = new KeyValue(arc2.lengthProperty(), 0);
            fillSwitchTimeline();
        } else if (type == TransitionType.RECTANGLE) {
            Rectangle rect = new Rectangle();
            double min = Math.min(w, h);
            if (direction) {
                rect.setX(w / 2d);
                rect.setY(h / 2d);
                rect.setWidth(0d);
                rect.setHeight(0d);
                nextPage.setClip(rect);
                kvs[0] = new KeyValue(rect.widthProperty(), w);
                kvs[1] = new KeyValue(rect.heightProperty(), h);
                kvs[2] = new KeyValue(rect.xProperty(), 0);
                kvs[3] = new KeyValue(rect.yProperty(), 0);
                kvs[4] = new KeyValue(currentPage.opacityProperty(), 0);
            } else {
                nextPage.toBack();
                nextPage.setOpacity(0);
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(w);
                rect.setHeight(h);
                currentPage.setClip(rect);
                kvs[0] = new KeyValue(rect.widthProperty(), 0);
                kvs[1] = new KeyValue(rect.heightProperty(), 0);
                kvs[2] = new KeyValue(rect.xProperty(), w / 2d);
                kvs[3] = new KeyValue(rect.yProperty(), h / 2d);
                kvs[4] = new KeyValue(nextPage.opacityProperty(), 1);
            }
            fillSwitchTimeline();
        }

        // 如果改变了大小, 那么会处于clip状态,导致不能完全显示;所以需要清除clip,并且还要把当前页面放在后面
        if (type != TransitionType.NONE && switchAnimation != null) {
            switchAnimation.setOnFinished(e -> {

                if (currentPage != null) {
                    currentPage.setClip(null);
                    // currentPage.setEffect(null);
                    currentPage.toBack();
                    currentPage.setTranslateX(Double.MAX_VALUE);
                }

                if (nextPage != null) {
                    nextPage.setClip(null);
                    // nextPage.setEffect(null);
                }

            });
            switchAnimation.play();
        }

    };

    // 获取该 切换按钮 在ToggleGroup里的位置
    private int getTgBtnIndex(Toggle tg) {
        ObservableList<Toggle> toggles = btnGroup.getToggles();
        for (int i = 0; i < toggles.size(); i++) {
            if (tg == toggles.get(i)) {
                return i;
            }
        }
        return -1;
    }

    private void initGui() {
        currentIndex = 0;
        nextIndex = 1;
        btnGroup = new ToggleGroup();

        // 初始化控件
        contentPane = new StackPane();
        topPane = new Pane();
        // topPane.setMouseTransparent(true);//该节点和子节点都无法被点击
        topPane.setPickOnBounds(false);// 几何图像来计算是否被点击. 透明部分不算点击

        leftButton = new StackPane();
        leftArrow = new Region();
        leftButton.getChildren().setAll(leftArrow);

        rightButton = new StackPane();
        rightArrow = new Region();
        rightButton.getChildren().setAll(rightArrow);

        navBar = new FlowPane(control.getNavOrientation());// 设置导航的方向为水平 or 垂直

        currentPage = new StackPane();
        nextPage = new StackPane();
        // 给控件添加css类名
        contentPane.getStyleClass().add("content-pane");
        topPane.getStyleClass().add("top-pane");
        leftButton.getStyleClass().add("left-button");
        leftArrow.getStyleClass().add("left-arrow");
        rightButton.getStyleClass().add("right-button");
        rightArrow.getStyleClass().add("right-arrow");
        navBar.getStyleClass().add("nav-bar");
        currentPage.getStyleClass().add("current-page");
        nextPage.getStyleClass().add("next-page");

        // 裁剪容器(避免显示的部分超出容器的范围)
        clipRegion(control);
        clipRegion(contentPane);
        clipRegion(topPane);

        // 绑定宽高
        DoubleProperty dbw = control.prefWidthProperty();
        DoubleProperty dbh = control.prefHeightProperty();
        contentPane.minWidthProperty().bind(dbw);
        contentPane.prefWidthProperty().bind(dbw);
        contentPane.minHeightProperty().bind(dbh);
        contentPane.prefHeightProperty().bind(dbh);
        currentPage.minWidthProperty().bind(dbw);
        currentPage.minHeightProperty().bind(dbh);
        nextPage.minWidthProperty().bind(dbw);
        nextPage.minHeightProperty().bind(dbh);
        topPane.minWidthProperty().bind(dbw);
        topPane.minHeightProperty().bind(dbh);
        // 顶层容器添加组件
        topPane.getChildren().setAll(navBar, leftButton, rightButton);
        // 底层容器添加组件
        contentPane.getChildren().setAll(currentPage, nextPage, topPane);
        // 添加底层容器到组件里
        getChildren().setAll(contentPane);
        getSkinnable().requestLayout();
        setAllItemSize();

        initTopPane();

        if (control.autoSwitchProperty().get()) {
            playAutoAnimation();
        }

    }

    private void initAutoAnimation() {
        stopAutoAnimation();
        if (dataList.size() <= 1) {
            return;
        }
        // 自动切换的时间间隔=显示时间+过渡时间
        autoAnimation = new Timeline(
                new KeyFrame(control.displayTimeProperty().get().add(control.transitionTimeProperty().get()), event -> {
                    if (dataList != null && dataList.size() > 1) {
                        nextIndex = ((int) getTgBtnIndex(btnGroup.getSelectedToggle()) + 1) % dataList.size();
                        btnGroup.getToggles().get(nextIndex).setSelected(true);
                    }
                }));

        autoAnimation.setCycleCount(Animation.INDEFINITE);
    }

    private void stopSwitchAnimation() {
        if (switchAnimation != null) {
            switchAnimation.stop();
        }
    }

    private void stopAutoAnimation() {
        if (autoAnimation != null) {
            autoAnimation.stop();
        }
    }

    private void playAutoAnimation() {
        initAutoAnimation();
        if (autoAnimation != null) {
            autoAnimation.play();
        }
    }

    // 判断是否为空集合
    private boolean isEmpty(ObservableList<CarouselData> list) {
        return list == null || list.size() == 0;
    }

    private void initTopPane() {
        // 设置 左右按钮的位置
        setLocationArrow();
        // 设置左右按钮被点击时候的响应
        setArrowAction();
        // 设置 导航的位置
        setLocationNavBar();
        // 初始化导航
        initNavBar();
    }

    public void showPreviousNode() {
        if (dataList.size() <= 1) {
            return;
        }
        nextIndex = (getTgBtnIndex(btnGroup.getSelectedToggle()) - 1) % dataList.size();
        if (nextIndex < 0) {
            nextIndex = dataList.size() - 1;
        }
        btnGroup.getToggles().get(nextIndex).setSelected(true);
    }

    public void showNextNode() {
        if (dataList.size() <= 1) {
            return;
        }
        nextIndex = ((int) getTgBtnIndex(btnGroup.getSelectedToggle()) + 1) % dataList.size();
        btnGroup.getToggles().get(nextIndex).setSelected(true);
    }

    private void setArrowAction() {
        // 点击 左键 显示前一页
        leftButton.setOnMouseClicked(e -> showPreviousNode());

        // 点击 右键 显示后一页
        rightButton.setOnMouseClicked(e -> showNextNode());
    }

    // 设置左右按键的位置
    private void setLocationArrow() {
        DoubleBinding y1 = Bindings.createDoubleBinding(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return contentPane.heightProperty().subtract(leftButton.heightProperty()).divide(2.0).get();
            }
        }, contentPane.heightProperty(), leftButton.heightProperty());
        leftButton.layoutYProperty().bind(y1);
        leftButton.setLayoutX(10);

        DoubleBinding y2 = Bindings.createDoubleBinding(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return contentPane.heightProperty().subtract(rightButton.heightProperty()).divide(2.0).get();
            }
        }, contentPane.heightProperty(), rightButton.heightProperty());
        rightButton.layoutYProperty().bind(y2);

        DoubleBinding w2 = Bindings.createDoubleBinding(new Callable<Double>() {

            @Override
            public Double call() throws Exception {
                return contentPane.widthProperty().subtract(rightButton.widthProperty()).subtract(10).get();
            }
        }, contentPane.widthProperty(), rightButton.widthProperty());

        rightButton.layoutXProperty().bind(w2);

        arrowShowCon();
    }

    // 设置左右按键是否显示
    private void arrowShowCon() {
        DisplayMode displayMode = control.arrowDisplayModeProperty().get();

        if (displayMode == DisplayMode.HIDE) {
            control.hoverProperty().removeListener(arrowAutoShowListenter);
            leftButton.setVisible(false);
            rightButton.setVisible(false);
        } else if (displayMode == DisplayMode.SHOW) {
            control.hoverProperty().removeListener(arrowAutoShowListenter);
            leftButton.setOpacity(1.0);
            rightButton.setOpacity(1.0);
            leftButton.setVisible(true);
            rightButton.setVisible(true);
        } else if (displayMode == DisplayMode.AUTO) {
            leftButton.setVisible(false);
            rightButton.setVisible(false);
            control.hoverProperty().addListener(arrowAutoShowListenter);
        }
    }

    private ChangeListener<Boolean> navAutoShowListenter = (ob, ov, nv) -> {
        navBar.setVisible(true);
        if (tlNavShow != null) {
            tlNavShow.stop();
        }
        if (tlNavHide != null) {
            tlNavHide.stop();
        }
        if (control.navDisplayModeProperty().get() == DisplayMode.AUTO) {
            // 移入显示
            if (nv) {
                navBar.setOpacity(0);
                tlNavShow.getKeyFrames()
                        .setAll(new KeyFrame(DURATION_ARROW_SHOW, new KeyValue(navBar.opacityProperty(), 1.0)));
                tlNavShow.play();
            } else {// 移除隐藏
                navBar.setOpacity(1);
                tlNavHide.getKeyFrames()
                        .setAll(new KeyFrame(DURATION_ARROW_SHOW, new KeyValue(navBar.opacityProperty(), 0)));
                tlNavHide.setOnFinished(ex -> {
                    navBar.setVisible(false);
                });
                tlNavHide.play();
            }
        }
    };

    private ChangeListener<Boolean> arrowAutoShowListenter = (ob, ov, nv) -> {
        leftButton.setVisible(true);
        rightButton.setVisible(true);

        if (tlArrowShow != null) {
            tlArrowShow.stop();
        }
        if (tlArrowHide != null) {
            tlArrowHide.stop();
        }
        if (control.arrowDisplayModeProperty().get() == DisplayMode.AUTO) {
            // 移入显示
            if (nv) {
                leftButton.setOpacity(0);
                rightButton.setOpacity(0);
                KeyValue kv2 = new KeyValue(leftButton.opacityProperty(), 1.0);
                KeyValue kv4 = new KeyValue(rightButton.opacityProperty(), 1.0);
                KeyFrame kf2 = new KeyFrame(DURATION_ARROW_SHOW, kv2);
                KeyFrame kf4 = new KeyFrame(DURATION_ARROW_SHOW, kv4);
                tlArrowShow.getKeyFrames().clear();
                tlArrowShow.getKeyFrames().addAll(kf2, kf4);

                tlArrowShow.play();
            } else {// 移除隐藏
                leftButton.setOpacity(1);
                rightButton.setOpacity(1);
                KeyValue kv2 = new KeyValue(leftButton.opacityProperty(), 0);
                KeyValue kv4 = new KeyValue(rightButton.opacityProperty(), 0);
                KeyFrame kf2 = new KeyFrame(DURATION_ARROW_SHOW, kv2);
                KeyFrame kf4 = new KeyFrame(DURATION_ARROW_SHOW, kv4);
                tlArrowHide.getKeyFrames().clear();
                tlArrowHide.getKeyFrames().addAll(kf2, kf4);
                tlArrowHide.setOnFinished(ex -> {
                    leftButton.setVisible(false);
                    rightButton.setVisible(false);
                });
                tlArrowHide.play();
            }
        }
    };

    // 设置导航栏的位置
    private void setLocationNavBar() {
        // 先解除绑定
        navBar.prefWidthProperty().unbind();
        navBar.prefHeightProperty().unbind();
        navBar.layoutYProperty().unbind();
        navBar.layoutXProperty().unbind();
        navBar.setLayoutX(0);
        navBar.setLayoutY(0);
        // navBar.setAlignment(Pos.CENTER);
        // 绑定navBar的位置
        if (control.navOrientationProperty().get() == Orientation.HORIZONTAL) {
            navBar.getStyleClass().setAll("nav-bar", "nav-bar-horizontal");
            navBar.setOrientation(Orientation.HORIZONTAL);
            navBar.prefWidthProperty().bind(topPane.widthProperty());
            navBar.setPrefHeight(30);
            navBar.setLayoutX(0);
            navBar.layoutYProperty().bind(topPane.heightProperty().subtract(navBar.heightProperty()));
        } else {
            navBar.getStyleClass().setAll("nav-bar", "nav-bar-vertical");
            navBar.setOrientation(Orientation.VERTICAL);
            navBar.prefHeightProperty().bind(topPane.heightProperty());
            navBar.setPrefWidth(100);
            navBar.layoutXProperty().bind(topPane.widthProperty().subtract(navBar.widthProperty()));
            navBar.setLayoutY(0);
        }
        // 导航显示
        showNavBarCon();
    }

    // 控制导航是否显示
    private void showNavBarCon() {

        DisplayMode mode = control.navDisplayModeProperty().get();
        if (mode == DisplayMode.SHOW) {
            control.hoverProperty().removeListener(navAutoShowListenter);
            navBar.setOpacity(1.0);
            navBar.setVisible(true);
        } else if (mode == DisplayMode.HIDE) {
            control.hoverProperty().removeListener(navAutoShowListenter);
            navBar.setVisible(false);
        } else {
            navBar.setVisible(false);
            control.hoverProperty().addListener(navAutoShowListenter);
        }
    }

    // 裁剪容器为指定的范围
    private void clipRegion(Region region) {
        Rectangle rectClip = new Rectangle();
        region.setClip(rectClip);
        rectClip.widthProperty().bind(region.widthProperty());
        rectClip.heightProperty().bind(region.heightProperty());
    }

    // 设置选择的项
    public void setSelectedItem(int index) {
        if (dataList != null || btnGroup != null || index >= 0 && index < dataList.size()) {
            btnGroup.getToggles().get(index).setSelected(true);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {

        layoutInArea(contentPane, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
                                      double leftInset) {
        return rightInset + leftInset
                + (control.prefWidthProperty().get() == -1 ? 300 : control.prefWidthProperty().get());
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
                                       double leftInset) {
        return topInset + bottomInset
                + (control.prefHeightProperty().get() == -1 ? 200 : control.prefHeightProperty().get());
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset,
                                      double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset,
                                     double leftInset) {

        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    public void dispose() {
        // 移除Listener
        control.widthProperty().removeListener(sizeListener);
        control.heightProperty().removeListener(sizeListener);
        control.navOrientationProperty().removeListener(navDirListener);
        control.arrowDisplayModeProperty().removeListener(arrowDisplayListener);
        control.navDisplayModeProperty().removeListener(showNavListener);
        control.dataListProperty().removeListener(dataListListener);
        control.scaleTypeProperty().removeListener(scaleTypeListener);
        control.autoSwitchProperty().removeListener(autoPlayListener);
        control.displayTimeProperty().removeListener(timeChangeListener);
        control.transitionTimeProperty().removeListener(timeChangeListener);
        control.hoverProperty().removeListener(hoverPasueListener);
        control.hoverProperty().removeListener(arrowAutoShowListenter);
        control.hoverProperty().removeListener(navAutoShowListenter);
        // 结束动画
        stopAutoAnimation();
        stopSwitchAnimation();
        // 清空组件
        getChildren().clear();
        // 调用父类的dispose
        super.dispose();
    }
}
