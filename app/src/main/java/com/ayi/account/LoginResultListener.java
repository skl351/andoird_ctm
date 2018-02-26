package com.ayi.account;

/**
 * AccountListener与LoginResultListener区别在于，
 * AccountListener类似广播事件。
 * LoginResultListener是回调事件。
 */
public interface LoginResultListener {

    public void onLoginSuccess(AccountService sender);

    public void onLoginCancel(AccountService sender);
}
