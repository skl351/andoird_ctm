package com.ayi.account;


import com.ayi.entity.UserInfo;

/**
 * Created by oceanzhang on 16/3/10.
 * 账号服务:负责登陆 注册 登出
 * 1.需要关心Account的页面，要先profile()判断是否有登录 没有登录的情况下，调用 login(LoginResultListener
 * listener)
 * 2.需要关心Account的页面, 调用 addListener(AccountListener listener)
 * 3.登录成功后，会先call LoginResultListener.onLoginSuccess, 然后再调用
 * AccountListener.onAccountChanged。
 */
public interface AccountService {
    public UserInfo profile();
    public String id();
    public String token();
    public String verifyCode();
    public String mobile();
    public void login(LoginResultListener listener, String backUri);
    public void login(LoginResultListener listener);
    public void signup(LoginResultListener listener);

    public void logout();

    /**
     * 未登陆时，进行登陆，并触发AccountListener.onAccountChanged事件
     * <p/>
     * <p/>
     * 已登录时，更新当前账号信息
     * <p/>
     * 只更新增量字段，如传入的profile只包含UserName，则只更新UserName字段，其他字段值不受影响。<br>
     * 如传入的profile中带有Token，则Token被忽略<br>
     * 如传入的profile中带有UserID，则必须与当前的UserID一致，否则忽略<br>
     * 任意字段更新都会触发AccountListener.onProfileChanged事件<br>
     */
    public void update(UserInfo profile);

    public void addListener(AccountListener listener);

    public void removeListener(AccountListener listener);
}
