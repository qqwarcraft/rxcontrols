package rx.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.EventHandler;
import javafx.scene.control.Skin;
import rx.event.RXActionEvent;
import rx.skins.RXTextFieldSkin;
import rx.utils.RXResources;

public class RXTextField extends RXTextFieldBase {
    private static final String DEFAULT_STYLE_CLASS = "rx-text-field";
    private static final String USER_AGENT_STYLESHEET = RXResources.load("/rx-controls.css")
            .toExternalForm();

    // onClickButtonProperty
    private final ObjectProperty<EventHandler<RXActionEvent>> onClickButtonProperty =
            new ObjectPropertyBase<EventHandler<RXActionEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(RXActionEvent.RXACTION, get());
                }

                @Override
                public Object getBean() {
                    return RXTextField.this;
                }

                @Override
                public String getName() {
                    return "onAction";

                }
            };

    public final ObjectProperty<EventHandler<RXActionEvent>> onClickButtonProperty() {
        return onClickButtonProperty;
    }

    public final EventHandler<RXActionEvent> getOnClickButton() {
        return onClickButtonProperty.get();
    }

    public final void setOnClickButton(EventHandler<RXActionEvent> value) {
        onClickButtonProperty.set(value);
    }

    public RXTextField() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public RXTextField(String text) {
        super(text);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
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
