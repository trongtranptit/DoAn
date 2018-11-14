package com.trong.clas.util;

import android.content.Context;

import com.trong.clas.doan_web3j.R;

import java.math.BigInteger;

public class Utilize {
    public static String getDisplayedBalance(Context context, String balanceInWei) {
        int endIndex = 5;
        int dotPos = 0;
        String res = "";
        String _1_ether = context.getResources().getString(R.string._1_ether);
        while (balanceInWei.length() < _1_ether.length()) {
            balanceInWei = "0"+balanceInWei;
        }
        int l = balanceInWei.length() - 1;
        for (int i= _1_ether.length() - 1;i >0;i--) {
            res = balanceInWei.charAt(l) + res;
            l--;
        }
        dotPos = l;
        res = "."+res;
        while (l>=0) {
            res = balanceInWei.charAt(l) + res;
            l--;
        }

        return res.substring(0,dotPos + endIndex);
    }
}
