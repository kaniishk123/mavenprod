package devops.challenge;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class VersionInfo {

    private static String version = "UNKNOWN";

    static {
        try {
            Manifest manifest = new Manifest(
                    VersionInfo.class.getResourceAsStream("/META-INF/MANIFEST.MF"));
            Attributes attrs = manifest.getMainAttributes();
            version = attrs.getValue("Implementation-Version");
        } catch (Exception ignored) {}
    }

    public static String getVersion() {
            return version;
    }
}
