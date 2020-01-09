package rx.skins;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import rx.controls.RXTextFieldBase;
import rx.enums.DisplayMode;
import rx.event.RXActionEvent;

public class RXTextFieldSkin extends TextFieldSkin {
    private Region region;
    private RXTextFieldBase textField;
    private StackPane btn;
    private ChangeListener<DisplayMode> displayModeChangeLi = (ob, ov, nv) -> {
        setButtonStatus(nv);
    };

    public RXTextFieldSkin(RXTextFieldBase textField) {
        super(textField);
        this.textField = textField;
        Pane topPane = new Pane();
        topPane.getStyleClass().add("tf-top-pane");
        btn = new StackPane();
        btn.getStyleClass().add("tf-button");
        region = new Region();
        region.getStyleClass().add("tf-button-shape");
        btn.getChildren().addAll(region);
        topPane.getChildren().addAll(btn);
        getChildren().addAll(topPane);
        btn.layoutXProperty().bind(topPane.widthProperty().subtract(btn.widthProperty()));
        btn.layoutYProperty().bind(topPane.heightProperty().subtract(btn.heightProperty()).divide(2));
        DisplayMode displayMode = textField.buttonDisplayModeProperty().get();

        setButtonStatus(displayMode);
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseClicked(e -> {
            getSkinnable().fireEvent(new RXActionEvent());
        });
        textField.buttonDisplayModeProperty().addListener(displayModeChangeLi);
    }

    private void setButtonStatus(DisplayMode displayMode) {
        if (displayMode == DisplayMode.SHOW) {
            btn.visibleProperty().unbind();
            btn.setVisible(true);
        } else if (displayMode == DisplayMode.HIDE) {
            btn.visibleProperty().unbind();
            btn.setVisible(false);
        } else if (displayMode == DisplayMode.AUTO) {
            // btn.setVisible(false);
            btn.visibleProperty().bind(textField.focusedProperty());
        }
    }

    @Override
    public void dispose() {
        textField.buttonDisplayModeProperty().removeListener(displayModeChangeLi);
        super.dispose();
    }
}
