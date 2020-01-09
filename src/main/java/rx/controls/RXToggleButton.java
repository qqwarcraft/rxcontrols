package rx.controls;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;

/*此组件: 行为上类似于RadioButton: 点击一次是选择,点击第二次依然是选择,不会取消
外观上类似于普通的ToggleButton: 没有了单选按钮前面的圆点,避免了写很多的css去屏蔽圆点
 */
public class RXToggleButton extends ToggleButton {
	private static final String DEFAULT_STYLE_CLASS = "rx-toggle-button";

	public RXToggleButton() {
		super();
		init();
	}

	public RXToggleButton(String text) {
		super(text);
		init();
	}

	private void init() {
		getStyleClass().add(DEFAULT_STYLE_CLASS);
	}

	@Override
	public void fire() {
		if (getToggleGroup() == null || !isSelected()) {
			super.fire();
		}
	}
}
