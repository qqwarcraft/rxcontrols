package rx.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.StringConverter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;
import rx.enums.DisplayMode;
import rx.skins.RXPasswordFieldSkin;
import rx.utils.RXResources;

// 对PasswordField 进行了修改和补充
public class RXPasswordField extends PasswordField {
    private StyleableObjectProperty<DisplayMode> buttonDisplayMode;
    private static final String USER_AGENT_STYLESHEET = RXResources.load("/rx-controls.css")
            .toExternalForm();
    private static final String DEFAULT_STYLE_CLASS = "rx-password-field";
    // 获取皮肤(目的是为了对showPassword进行控制: maskText 方法里需要使用showPasswordProperty,
    // 如果把showPasswordProperty给Control , 那么maskText 会抛出空指针,因为刚开始的时候Control为空
    protected RXPasswordFieldSkin skin;

    public final SimpleBooleanProperty showPasswordProperty() {
        return skin.showPasswordProperty();
    }

    public final boolean isShowPassword() {
        return skin.showPasswordProperty().get();
    }

    public final void setShowPassword(final boolean showPassword) {
        skin.showPasswordProperty().set(showPassword);
    }

    public RXPasswordField() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        skin = new RXPasswordFieldSkin(this);
        setAccessibleRole(AccessibleRole.PASSWORD_FIELD);
        showingProperty().addListener(
                (observable, oldValue, newValue) -> pseudoClassStateChanged(SHOWING_PSEUDO_CLASS, newValue));
    }

    @Override
    public String getUserAgentStylesheet() {
        return USER_AGENT_STYLESHEET;
    }

    public RXPasswordField(String txt) {
        this();
        setText(txt);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return skin;
    }

    // 禁止对密码进行 剪切和 复制
    @Override
    public void cut() {
    }

    @Override
    public void copy() {
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT:
                return null;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public final SimpleStyleableStringProperty echocharProperty() {
        return skin.echocharProperty();
    }

    public final String getEchochar() {
        return skin.echocharProperty().get();
    }

    public final void setEchochar(final String echochar) {
        skin.echocharProperty().set(echochar);
    }

    public final StyleableObjectProperty<DisplayMode> buttonDisplayModeProperty() {
        if (buttonDisplayMode == null) {
            buttonDisplayMode = new StyleableObjectProperty<DisplayMode>(DisplayMode.AUTO) {

                @Override
                public CssMetaData<? extends Styleable, DisplayMode> getCssMetaData() {
                    return StyleableProperties.BUTTON_DISPLAY_MODE;
                }

                @Override
                public Object getBean() {
                    return RXPasswordField.this;
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

    // 样式
    public static class StyleableProperties {
        public static final CssMetaData<RXPasswordField, String> ENCHOCHAR = new CssMetaData<RXPasswordField, String>(
                "-rx-echochar", StringConverter.getInstance(), String.valueOf(RXPasswordFieldSkin.BULLET)) {

            @Override
            public StyleableProperty<String> getStyleableProperty(RXPasswordField control) {
                return control.echocharProperty();
            }

            @Override
            public boolean isSettable(RXPasswordField control) {
                return control.echocharProperty() == null || !control.echocharProperty().isBound();
            }
        };
        // 按钮显示
        private static final CssMetaData<RXPasswordField, DisplayMode> BUTTON_DISPLAY_MODE = new CssMetaData<RXPasswordField, DisplayMode>(
                "-rx-button-display", new EnumConverter<DisplayMode>(DisplayMode.class), DisplayMode.AUTO) {
            @Override
            public boolean isSettable(RXPasswordField control) {
                return control.buttonDisplayMode == null || !control.buttonDisplayMode.isBound();
            }

            @Override
            public StyleableProperty<DisplayMode> getStyleableProperty(RXPasswordField control) {
                return control.buttonDisplayModeProperty();
            }
        };

        // 创建一个CSS样式的表
        protected static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, ENCHOCHAR, BUTTON_DISPLAY_MODE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    // 这个方法写了,可以在SceneBuilder的Style里出现等提示
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    private static final PseudoClass SHOWING_PSEUDO_CLASS = PseudoClass.getPseudoClass("showing");

    private BooleanProperty showing;

    public final void setShowing(boolean showing) {
        showingProperty().set(showing);
    }

    public final boolean isShowing() {
        return showing == null ? false : showing.get();
    }

    public final BooleanProperty showingProperty() {
        if (showing == null) {
            showing = new SimpleBooleanProperty(false);
        }
        return showing;
    }
}
