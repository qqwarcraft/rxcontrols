package rx.controls;

import com.sun.javafx.css.converters.EnumConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import rx.enums.ShapeType;
import rx.skins.RXAvatarSkin;
import rx.utils.RXResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RXAvatar extends Control {
    private static final String DEFAULT_STYLE_CLASS = "rx-avatar";
    private static final String USER_AGENT_STYLESHEET = RXResources.load("/rx-controls.css").toExternalForm();

    private ObjectProperty<Image> image;
    private StyleableObjectProperty<ShapeType> shapeType;
    private SimpleDoubleProperty arcWidth;
    private SimpleDoubleProperty arcHeight;

    public RXAvatar() {
        setPrefSize(100, 100);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public RXAvatar(String imageUrl) {
        this(new Image(imageUrl, true));
    }

    public RXAvatar(Image image) {
        this();
        setImage(image);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RXAvatarSkin(this);
    }

    public String getUserAgentStylesheet() {
        return USER_AGENT_STYLESHEET;
    }

    public final ObjectProperty<Image> imageProperty() {
        if (image == null) {
            image = new SimpleObjectProperty<Image>(this, "image");
        }
        return this.image;
    }

    public final Image getImage() {
        return this.imageProperty().get();
    }

    public final void setImage(final Image image) {
        this.imageProperty().set(image);
    }

    public final StyleableObjectProperty<ShapeType> shapeTypeProperty() {
        if (shapeType == null) {
            shapeType = new StyleableObjectProperty<ShapeType>(ShapeType.CIRCLE) {
                @Override
                public CssMetaData<? extends Styleable, ShapeType> getCssMetaData() {
                    return StyleableProperties.SHAPE_TYPE;
                }

                @Override
                public Object getBean() {
                    return RXAvatar.this;
                }

                @Override
                public String getName() {
                    return "shapeType";
                }
            };
        }

        return this.shapeType;
    }

    public final ShapeType getShapeType() {
        return this.shapeTypeProperty().get();
    }

    public final void setShapeType(final ShapeType shapeType) {
        this.shapeTypeProperty().set(shapeType);
    }

    // 样式
    private static class StyleableProperties {
        private static final CssMetaData<RXAvatar, ShapeType> SHAPE_TYPE = new CssMetaData<RXAvatar, ShapeType>(
                "-rx-shape-type", new EnumConverter<ShapeType>(ShapeType.class), ShapeType.CIRCLE) {
            @Override
            public boolean isSettable(RXAvatar control) {
                return control.shapeType == null || !control.shapeType.isBound();
            }

            @Override
            public StyleableProperty<ShapeType> getStyleableProperty(RXAvatar control) {
                return control.shapeTypeProperty();
            }
        };

        // 创建一个CSS样式的表
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, SHAPE_TYPE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    // 这个方法写了,可以在SceneBuilder的Style里出现提示
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public final SimpleDoubleProperty arcWidthProperty() {
        if (arcWidth == null) {
            arcWidth = new SimpleDoubleProperty(0);
        }
        return this.arcWidth;
    }

    public final double getArcWidth() {
        return this.arcWidthProperty().get();
    }

    public final void setArcWidth(final double arcWidth) {
        this.arcWidthProperty().set(arcWidth);
    }

    public final SimpleDoubleProperty arcHeightProperty() {
        if (arcHeight == null) {
            arcHeight = new SimpleDoubleProperty(0);
        }
        return this.arcHeight;
    }

    public final double getArcHeight() {
        return this.arcHeightProperty().get();
    }

    public final void setArcHeight(final double arcHeight) {
        this.arcHeightProperty().set(arcHeight);
    }

}
