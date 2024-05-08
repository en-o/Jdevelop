package cn.jdevelops.authentication.jwt.vo;

import cn.jdevelops.util.jwt.constant.SignState;

/**
 * 签名对象
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/9 10:04
 */
public class TokenSign {

    /**
     * token
     */
    String sign;

    /**
     * token描述
     * @see SignState
     */
    String description;

    public TokenSign() {
    }


    public TokenSign(String sign) {
        this.sign = sign;
        this.description = SignState.normal;
    }

    public TokenSign(String sign, String description) {
        this.sign = sign;
        this.description = description;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TokenVO{" +
                "sign='" + sign + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
