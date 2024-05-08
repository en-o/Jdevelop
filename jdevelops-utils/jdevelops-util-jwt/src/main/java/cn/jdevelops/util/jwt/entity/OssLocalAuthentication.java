package cn.jdevelops.util.jwt.entity;

/**
 * 本地上次文件访问鉴权
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/18 07:06
 */
public class OssLocalAuthentication {

    /**
     *  是否开启  local oss file access authentication
     *  默认false
     */
    private boolean enable = false;

    /**
     * 鉴权的 cookie key
     */
    private String ossLocalJwtKey;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getOssLocalJwtKey() {
        return ossLocalJwtKey;
    }

    public void setOssLocalJwtKey(String ossLocalJwtKey) {
        this.ossLocalJwtKey = ossLocalJwtKey;
    }

    @Override
    public String toString() {
        return "OssLocalAuthentication{" +
                "enable=" + enable +
                ", ossLocalJwtKey='" + ossLocalJwtKey + '\'' +
                '}';
    }
}
