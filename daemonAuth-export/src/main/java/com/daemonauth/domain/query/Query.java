package com.daemonauth.domain.query;

/**
 * User:
 * Date: 15-1-8
 * Time: 下午6:02
 */
public class Query<T> {
    private T query;

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }
}
