package rx.skins;

import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import rx.controls.RXHighlightLabel;
import rx.utils.StringUtil;

import java.util.ArrayList;

public class RXHighlightLabelSkin extends SkinBase<RXHighlightLabel> {
    RXHighlightLabel control;
    Pane textFlow;
    InvalidationListener invalidListener = ob -> {
        fillPane(control.getText(), control.getKeywords(), control.isIgnoreCase(), textFlow);
    };

    public RXHighlightLabelSkin(RXHighlightLabel control) {
        super(control);
        this.control = control;
        textFlow = new TextFlow();
//        textFlow.setPadding(new Insets(0));
        String keywords = control.getKeywords();
        String text = control.getText();
        boolean ignoreCase = control.isIgnoreCase();
        getChildren().setAll(textFlow);
        fillPane(text, keywords, ignoreCase, textFlow);
        control.ignoreCaseProperty().addListener(invalidListener);
        control.textProperty().addListener(invalidListener);
        control.keywordsProperty().addListener(invalidListener);
    }

    private boolean match;

    public boolean isMatch() {
        return match;
    }

    private void fillPane(String text, String keywords, boolean ignoreCase, Pane textFlow) {
        textFlow.getChildren().clear();//首先清空
        ArrayList<Pair<String, Boolean>> list = StringUtil.parseText(text, keywords, ignoreCase);
        ArrayList<Node> nodeList = new ArrayList<>();
        list.forEach(pair -> {
            if (pair.getValue()) {
                match = true;
                Label node = new Label(pair.getKey());
               // node.setStyle("-fx-text-fill: #ff381a;-fx-background-color: #fff96b");
             //   node.setStyle("-fx-fill: #ff0000;-fx-background-color: #faffc5");
                node.getStyleClass().add("highlight-label");//高亮的文本
                nodeList.add(node);
            } else {
                Text node = new Text(pair.getKey());
                node = new Text(pair.getKey());
                //node.setStyle("-fx-text-fill: black");
                node.getStyleClass().add("plain-text");//普通文本
                nodeList.add(node);
            }
        });
        textFlow.getChildren().setAll(nodeList);
    }

    @Override
    protected void layoutChildren(final double x, final double y, final double w, final double h) {
        layoutInArea(textFlow, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
    }


    @Override
    public void dispose() {
        control.ignoreCaseProperty().removeListener(invalidListener);
        control.textProperty().removeListener(invalidListener);
        control.keywordsProperty().removeListener(invalidListener);
        if (textFlow!=null) {
            textFlow.getChildren().clear();
        }
        getChildren().clear();
        super.dispose();
    }
}
