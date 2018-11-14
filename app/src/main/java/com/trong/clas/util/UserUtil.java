package com.trong.clas.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.trong.clas.doan_web3j.Wallet;
import com.trong.clas.model.SavedInfor;

import static android.content.Context.MODE_PRIVATE;

public class UserUtil {
    private UserUtil() {};
    public static UserUtil newInstance() {
        return new UserUtil();
    }

    public boolean saveUserInfo(Context context, SavedInfor infor){
        try{
            SharedPreferences.Editor editor = context.getSharedPreferences(Constant.WALLETS, MODE_PRIVATE).edit();
            editor.putString(Constant.CURRENT_WALLET, infor.getAddress() );
            editor.apply();

//            SavedInfor.saveToRealm(infor);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
