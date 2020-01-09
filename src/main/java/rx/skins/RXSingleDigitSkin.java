package rx.skins;

import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.SkinBase;
import javafx.scene.shape.Polygon;
import rx.controls.RXSingleDigit;

public class RXSingleDigitSkin extends SkinBase<RXSingleDigit> {

    private static final boolean[][] DIGIT_BOOLEAN = new boolean[][]{
            new boolean[]{true, false, true, true, true, true, true},//0
            new boolean[]{false, false, false, false, true, false, true},//1
            new boolean[]{true, true, true, false, true, true, false},
            new boolean[]{true, true, true, false, true, false, true},
            new boolean[]{false, true, false, true, true, false, true},
            new boolean[]{true, true, true, true, false, false, true},
            new boolean[]{true, true, true, true, false, true, true},
            new boolean[]{true, false, false, false, true, false, true},
            new boolean[]{true, true, true, true, true, true, true},
            new boolean[]{true, true, true, true, true, false, true}};
    private boolean flag;
    private Polygon[] polygons;
    private Group digitShape = new Group();
    //尺寸改变监听器
    private InvalidationListener sizeListener = ob -> flag = true;
    //其他改变监听器
    private InvalidationListener otherListener = ob -> {
        if (digitShape != null) {
            double w = getSkinnable().widthProperty().get();
            double h = getSkinnable().heightProperty().get();
            updateShape(w, h);
            getSkinnable().requestLayout();
        }
    };

    public RXSingleDigitSkin(RXSingleDigit control) {
        super(control);
        getChildren().add(digitShape);
        //尺寸改变监听
        control.widthProperty().addListener(sizeListener);
        control.heightProperty().addListener(sizeListener);
        //颜色改变监听
        control.lightFillProperty().addListener(otherListener);
        control.darkFillProperty().addListener(otherListener);
        //数字改变监听
        control.digitProperty().addListener(otherListener);
        //默认数值从-1修改成1, 触发 数字改变监听器,可以确保打开SceneBuilder时的默认显示状态
        //control.digitProperty().set(0);
    }

    private void updateShape(double w, double h) {

        polygons = new Polygon[]{
                new Polygon(w * 0.037037, h * 0, w * 0.962963, h * 0, w * 0.777778, h * 0.092593, w * 0.222222,
                        h * 0.092593),
                new Polygon(w * 0.222222, h * 0.453704, w * 0.777778, h * 0.453704, w * 0.962963, h * 0.5, w * 0.777778,
                        h * 0.546296, w * 0.222222, h * 0.546296, w * 0.037037, h * 0.5),
                new Polygon(w * 0.222222, h * 0.907407, w * 0.777778, h * 0.907407, w * 0.962963, h * 1, w * 0.037037,
                        h * 1),
                new Polygon(w * 0, h * 0.018519, w * 0.185185, h * 0.111111, w * 0.185185, h * 0.435185, w * 0,
                        h * 0.481481),
                new Polygon(w * 0.814815, h * 0.111111, w * 1, h * 0.018519, w * 1, h * 0.481481, w * 0.814815,
                        h * 0.435185),
                new Polygon(w * 0, h * 0.518519, w * 0.185185, h * 0.564815, w * 0.185185, h * 0.888889, w * 0,
                        h * 0.981481),
                new Polygon(w * 0.814815, h * 0.564815, w * 1, h * 0.518519, w * 1, h * 0.981481, w * 0.814815,
                        h * 0.888889)};

        digitShape.getChildren().setAll(polygons);// 注意这里是setAll ,不是addAll
        for (int i = 0; i < polygons.length; i++) {
            Polygon temp = polygons[i];
            temp.setSmooth(true);
            temp.setFill(DIGIT_BOOLEAN[getSkinnable().getDigit()][i]
                    ? getSkinnable().lightFillProperty().get() : getSkinnable().darkFillProperty().get());
        }

    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        if (flag) {
            updateShape(w, h);
            flag = false;
        }
        layoutInArea(digitShape, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
                                      double leftInset) {
        return rightInset + leftInset + 50;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
                                       double leftInset) {
        return topInset + bottomInset + 100;
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

        getSkinnable().widthProperty().removeListener(sizeListener);
        getSkinnable().heightProperty().removeListener(sizeListener);
        getSkinnable().digitProperty().removeListener(otherListener);
        getSkinnable().lightFillProperty().removeListener(otherListener);
        getSkinnable().darkFillProperty().removeListener(otherListener);
        getChildren().clear();//清空
        super.dispose();
    }
}
