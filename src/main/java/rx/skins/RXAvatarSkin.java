package rx.skins;

import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import rx.controls.RXAvatar;
import rx.enums.ShapeType;

public class RXAvatarSkin extends SkinBase<RXAvatar> {
    private Group rootPane;

    private ImageView imageView;
    private RXAvatar avatar;
    private InvalidationListener updateUIListener = (ob) -> clipImageView();

    private InvalidationListener arcSizeListener = (ob) -> {
        if (avatar.getShapeType() == ShapeType.SQUARE) {
            clipImageView();
        }
    };

    public RXAvatarSkin(RXAvatar avatar) {
        super(avatar);
        this.avatar = avatar;
        rootPane = new Group();
        imageView = new ImageView();
        imageView.imageProperty().bind(avatar.imageProperty());
        imageView.setSmooth(true);// 平滑
        imageView.setPreserveRatio(true);// 按比例缩放,改变w,那么同时改变h
        rootPane.getChildren().setAll(imageView);
        getChildren().setAll(rootPane);
        clipImageView();

        avatar.arcWidthProperty().addListener(arcSizeListener);
        avatar.arcHeightProperty().addListener(arcSizeListener);
        avatar.shapeTypeProperty().addListener(updateUIListener);
        avatar.widthProperty().addListener(updateUIListener);
        avatar.heightProperty().addListener(updateUIListener);
        avatar.prefWidthProperty().addListener(updateUIListener);
        avatar.prefHeightProperty().addListener(updateUIListener);
        avatar.imageProperty().addListener(updateUIListener);

    }

    private void clipImageView() {
        Image img = avatar.imageProperty().get();
        if (img == null) {
            return;
        }
        // !!恢复图形的fitHeight和fitWidth
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);

        double w = img.widthProperty().get();
        double h = img.heightProperty().get();
        double cx = 0, cy = 0;
        // 有的图片是竖图比如ds.jpg, 有的图片是横图pc.jpg
        // 为了找到圆心, 那么根据宽和高比较小的一个来计算缩放比例
        double conW = avatar.prefWidthProperty().get();
        double conH = avatar.prefHeightProperty().get();
        double r = Math.min(conW, conH) / 2;
        if (Double.compare(w, h) > 0) {
            imageView.setFitHeight(r * 2);// 设置合适的高度 ,那么宽度也自动修改了
            double scale = (r * 2) / img.getHeight();// 计算比例
            cx = w * scale / 2 + imageView.getLayoutX();// 计算出圆心
            cy = imageView.getFitHeight() / 2 + imageView.getLayoutY();
        } else {
            imageView.setFitWidth(r * 2);
            double scale = (r * 2) / img.getWidth();
            cx = imageView.getFitWidth() / 2 + imageView.getLayoutX();
            cy = h * scale / 2 + imageView.getLayoutY();
        }

        ShapeType shapeType = avatar.getShapeType();
        Shape clipShape = null;
        if (shapeType == ShapeType.CIRCLE) {
            clipShape = new Circle(cx, cy, r);
            //如果是正方形头像
        } else if (shapeType == ShapeType.SQUARE) {
            clipShape = new Rectangle(cx - r, cy - r, r * 2, r * 2);
            ((Rectangle) clipShape).arcWidthProperty().bind(avatar.arcWidthProperty());
            ((Rectangle) clipShape).arcHeightProperty().bind(avatar.arcHeightProperty());
            //如果是六边形头像HEXAGON_H
        } else if (shapeType == ShapeType.HEXAGON_H) {//
            double rw = 0;
            double rh = 0;
            double rate = 1.16;
            if (Double.compare(conW / conH, rate) > 0) {// 组件太宽
                rh = conH / 2;
                rw = rh * rate;
                if (w / h > rate) {// 宽图
                    imageView.setFitHeight(conH);
                } else {// 窄图.
                    imageView.setFitWidth(conH * rate);
                }
            } else {// 组件太窄
                rw = conW / 2;
                rh = rw / rate;
                if (w / h > rate) {// 宽图
                    imageView.setFitHeight(conW * rate);
                } else {// 窄图.
                    imageView.setFitWidth(conW);
                }
            }
            cx = (imageView.prefWidth(-1)) / 2;// 计算出圆心
            cy = (imageView.prefHeight(-1)) / 2;

            clipShape = new Polygon(cx - rw / 2, cy - rh, cx + rw / 2, cy - rh, cx + rw, cy, cx + rw / 2, cy + rh,
                    cx - rw / 2, cy + rh, cx - rw, cy);
        } else { //如果是六边形头像HEXAGON_V
            double rw = 0;
            double rh = 0;
            double rate = 0.871;
            if (Double.compare(conW / conH, rate) > 0) {// 组件太宽
                rh = conH / 2;
                rw = rh * rate;
                if (w / h > rate) {// 宽图
                    imageView.setFitHeight(rh * 2);
                } else {// 窄图.
                    imageView.setFitWidth(rw * 2);
                }
            } else {// 组件太窄
                rw = conW / 2;
                rh = rw / rate;
                if (w / h > rate) {// 宽图
                    imageView.setFitHeight(rh * 2);
                } else {// 窄图.
                    imageView.setFitWidth(rw * 2);
                }
            }
            cx = (imageView.prefWidth(-1)) / 2;// 计算出圆心
            cy = (imageView.prefHeight(-1)) / 2;

            clipShape = new Polygon(cx, cy - rh, cx + rw, cy - rh / 2, cx + rw, cy + rh / 2,
                    cx, cy + rh, cx - rw, cy + rh / 2, cx - rw, cy - rh / 2);
        }
        imageView.setClip(clipShape);
    }

    @Override
    protected void layoutChildren(final double x, final double y, final double w, final double h) {
        layoutInArea(rootPane, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
                                      double leftInset) {
        return rightInset + leftInset + 100;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
                                       double leftInset) {
        return topInset + bottomInset + 100;
    }


    @Override
    public void dispose() {
        avatar.arcWidthProperty().removeListener(arcSizeListener);
        avatar.arcHeightProperty().removeListener(arcSizeListener);
        avatar.shapeTypeProperty().removeListener(updateUIListener);
        avatar.widthProperty().removeListener(updateUIListener);
        avatar.heightProperty().removeListener(updateUIListener);
        avatar.prefWidthProperty().removeListener(updateUIListener);
        avatar.prefHeightProperty().removeListener(updateUIListener);
        avatar.imageProperty().removeListener(updateUIListener);
        getChildren().clear();
        super.dispose();
    }

}
