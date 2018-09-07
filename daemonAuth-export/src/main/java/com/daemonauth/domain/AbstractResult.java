package com.daemonauth.domain;


import com.daemonauth.domain.enums.ResultInfoEnum;

/**
 * 返回结果抽象类<br>
 * User: laitao<br>
 * Date: 15-3-18<br>
 * Time: 下午2:48<br>
 */
public abstract class AbstractResult {

    /**
     * 结果编码
     */
    private String code;
    /**
     * 结果信息
     */
    private String message;

    public AbstractResult() {
    }

    public AbstractResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public AbstractResult(ResultInfoEnum info) {
        if (info != null) {
            code = info.getErrorCode();
            message = info.getErrorMsg();
        }
    }

    public AbstractResult(AppRuntimeException info) {
        if (info != null) {
            code = info.getErrorCode();
            if (code == null) {
                setInfo(ResultInfoEnum.UNKNOW_ERROR);
                message = info.getMessage();
            } else {
                message = info.getErrorMessage();
            }
        } else {
            setInfo(ResultInfoEnum.UNKNOW_ERROR);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setInfo(ResultInfoEnum info) {
        if (info != null) {
            code = info.getErrorCode();
            message = info.getErrorMsg();
        }
    }


}
