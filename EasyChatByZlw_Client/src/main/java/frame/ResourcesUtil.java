package frame;

import java.io.File;

/**
 * 用于将某一个类中访问资源时，将资源访问路径引导到resources资源文件夹下。
 */
public class ResourcesUtil {
    public static final String resourcesPath = ResourcesUtil.class.getResource("/").getPath() + File.separator;
}
