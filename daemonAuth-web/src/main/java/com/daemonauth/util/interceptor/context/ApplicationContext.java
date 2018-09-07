package com.daemonauth.util.interceptor.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yangkuan
 * Date: 13-11-28
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationContext {
    static ThreadLocal<ApplicationContext> aplicationContext = new ThreadLocal<ApplicationContext>();

    /**
     * Returns the ApplicationContext specific to the current thread.
     *
     * @return the ApplicationContext for the current thread, is never <tt>null</tt>.
     */
    public static ApplicationContext getContext() {
        return aplicationContext.get();
    }

    /**
     * Sets the action context for the current thread.
     *
     * @param context the action context.
     */
    public static void setContext(ApplicationContext context) {
        aplicationContext.set(context);
    }
    /**
     * Creates a new ApplicationContext initialized with another context.
     */
    public ApplicationContext() {
    }

    private Map<String, Object> context = new HashMap<String, Object>();

    /**
     * Returns a value that is stored in the current ActionContext by doing a lookup using the value's key.
     *
     * @param key the key used to find the value.
     * @return the value that was found using the key or <tt>null</tt> if the key was not found.
     */
    public Object get(String key) {
        return context.get(key);
    }

    /**
     * Stores a value in the current ActionContext. The value can be looked up using the key.
     *
     * @param key   the key of the value.
     * @param value the value to be stored.
     */
    public void put(String key, Object value) {
        context.put(key, value);
    }
}
