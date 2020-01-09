package rx.controls;

import javafx.scene.control.Pagination;
import javafx.scene.control.Skin;
import rx.skins.RXPaginationSkin;

public class RXPagination extends Pagination {
    private static final String DEFAULT_STYLE_CLASS = "rx-pagination";

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RXPaginationSkin(this);
    }

    public RXPagination(int pageCount, int pageIndex) {
       super(pageCount,pageIndex);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public RXPagination(int pageCount) {
        this(pageCount, 0);
    }
    public RXPagination(){
       super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

}
