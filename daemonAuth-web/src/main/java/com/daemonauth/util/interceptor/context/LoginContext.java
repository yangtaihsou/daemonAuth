package com.daemonauth.util.interceptor.context;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: yangkuan
 * Date: 13-11-28
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class LoginContext {
    private final static Log log = LogFactory.getLog(LoginContext.class);


    /**
     * Constant for the LoginContextt object.
     */
    public static final String HTTP_LOGIN_CONTEXT = "com.daemonauth.common.web.LoginContext";
    /**
     * dotnet登录写入的对象
     */
    public static final String HTTP_DOTNET_LOGIN_TICKET_CONTEXT = "com.daemonauth.common.struts.interceptor.dotnet.FormsAuthenticationTicket";

    /**
     * 用户ID
     */
    private long userId;
    /**
     * 登录名称
     */
    private String pin;


    /**
     * 显示名称

     */
    private String nick;
    /**
     * passport cookie的checksum
     */
    private int checksum;
    /**
     * 创建时间
     * 默认为当前时间
     */
    private long created = System.currentTimeMillis();
    /**
     * 过期时间
     * 如果没有指定，就使用拦截器默认的
     */
    private long expires;

    /**
     * 是否持久化
     */
    private boolean persistent;
    /**
     * 部门名字
     */
    private String departmentName;

    /**
     * Method getUserId returns the userId of this LoginContext object.
     * <p/>
     * 用户ID
     *
     * @return the userId (type long) of this LoginContext object.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Method setUserId sets the userId of this LoginContext object.
     * <p/>
     * 用户ID
     *
     * @param userId the userId of this LoginContext object.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Method getPin returns the pin of this LoginContext object.
     * <p/>
     * 登录名称
     *
     * @return the pin (type String) of this LoginContext object.
     */
    public String getPin() {
        return pin;
    }

    /**
     * Method setPin sets the pin of this LoginContext object.
     * <p/>
     * 登录名称
     *
     * @param pin the pin of this LoginContext object.
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Method getNick returns the nick of this LoginContext object.
     * <p/>
     * 显示名称
     *
     * @return the nick (type String) of this LoginContext object.
     */
    public String getNick() {
        return nick;
    }

    /**
     * Method setNick sets the nick of this LoginContext object.
     * <p/>
     * 显示名称
     *
     * @param nick the nick of this LoginContext object.
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * 实际上是将loginContext放到了ApplicationContext中
     *
     * @param loginContext 对象
     */
    public static void setLoginContext(LoginContext loginContext) {
        ApplicationContext.getContext().put(HTTP_LOGIN_CONTEXT, loginContext);
    }

    /**
     * 取出登录的上下文
     *
     * @return null 如果没有的话
     */
    public static LoginContext getLoginContext() {
      //  return (LoginContext) ApplicationContext.getContext().get(HTTP_LOGIN_CONTEXT);
        LoginContext loginContext = new LoginContext();
        loginContext.setPin("test");
        loginContext.setNick("test");
        return loginContext;
    }

    /**
     * 删除上下文、其实一般不用删除
     */
 /*   public static void remove() {
        ApplicationContext.getContext().getContextMap().remove(HTTP_LOGIN_CONTEXT);
    }*/


    /**
     * 反向构造上下文。
     *
     * @param value 需要反向构造的串。形式如下：userId=123,pin=yangsy,nick=杨思勇
     * @return 上下文
     * @see #toCookieValue()
     */
    public static LoginContext parse(String value) {
        LoginContext context = new LoginContext();
        setValue(value, context);
        return context;
    }

    /**
     * Method setValue ...
     *
     * @param value   of type String
     * @param context of type LoginContext
     */
    protected static void setValue(String value, LoginContext context) {
        if (StringUtils.isNotEmpty(value)) {
            String[] fields = value.split(",");
            for (String keyValues : fields) {
                String[] keyValue = keyValues.split("=");
                if (keyValue.length == 2) {
                    try {
                        String field = keyValue[0];
                        if (StringUtils.isNotBlank(field)) {
                            //BeanUtils.setProperty(context, field, StringEscapeUtils.unescapeCombinedCookie(keyValue[1]));
                        }
                    } catch (Exception e) {
                        log.warn("praser error!", e);
                    }
                }
            }
        }
    }


    /**
     * 将实体系列化成字符串。
     * 2010-12-17 yangsiyong改进：默认值不再写入cookie，以减少cookie的大小。
     * @return 字符串。形式：字段1=值1,字段2=值2。该方法不会返回空
     * @see #parse(String)
     */
    public String toCookieValue() {
        final StringBuilder sb = new StringBuilder();
        if (userId != 0) {
            sb.append(",userId=").append(userId);
        }
        if (StringUtils.isNotEmpty(pin)) {
            sb.append(",pin=").append( pin);
        }
        if (StringUtils.isNotEmpty(nick)) {
            sb.append(",nick=").append( nick);
        }
        if (created != 0) {
            sb.append(",created=").append(created);
        }
        if (checksum != 0) {
            sb.append(",checksum=").append(checksum);
        }
        if (expires != 0) {
            sb.append(",expires=").append(expires);
        }
        if (persistent) {
            sb.append(",persistent=").append(persistent);
        }
        return sb.length() > 0 ? sb.substring(1) : "";
    }


    /**
     * 判断是否登录。标准：trim(pin).length > 0
     *
     * @return true 已经登录  false 没有登录
     */
    public boolean getLogin() {
        return isLogin();
    }

    /**
     * 判断是否登录。标准：trim(pin).length > 0
     *
     * @return true 已经登录  false 没有登录
     */
    public boolean isLogin() {
        return StringUtils.isNotBlank(pin);
    }

    /**
     * Method setChecksum sets the checksum of this LoginContext object.
     * <p/>
     * passport cookie的checksum
     *
     * @param checksum the checksum of this LoginContext object.
     */
    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    /**
     * Method getChecksum returns the checksum of this LoginContext object.
     * <p/>
     * passport cookie的checksum
     *
     * @return the checksum (type int) of this LoginContext object.
     */
    public int getChecksum() {
        return checksum;
    }

    /**
     * Method getCreated returns the created of this LoginContext object.
     * <p/>
     * 创建时间
     *
     * @return the created (type long) of this LoginContext object.
     */
    public long getCreated() {
        return created;
    }

    /**
     * 创建日期
     *
     * @return 创建日期
     */
    public Date getCreatedDate() {
        return new Date(created);
    }


    /**
     * Method setCreated sets the created of this LoginContext object.
     * <p/>
     * 创建时间
     *
     * @param created the created of this LoginContext object.
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Method setCreatedDate sets the createdDate of this LoginContext object.
     *
     * @param created the createdDate of this LoginContext object.
     */
    public void setCreatedDate(Date created) {
        this.created = created.getTime();
    }

    /**
     * 设置创建时间等于当前日期
     */
    public void setCreated() {
        this.created = System.currentTimeMillis();
    }

    /**
     * Method getExpires returns the expires of this LoginContext object.
     * <p/>
     * 过期时间
     *
     * @return the expires (type long) of this LoginContext object.
     */
    public long getExpires() {
        return expires;
    }

    /**
     * Method getExpiresDate returns the expiresDate of this LoginContext object.
     *
     * @return the expiresDate (type Date) of this LoginContext object.
     */
    public Date getExpiresDate() {
        return new Date(expires);
    }

    /**
     * Method setExpires sets the expires of this LoginContext object.
     * <p/>
     * 过期时间
     *
     * @param expires the expires of this LoginContext object.
     */
    public void setExpires(long expires) {
        this.expires = expires;
    }

    /**
     * Method setExpiresDate sets the expiresDate of this LoginContext object.
     *
     * @param expires the expiresDate of this LoginContext object.
     */
    public void setExpiresDate(Date expires) {
        this.expires = expires.getTime();
    }

    /**
     * 设置cookie的过期时间，单位：毫秒
     *
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.expires = this.created + timeout;
    }

    /**
     * Method isPersistent returns the persistent of this LoginContext object.
     * <p/>
     * 是否持久化
     *
     * @return the persistent (type boolean) of this LoginContext object.
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     * Method setPersistent sets the persistent of this LoginContext object.
     * <p/>
     * 是否持久化
     *
     * @param persistent the persistent of this LoginContext object.
     */
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


}
