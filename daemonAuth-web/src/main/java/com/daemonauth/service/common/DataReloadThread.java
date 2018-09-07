package com.daemonauth.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * User:
 * Date: 15-3-19
 * Time: 上午10:20
 */
@Service("dataReloadThread")
public class DataReloadThread {

    private final static Logger log = LoggerFactory.getLogger(DataReloadThread.class);
    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    @PostConstruct
    public void excute() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (; ; ) {
                    try {
                        Thread.sleep(10000L);
                        authorityDataConfig.reload();
                    } catch (Exception e) {
                        log.error("线程10秒无限循环重载数据抛出异常:", e);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
