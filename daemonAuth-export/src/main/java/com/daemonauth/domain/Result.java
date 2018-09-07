package com.daemonauth.domain;

/**
 * User:
 * Date: 14-11-20
 * Time: 上午11:37
 */
public class Result {
    private Boolean status;
    private String reason;
    private Integer count;
    private String content;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
