package com.ayi.account.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ayi.account.AccountListener;
import com.ayi.account.LoginResultListener;

import java.util.ArrayList;

/**
 * Created by oceanzhang on 16/3/10.
 */
public class AyiAccountService extends BaseAccountService {

    private final ArrayList<AccountListener> listeners = new ArrayList<>();
    private LoginResultListener loginResultListener;

    public AyiAccountService(Context context) {
        super(context);
    }

    @Override
    protected void dispatchAccountChanged() {
        if(loginResultListener != null && token() != null){
            loginResultListener.onLoginSuccess(this);
            loginResultListener = null;
        }
        for(AccountListener listener:listeners){
            listener.onAccountChanged(this);
        }
    }

    @Override
    protected void dispatchProfileChanged() {
        for(AccountListener listener:listeners){
            listener.onProfileChanged(this);
        }
    }

    @Override
    public void login(LoginResultListener listener) {
        login(listener,null);
    }
    @Override
    public void login(LoginResultListener listener,String backUri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri.Builder builder = Uri.parse("ayiadmin://login").buildUpon();
        if (backUri != null) {
            builder.appendQueryParameter("back",backUri);
        }
        Uri uri = builder.build();
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        loginResultListener = listener;
    }
    @Override
    public void signup(LoginResultListener listener) {

    }

    @Override
    public void addListener(AccountListener listener) {
        if(listener != null){
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(AccountListener listener) {
        if(listener != null){
            listeners.remove(listener);
        }
    }
}
