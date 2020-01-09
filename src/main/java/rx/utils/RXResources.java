package rx.utils;

import java.net.URL;

public final class RXResources {

    public static URL load(String path) {
        return RXResources.class.getResource(path);
    }

    private RXResources() {}

}