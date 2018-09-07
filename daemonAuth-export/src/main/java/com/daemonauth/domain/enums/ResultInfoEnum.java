package com.daemonauth.domain.enums;

/**
 * @Package
 * @Description: TODO
 * @Author
 * @Date 2017/4/12
 * @Time 19:19
 * @Version V1.0
 */
public enum ResultInfoEnum {
    SUCCESS("000000", "交易成功"),
    UNKNOW_ERROR("000001", "未知异常"),
    PARAM_ERROR("000002", "参数错误"),
    JSF_ERROR("100000", "JSF接口异常"),
    DB_ERROR("200000", "数据库异常");


    private String errorCode;
    private String errorMsg;

    ResultInfoEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static ResultInfoEnum findByCode(String code) {
        ResultInfoEnum result = null;
        ResultInfoEnum[] values = ResultInfoEnum.values();
        for (ResultInfoEnum value : values) {
            if (value.getErrorCode().equals(code)) {
                result = value;
            }
        }
        return result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg(String extMsg) {
        if (null != extMsg && !"".equals(extMsg)) {
            return errorMsg + "：" + extMsg;
        }

        return errorMsg;
    }
}
