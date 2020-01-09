package rx.controls;

import javafx.scene.control.Skin;
import rx.skins.RXTextFieldSkin;
import rx.utils.RXResources;

public class RXTextFieldCopy extends RXTextFieldBase{
    private static final String DEFAULT_STYLE_CLASS = "rx-text-field-copy";
    private static final String USER_AGENT_STYLESHEET = RXResources.load("/rx-controls.css")
            .toExternalForm();

    public RXTextFieldCopy() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setOnClickButton(e->{
            selectAll();
            copy();
        });
    }

    public RXTextFieldCopy(String text) {
        this();
        setText(text);
    }
    @Override
    protected Skin<?> createDefaultSkin() {
        return new RXTextFieldSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return USER_AGENT_STYLESHEET;
    }

}
