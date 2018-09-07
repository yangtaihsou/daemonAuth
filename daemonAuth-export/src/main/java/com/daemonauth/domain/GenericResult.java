package com.daemonauth.domain;


import com.daemonauth.domain.enums.ResultInfoEnum;

public class GenericResult<T> extends AbstractResult {

    private T value;

    public GenericResult() {
        super(ResultInfoEnum.UNKNOW_ERROR);
    }

    public GenericResult(ResultInfoEnum info) {
        super(info);
    }

    public GenericResult(String code, String message) {
        super(code, message);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
