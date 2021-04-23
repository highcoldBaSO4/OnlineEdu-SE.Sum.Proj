package com.se231.onlineedu.message.response;

import com.se231.onlineedu.model.SignIn;

/**
 * @author yuxuanLiu
 * @date 2019/08/02
 */
public class SignInWithState {
    private SignIn signIn;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    private boolean ok;

    public SignInWithState() {
    }

    public SignIn getSignIn() {
        return signIn;
    }

    public void setSignIn(SignIn signIn) {
        this.signIn = signIn;
    }

    public SignInWithState(SignIn signIn, boolean ok) {
        this.signIn = signIn;
        this.ok = ok;
    }
}
