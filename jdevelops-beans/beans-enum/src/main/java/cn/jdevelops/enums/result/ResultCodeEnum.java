package cn.jdevelops.enums.result;


/**
 * 返回值 code message 枚举
 *  result code 2开头
 * @author tn
 * @date 2019年07月29日 14:16
 */
public enum ResultCodeEnum {
    /**
     * 成功
     */
    SUCCESS(200,"成功"),

    /**
     * 失败
     */
    ERROR(404,"页面不存在"),

    /**
     * 失败
     */
    FAIL(10000,"失败"),

    /**
     * 失败
     */
    FAIL_5(500,"失败"),

    /**
     * 业务异常
     */
    BIZ_ERROR(10001,"业务异常"),

    /**
     * 入参校验异常
     */
    CHECK_ERROR(10002,"入参校验异常"),


    /**
     * 系统异常
     */
    SYS_ERROR(10005,"系统异常"),

    /**
     * 未找到
     */
    NOT_FOUND_ERROR(10006,"未找到"),

    /**
     * 系统限流
     */
    SYS_THROTTLING(10007,"系统限流"),


    /**
     * 数据访问异常
     */
    DATA_ACCESS_EXCEPTION(10010,"数据访问异常"),

    /**
     * JSON格式错误
     */
    JSON_ERROR(10011,"JSON格式错误"),


    /**
     * 消息不可读
     */
    MESSAGE_NO_READING(10012,"消息不可读"),

    /**
     * 消息不可读
     */
    API_SIGN_ERROR(10013,"接口签名不正确"),

    /**
     * 敏感词汇
     */
    SENSITIVE_WORD(10014,"敏感词汇，请重新输入"),

    /**
     * 参数不正确
     */
    PARAMETER_ERROR(10015,"参数不正确"),

    /**
     * 数据重复
     */
    DATA_REDO(10016,"数据重复"),


    /**
     * 数据格式检验失败
     */
    VALID_ERROR(2444,"数据格式检验失败"),

    /**
     * 用户不存在
     */
    SQL_ERROR(3306,"sql异常，详情请查看日志"),




    ;


    /**
     * code
     */
    private final int code;
    /**
     * 消息
     */
    private final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
