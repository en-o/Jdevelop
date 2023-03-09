package cn.jdevelops.jwtweb.vo;


/**
 * WebApiInterceptor.check
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-10 11:33
 */
public class CheckVO {
    Boolean check;
    String token;

    public CheckVO(Boolean check, String token) {
        this.check = check;
        this.token = token;
    }

    @Override
    public String toString() {
        return "CheckVO{" +
                "check=" + check +
                ", token='" + token + '\'' +
                '}';
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}