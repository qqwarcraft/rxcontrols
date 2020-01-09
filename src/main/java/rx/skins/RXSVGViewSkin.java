package rx.skins;

import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import rx.controls.RXSVGView;
import rx.bean.PathInfo;
import rx.utils.SvgUtil;

import java.util.ArrayList;

public class RXSVGViewSkin extends SkinBase<RXSVGView> {
    Pane pane;
    InvalidationListener updateListener = ob -> updateSVG();

    public RXSVGViewSkin(RXSVGView control) {
        super(control);
        pane = new Pane();
        pane.setId("sv-root-pane");
        updateSVG();
        getChildren().setAll(pane);
        control.contentProperty().addListener(updateListener);
    }

    private void updateSVG() {
        pane.getChildren().clear();//首先清空
        String content = getSkinnable().getContent();
        if (content.trim().isEmpty()) {
            return;
        }
        ArrayList<PathInfo> pathInfos = SvgUtil.parseSvg(content);
        for (PathInfo info : pathInfos) {
            SVGPath path = new SVGPath();
            path.setContent(info.getPathD());
            path.setFill(Color.valueOf(info.getPathFill()));
            path.setId(info.getPathId());
            pane.getChildren().add(path);
        }
    }

    @Override
    protected void layoutChildren(final double x, final double y, final double w, final double h) {
        layoutInArea(pane, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
    }

    @Override
    public void dispose() {
        getSkinnable().contentProperty().removeListener(updateListener);
        getChildren().clear();
        super.dispose();
    }
}
