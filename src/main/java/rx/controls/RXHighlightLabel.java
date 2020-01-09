package rx.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import rx.skins.RXHighlightLabelSkin;
import rx.utils.RXResources;

/**
 * 下次有升级思路: 添加 一个匹配规则的枚举类MatchRule,
 * 如果是 MatchRule.REG 就把关键字当正则表达式去匹配
 * 如果是MatchRule.STR 就把关键字当普通文字去匹配
 */
public class RXHighlightLabel extends Control {
    private static final String DEFAULT_STYLE_CLASS = "rx-highlight-label";
    private static final String USER_AGENT_STYLESHEET = RXResources.load("/rx-controls.css").toExternalForm();

    //构造器
    public RXHighlightLabel() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public RXHighlightLabel(String text) {
        this();
        textProperty().set(text);
    }

    public RXHighlightLabel(String text, String keywords) {
        this(text);
        keywordsProperty().set(keywords);
    }

    protected Skin<?> createDefaultSkin() {
        return new RXHighlightLabelSkin(this);
    }

    public String getUserAgentStylesheet() {
        return USER_AGENT_STYLESHEET;
    }

    //是否匹配上了
    public boolean isMatch(){
        return ((RXHighlightLabelSkin)this.getSkin()).isMatch();
    }

    // textProperty文本内容
    private final StringProperty text = new SimpleStringProperty(this, "");

    public final StringProperty textProperty() {
        return text;
    }

    public final String getText() {
        return text.get();
    }

    public final void setText(String value) {
        text.set(value);
    }

    // keywordsProperty 关键字
    private final StringProperty keywords = new SimpleStringProperty(this, "");

    public final StringProperty keywordsProperty() {
        return keywords;
    }

    public final String getKeywords() {
        return keywords.get();
    }

    public final void setKeywords(String value) {
        keywords.set(value);
    }

    // ignoreCaseProperty 忽略大小写 true忽略, false不忽略
    private final BooleanProperty ignoreCase = new SimpleBooleanProperty(this, "ignoreCase", true);

    public final BooleanProperty ignoreCaseProperty() {
        return ignoreCase;
    }

    public final boolean isIgnoreCase() {
        return ignoreCase.get();
    }

    public final void setIgnoreCase(boolean value) {
        ignoreCase.set(value);
    }


}
