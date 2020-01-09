package rx.controls;

import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import rx.skins.RXSVGViewSkin;

public class RXSVGView extends Control {
    private static final String DEFAULT_STYLE_CLASS = "rx-svg-view";

    private StringProperty content;

    public RXSVGView(){
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }
    public RXSVGView(String content){
        this();
        setContent(content);
    }

    public final void setContent(String value) {
        contentProperty().set(value);
    }

    public final String getContent() {
        return content == null ? "" : content.get();
    }

    public final StringProperty contentProperty() {
        if (content == null) {
            content = new StringPropertyBase("") {
                @Override
                public Object getBean() {
                    return RXSVGView.this;
                }
                @Override
                public String getName() {
                    return "content";
                }
            };
        }
        return content;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RXSVGViewSkin(this);
    }
}
