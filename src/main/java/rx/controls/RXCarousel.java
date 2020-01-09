package rx.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.EnumConverter;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import rx.enums.DisplayMode;
import rx.enums.ScaleType;
import rx.enums.TransitionType;
import rx.skins.RXCarouselSkin;
import rx.utils.RXResources;

public class RXCarousel extends Control {
    private static final String DEFAULT_STYLE_CLASS = "rx-carousel";// 添加css的类名
    private static final String USER_AGENT_STYLESHEET = RXResources.load("/rx-controls.css")
            .toExternalForm();

    private SimpleListProperty<CarouselData> dataList;// 需要显示的项目
    private StyleableObjectProperty<Duration> transitionTime;// 过渡动画的时间
    private StyleableObjectProperty<Duration> displayTime;// 每页显示的时间
    private StyleableBooleanProperty autoSwitch;// 是否自动切换页面
    private StyleableObjectProperty<TransitionType> transitionType;// 过渡动画的显示方法
    private StyleableObjectProperty<Orientation> navOrientation;// 导航按钮的方向
    private StyleableObjectProperty<ScaleType> scaleType;// 缩放类型
    private StyleableObjectProperty<DisplayMode> arrowDisplayMode;// 箭头显示方式
    private StyleableObjectProperty<DisplayMode> navDisplayMode;// 导航是否可见
    private StyleableBooleanProperty hoverPause;// 移入是否暂停
    private ReadOnlyIntegerWrapper ow;// 只读属性包装类
    private ReadOnlyIntegerProperty selectedIndex;// 只读属性(已经选中的项)
    private RXCarouselSkin skin;

    @Override
    protected Skin<?> createDefaultSkin() {
        skin = new RXCarouselSkin(this, ow);
        return skin;
    }

    @Override
    public String getUserAgentStylesheet() {
        return USER_AGENT_STYLESHEET;
    }

    public RXCarousel() {
        setPrefSize(300, 200);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        ow = new ReadOnlyIntegerWrapper(-1);
        dataList = new SimpleListProperty<CarouselData>(this, "dataList", FXCollections.observableArrayList());
    }

    public RXCarousel(SimpleListProperty<CarouselData> nodes) {
        this();
        setDataList(nodes);
    }

    public RXCarousel(ObservableList<CarouselData> nodes) {
        this(new SimpleListProperty<CarouselData>(nodes));
    }

    public final StyleableObjectProperty<Duration> transitionTimeProperty() {
        if (transitionTime == null) {
            transitionTime = new SimpleStyleableObjectProperty<>(StyleableProperties.TRANSITION_TIME, this,
                    "transitionTime", Duration.millis(600));
        }
        return this.transitionTime;
    }

    public final Duration getTransitionTime() {
        return this.transitionTimeProperty().get();
    }

    public final void setTransitionTime(final Duration transitionTime) {
        this.transitionTimeProperty().set(transitionTime);
    }

    public final StyleableObjectProperty<Duration> displayTimeProperty() {
        if (displayTime == null) {
            displayTime = new SimpleStyleableObjectProperty<>(StyleableProperties.DISPLAY_TIME, this, "displayTime",
                    Duration.millis(3500));
        }
        return this.displayTime;
    }

    public final Duration getDisplayTime() {
        return this.displayTimeProperty().get();
    }

    public final void setDisplayTime(final Duration displayTime) {
        this.displayTimeProperty().set(displayTime);
    }

    public final StyleableBooleanProperty autoSwitchProperty() {
        if (autoSwitch == null) {
            autoSwitch = new SimpleStyleableBooleanProperty(StyleableProperties.AUTO_SWITCH, RXCarousel.this,
                    "autoSwitch", true);
        }
        return this.autoSwitch;
    }

    public final boolean isAutoSwitch() {
        return this.autoSwitchProperty().get();
    }

    public final void setAutoSwitch(final boolean autoSwitch) {
        this.autoSwitchProperty().set(autoSwitch);
    }

    public final StyleableObjectProperty<TransitionType> transitionTypeProperty() {
        if (transitionType == null) {
            transitionType = new StyleableObjectProperty<TransitionType>(TransitionType.MOVE_TO_LEFT) {

                @Override
                public CssMetaData<? extends Styleable, TransitionType> getCssMetaData() {
                    return StyleableProperties.TRANSITION_TYPE;
                }

                @Override
                public Object getBean() {
                    return RXCarousel.this;
                }

                @Override
                public String getName() {
                    return "transitionType";
                }

            };
        }
        return this.transitionType;
    }

    public final TransitionType getTransitionType() {
        return this.transitionTypeProperty().get();
    }

    public final void setTransitionType(final TransitionType transitionType) {
        this.transitionTypeProperty().set(transitionType);
    }

    // 样式
    private static class StyleableProperties {

        private static final CssMetaData<RXCarousel, Boolean> HOVER_PAUSE = new CssMetaData<RXCarousel, Boolean>(
                "-rx-hover-pause", BooleanConverter.getInstance(), true) {

            @Override
            public boolean isSettable(RXCarousel control) {
                return control.hoverPause == null || !control.hoverPause.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(RXCarousel control) {
                return control.hoverPauseProperty();
            }
        };

        // 显示导航栏navDisplayMode
        private static final CssMetaData<RXCarousel, DisplayMode> NAV_DISPLAY_MODE = new CssMetaData<RXCarousel, DisplayMode>(
                "-rx-nav-display", new EnumConverter<DisplayMode>(DisplayMode.class), DisplayMode.SHOW) {
            @Override
            public boolean isSettable(RXCarousel control) {
                return control.navDisplayMode == null || !control.navDisplayMode.isBound();
            }

            @Override
            public StyleableProperty<DisplayMode> getStyleableProperty(RXCarousel control) {
                return control.navDisplayModeProperty();
            }
        };

        // 过渡动画类型 transitionType
        private static final CssMetaData<RXCarousel, TransitionType> TRANSITION_TYPE = new CssMetaData<RXCarousel, TransitionType>(
                "-rx-transition-type", new EnumConverter<TransitionType>(TransitionType.class),
                TransitionType.MOVE_TO_LEFT) {
            @Override
            public boolean isSettable(RXCarousel control) {
                return control.transitionType == null || !control.transitionType.isBound();
            }

            @Override
            public StyleableProperty<TransitionType> getStyleableProperty(RXCarousel control) {
                return control.transitionTypeProperty();
            }
        };

        // 导航条的位置 水平/垂直 navOrientation
        private static final CssMetaData<RXCarousel, Orientation> NAV_ORIENTATION = new CssMetaData<RXCarousel, Orientation>(
                "-rx-nav-orientation", new EnumConverter<Orientation>(Orientation.class), Orientation.HORIZONTAL) {
            @Override
            public boolean isSettable(RXCarousel control) {
                return control.navOrientation == null || !control.navOrientation.isBound();
            }

            @Override
            public StyleableProperty<Orientation> getStyleableProperty(RXCarousel control) {
                return control.navOrientationProperty();
            }
        };

        // 缩放类型css样式支持
        private static final CssMetaData<RXCarousel, ScaleType> SCALE_TYPE = new CssMetaData<RXCarousel, ScaleType>(
                "-rx-scale-type", new EnumConverter<ScaleType>(ScaleType.class), ScaleType.FIT_XY) {
            @Override
            public boolean isSettable(RXCarousel control) {
                return control.scaleType == null || !control.scaleType.isBound();
            }

            @Override
            public StyleableProperty<ScaleType> getStyleableProperty(RXCarousel control) {
                return control.scaleTypeProperty();
            }
        };

        // 箭头显示css样式支持
        private static final CssMetaData<RXCarousel, DisplayMode> ARROW_DISPLAY_MODE = new CssMetaData<RXCarousel, DisplayMode>(
                "-rx-arrow-display", new EnumConverter<DisplayMode>(DisplayMode.class), DisplayMode.AUTO) {
            @Override
            public boolean isSettable(RXCarousel control) {
                return control.arrowDisplayMode == null || !control.arrowDisplayMode.isBound();
            }

            @Override
            public StyleableProperty<DisplayMode> getStyleableProperty(RXCarousel control) {
                return control.arrowDisplayModeProperty();
            }
        };

        private static final CssMetaData<RXCarousel, Duration> TRANSITION_TIME = new CssMetaData<RXCarousel, Duration>(
                "-rx-transition-time", DurationConverter.getInstance(), Duration.millis(800)) {

            @Override
            public boolean isSettable(RXCarousel control) {
                return control.transitionTime == null || !control.transitionTime.isBound();
            }

            @Override
            public StyleableProperty<Duration> getStyleableProperty(RXCarousel control) {
                return control.transitionTimeProperty();
            }
        };
        private static final CssMetaData<RXCarousel, Duration> DISPLAY_TIME = new CssMetaData<RXCarousel, Duration>(
                "-rx-display-time", DurationConverter.getInstance(), Duration.millis(3500)) {

            @Override
            public boolean isSettable(RXCarousel control) {
                return control.displayTime == null || !control.displayTime.isBound();
            }

            @Override
            public StyleableProperty<Duration> getStyleableProperty(RXCarousel control) {
                return control.displayTimeProperty();
            }
        };
        private static final CssMetaData<RXCarousel, Boolean> AUTO_SWITCH = new CssMetaData<RXCarousel, Boolean>(
                "-rx-auto-switch", BooleanConverter.getInstance(), true) {

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(RXCarousel control) {
                return control.autoSwitchProperty();
            }

            @Override
            public boolean isSettable(RXCarousel control) {
                return control.autoSwitch == null || !control.autoSwitch.isBound();
            }
        };

        // 创建一个CSS样式的表
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, TRANSITION_TIME, DISPLAY_TIME, AUTO_SWITCH, ARROW_DISPLAY_MODE, SCALE_TYPE,
                    NAV_DISPLAY_MODE, NAV_ORIENTATION, TRANSITION_TYPE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    // 这个方法写了,可以在SceneBuilder的Style里出现等提示
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public final SimpleListProperty<CarouselData> dataListProperty() {
        return this.dataList;
    }

    public final ObservableList<CarouselData> getDataList() {
        return this.dataListProperty().get();
    }

    public final void setDataList(final SimpleListProperty<CarouselData> dataList) {
        this.dataListProperty().set(dataList);
    }

    public final void setDataList(final ObservableList<CarouselData> dataList) {
        this.dataListProperty().set(dataList);
    }

    public final StyleableObjectProperty<Orientation> navOrientationProperty() {
        if (navOrientation == null) {
            navOrientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) {

                @Override
                public CssMetaData<? extends Styleable, Orientation> getCssMetaData() {
                    return StyleableProperties.NAV_ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return RXCarousel.this;
                }

                @Override
                public String getName() {
                    return "navOrientation";
                }
            };
        }

        return this.navOrientation;
    }

    public final Orientation getNavOrientation() {
        return this.navOrientationProperty().get();
    }

    public final void setNavOrientation(final Orientation navOrientation) {
        this.navOrientationProperty().set(navOrientation);
    }

    public final StyleableObjectProperty<ScaleType> scaleTypeProperty() {
        if (scaleType == null) {
            scaleType = new StyleableObjectProperty<ScaleType>(ScaleType.FIT_XY) {

                @Override
                public CssMetaData<? extends Styleable, ScaleType> getCssMetaData() {
                    return StyleableProperties.SCALE_TYPE;
                }

                @Override
                public Object getBean() {
                    return RXCarousel.this;
                }

                @Override
                public String getName() {
                    return "scaleType";
                }
            };
        }
        return this.scaleType;
    }

    public final ScaleType getScaleType() {
        return this.scaleTypeProperty().get();
    }

    public final void setScaleType(final ScaleType scaleType) {
        this.scaleTypeProperty().set(scaleType);
    }

    public final StyleableObjectProperty<DisplayMode> arrowDisplayModeProperty() {
        if (arrowDisplayMode == null) {
            arrowDisplayMode = new StyleableObjectProperty<DisplayMode>(DisplayMode.AUTO) {

                @Override
                public CssMetaData<? extends Styleable, DisplayMode> getCssMetaData() {
                    return StyleableProperties.ARROW_DISPLAY_MODE;
                }

                @Override
                public Object getBean() {
                    return RXCarousel.this;
                }

                @Override
                public String getName() {
                    return "arrowDisplayMode";
                }
            };
        }

        return this.arrowDisplayMode;
    }

    public final DisplayMode getArrowDisplayMode() {
        return this.arrowDisplayModeProperty().get();
    }

    public final void setArrowDisplayMode(final DisplayMode arrowDisplayMode) {
        this.arrowDisplayModeProperty().set(arrowDisplayMode);
    }

    public final StyleableObjectProperty<DisplayMode> navDisplayModeProperty() {
        if (navDisplayMode == null) {
            navDisplayMode = new StyleableObjectProperty<DisplayMode>(DisplayMode.SHOW) {

                @Override
                public CssMetaData<? extends Styleable, DisplayMode> getCssMetaData() {
                    return StyleableProperties.NAV_DISPLAY_MODE;
                }

                @Override
                public Object getBean() {
                    return RXCarousel.this;
                }

                @Override
                public String getName() {
                    return "navDisplayMode";
                }
            };
        }
        return this.navDisplayMode;
    }

    public final DisplayMode getNavDisplayMode() {
        return this.navDisplayModeProperty().get();
    }

    public final void setNavDisplayMode(final DisplayMode navDisplayMode) {
        this.navDisplayModeProperty().set(navDisplayMode);
    }

    public final StyleableBooleanProperty hoverPauseProperty() {
        if (hoverPause == null) {
            hoverPause = new SimpleStyleableBooleanProperty(StyleableProperties.HOVER_PAUSE, RXCarousel.this,
                    "hoverPause", true);
        }
        return this.hoverPause;
    }

    public final boolean isHoverPause() {
        return this.hoverPauseProperty().get();
    }

    public final void setHoverPause(final boolean hoverPause) {
        this.hoverPauseProperty().set(hoverPause);
    }

    public final ReadOnlyIntegerProperty selectedIndexProperty() {
        if (selectedIndex == null) {
            selectedIndex = ow.getReadOnlyProperty();
        }
        return this.selectedIndex;
    }

    public final int getSelectedIndex() {
        return this.selectedIndexProperty().get();
    }

    // 设置选中的数据(注意这个方法和上面的selectedIndexProperty(只读属性) 无直接关系)
    // 所以 方法名添加了Item单词在中间
    public void setSelectedItemIndex(int index) {
        RXCarouselSkin skin = (RXCarouselSkin) getSkin();
        skin.setSelectedItem(index);
    }

    public void showNextNode() {
        RXCarouselSkin skin = (RXCarouselSkin) getSkin();
        skin.showNextNode();
    }

    public void showPreviousNode() {
        RXCarouselSkin skin = (RXCarouselSkin) getSkin();
        skin.showPreviousNode();
    }

    // 轮播数据
    public static final class CarouselData {
        private Node node; // 需要展示的节点
        private String title;// 该节点的标题(描述信息)

        public CarouselData(Node node, String title) {
            this.node = node;
            this.title = title;
        }

        public CarouselData(String imageUrl, String title) {
            this.node = new ImageView(imageUrl);
            this.title = title;
        }

        public CarouselData(ImageView imageView, String title) {
            this.node = imageView;
            this.title = title;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}
