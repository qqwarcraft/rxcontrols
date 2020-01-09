package rx.controls;

import com.sun.javafx.css.converters.EnumConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import rx.enums.DisplayMode;
import rx.event.RXActionEvent;
import rx.skins.RXTextFieldSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RXTextFieldBase extends TextField {
    private StyleableObjectProperty<DisplayMode> buttonDisplayMode;

    // onClickButtonProperty
    private final ObjectProperty<EventHandler<RXActionEvent>> onClickButtonProperty =
            new ObjectPropertyBase<EventHandler<RXActionEvent>>() {

                @Override
                protected void invalidated() {
                    setEventHandler(RXActionEvent.RXACTION, get());
                }

                @Override
                public Object getBean() {
                    return RXTextFieldBase.this;
                }

                @Override
                public String getName() {
                    return "onAction";

                }
            };

    protected  ObjectProperty<EventHandler<RXActionEvent>> onClickButtonProperty() {
        return onClickButtonProperty;
    }

    protected  EventHandler<RXActionEvent> getOnClickButton() {
        return onClickButtonProperty.get();
    }

    protected  void setOnClickButton(EventHandler<RXActionEvent> value) {
        onClickButtonProperty.set(value);
    }

    public RXTextFieldBase() {
    }

    public RXTextFieldBase(String text) {
        super(text);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RXTextFieldSkin(this);
    }

    private static class StyleableProperties {
        // 按钮显示
        private static final CssMetaData<RXTextFieldBase, DisplayMode> BUTTON_DISPLAY_MODE = new CssMetaData<RXTextFieldBase, DisplayMode>(
                "-rx-button-display", new EnumConverter<DisplayMode>(DisplayMode.class), DisplayMode.AUTO) {
            @Override
            public boolean isSettable(RXTextFieldBase control) {
                return control.buttonDisplayMode == null || !control.buttonDisplayMode.isBound();
            }

            @Override
            public StyleableProperty<DisplayMode> getStyleableProperty(RXTextFieldBase control) {
                return control.buttonDisplayModeProperty();
            }
        };

        // 创建一个CSS样式的表
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, BUTTON_DISPLAY_MODE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }

    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    // 这个方法写了,可以在SceneBuilder的Style里出现等提示
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return RXTextFieldBase.StyleableProperties.STYLEABLES;
    }

    public final StyleableObjectProperty<DisplayMode> buttonDisplayModeProperty() {
        if (buttonDisplayMode == null) {
            buttonDisplayMode = new StyleableObjectProperty<DisplayMode>(DisplayMode.AUTO) {

                @Override
                public CssMetaData<? extends Styleable, DisplayMode> getCssMetaData() {
                    return RXTextFieldBase.StyleableProperties.BUTTON_DISPLAY_MODE;
                }

                @Override
                public Object getBean() {
                    return RXTextFieldBase.this;
                }

                @Override
                public String getName() {
                    return "buttonDisplayMode";
                }
            };
        }
        return this.buttonDisplayMode;
    }

    public final DisplayMode getButtonDisplayMode() {
        return this.buttonDisplayModeProperty().get();
    }

    public final void setButtonDisplayMode(final DisplayMode buttonDisplayMode) {
        this.buttonDisplayModeProperty().set(buttonDisplayMode);
    }
}
