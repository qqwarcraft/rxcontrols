package rx.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.css.converters.PaintConverter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import rx.skins.RXSingleDigitSkin;

public class RXSingleDigit extends Control {
	private static final String DEFAULT_STYLE_CLASS = "rx-single-digit";

	// 填充色 (明亮部分)
	private StyleableObjectProperty<Paint> brightenPaint;
	// 填充色(灰暗部分)
	private StyleableObjectProperty<Paint> darkenPaint;
	// 数字
	private SimpleIntegerProperty digit;

	public RXSingleDigit() {
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		digit = new SimpleIntegerProperty(0);
	}

	public RXSingleDigit(int digitNum) {
		this();
		setDigit(digitNum);
	}
	// 设置皮肤
	@Override
	protected Skin<?> createDefaultSkin() {
		return new RXSingleDigitSkin(this);
	}

	public final StyleableObjectProperty<Paint> lightFillProperty() {
		if (brightenPaint == null) {
			brightenPaint = new SimpleStyleableObjectProperty<Paint>(StyleableProperties.BRIGHTEN_PAINT, RXSingleDigit.this,
					"brightenPaint", Color.BLACK);
		}
		return this.brightenPaint;
	}

	public final Paint getLightFill() {
		return this.lightFillProperty().get();
	}

	public final void setLightFill(final Paint lightFill) {
		this.lightFillProperty().set(lightFill);
	}

	public final StyleableObjectProperty<Paint> darkFillProperty() {
		if (darkenPaint == null) {
			darkenPaint = new SimpleStyleableObjectProperty<Paint>(StyleableProperties.DARKEN_PAINT, RXSingleDigit.this,
					"darkenPaint", Color.web("#dddddd"));
		}
		return this.darkenPaint;
	}

	public final Paint getDarkFill() {
		return this.darkFillProperty().get();
	}

	public final void setDarkFill(final Paint darkFill) {
		this.darkFillProperty().set(darkFill);
	}

	// 样式
	private static class StyleableProperties {
		private static final CssMetaData<RXSingleDigit, Paint> BRIGHTEN_PAINT = new CssMetaData<RXSingleDigit, Paint>(
				"-rx-brighten-color", PaintConverter.getInstance(), Color.BLACK) {

			@Override
			public boolean isSettable(RXSingleDigit control) {
				return control.brightenPaint == null || !control.brightenPaint.isBound();
			}

			@Override
			public StyleableProperty<Paint> getStyleableProperty(RXSingleDigit control) {
				return control.lightFillProperty();
			}
		};

		private static final CssMetaData<RXSingleDigit, Paint> DARKEN_PAINT = new CssMetaData<RXSingleDigit, Paint>(
				"-rx-darken-color", PaintConverter.getInstance(), Color.web("#dddddd")) {

			@Override
			public boolean isSettable(RXSingleDigit control) {
				return control.darkenPaint == null || !control.darkenPaint.isBound();
			}

			@Override
			public StyleableProperty<Paint> getStyleableProperty(RXSingleDigit control) {
				return control.darkFillProperty();
			}
		};

		// 创建一个CSS样式的表
		private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
		static {
			final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
			Collections.addAll(styleables, BRIGHTEN_PAINT, DARKEN_PAINT);
			STYLEABLES = Collections.unmodifiableList(styleables);
		}
	}

	@Override
	protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		return getClassCssMetaData();
	}

	// 这个方法写了,可以在SceneBuilder的Style里出现 等提示
	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return StyleableProperties.STYLEABLES;
	}

	public final SimpleIntegerProperty digitProperty() {
		return this.digit;
	}

	public final int getDigit() {
		int value = this.digitProperty().getValue();
		if (value < 0) {
			return 0;
		} else if (value > 9) {
			return 9;
		} else {
			return value;
		}
	}

	public final void setDigit(final int digit) {
		if (digit < 0) {
			this.digitProperty().setValue(0);
		} else if (digit > 9) {
			this.digitProperty().setValue(9);
		} else {
			this.digitProperty().setValue(digit);
		}

	}

}
