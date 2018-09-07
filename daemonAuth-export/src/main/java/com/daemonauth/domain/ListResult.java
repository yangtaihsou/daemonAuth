package com.daemonauth.domain;


import com.daemonauth.domain.enums.ResultInfoEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListResult<T> extends AbstractResult implements Serializable {
    private static final long serialVersionUID = -7450177725900417295L;

    private List<T> list = new ArrayList<T>();

    private Long count;//用于分页，总记录条数

    public ListResult() {
    }

    public ListResult(ResultInfoEnum info) {
        super(info);
    }

    public ListResult(String code, String message) {
        super(code, message);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean add(T value) {
        return this.list.add(value);
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


}
