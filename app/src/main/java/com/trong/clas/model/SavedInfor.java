package com.trong.clas.model;

import android.util.Log;

import com.trong.clas.doan_web3j.Wallet;

import org.web3j.crypto.Credentials;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class SavedInfor extends RealmObject implements Serializable {
    @PrimaryKey
    private String mAddress;
    private String mPublickey;
    private String mPrivatekey;
    private String mPassword;
    private String mAccountName;
    private boolean mIsCurrentWallet;

    @Override
    public String toString() {
        return "SavedInfor{" +
                "mAddress='" + mAddress + '\'' +
                ", mPublickey='" + mPublickey + '\'' +
                ", mPrivatekey='" + mPrivatekey + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mAccountName='" + mAccountName + '\'' +
                ", mIsCurrentWallet=" + mIsCurrentWallet +
                '}';
    }

    public SavedInfor() {
    }
    public static SavedInfor getInstance(SavedInfor infor) {
        SavedInfor res = new SavedInfor();
        res.setAccountName(infor.getAccountName());
        res.setPassword(infor.getPassword());
        res.setAddress(infor.getAddress());
        res.setPrivatekey(infor.getPrivatekey());
        res.setPublickey(infor.getPublickey());
        res.setmIsCurrentWallet(infor.ismIsCurrentWallet());
        return res;
    }
    public boolean ismIsCurrentWallet() {
        return mIsCurrentWallet;
    }

    public void setmIsCurrentWallet(boolean mIsCurrentWallet) {
        this.mIsCurrentWallet = mIsCurrentWallet;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String mAccountName) {
        this.mAccountName = mAccountName;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getPublickey() {
        return mPublickey;
    }

    public void setPublickey(String mPublickey) {
        this.mPublickey = mPublickey;
    }

    public String getPrivatekey() {
        return mPrivatekey;
    }

    public void setPrivatekey(String mPrivatekey) {
        this.mPrivatekey = mPrivatekey;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public static boolean saveToRealm(SavedInfor walletInfor) {
        try{
            Realm realm = Realm.getDefaultInstance();
            SavedInfor infor = new SavedInfor();
            infor.setAddress(walletInfor.getAddress());
            infor.setPassword(walletInfor.getPassword());
            infor.setPublickey(walletInfor.getPublickey());
            infor.setPrivatekey(walletInfor.getPrivatekey());
            infor.setAccountName(walletInfor.getAccountName());
            infor.setmIsCurrentWallet(walletInfor.ismIsCurrentWallet());
            realm.beginTransaction();
            realm.copyToRealm(infor);
            realm.commitTransaction();
            realm.close();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public static ArrayList<SavedInfor> getSavedWalletsInfor() {
        final ArrayList<SavedInfor> wallets = new ArrayList<>();
        try{

            Realm realm = Realm.getDefaultInstance();
           realm.executeTransaction(new Realm.Transaction() {
               @Override
               public void execute(Realm realm) {
                   RealmResults<SavedInfor> realmResults =  realm.where(SavedInfor.class)
                           .findAll();
                   for (SavedInfor infor :realmResults) {
                       wallets.add(infor);
                   }
               }
           });
//           String src = "{\"address\":\"c179812e20fb5292f4830d6794b48fcc91cdd2c7\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"b8e8fa295b57f05abbe667d043e8a0b79ada9ae21f28a6ca47674c3f54e724d3\",\"cipherparams\":{\"iv\":\"9fea1f747930428ee9791b77e182adab\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"0172761d94261396687980b436d5345bef04859b6d465ee663139ab9e09d38b8\"},\"mac\":\"cb9d0b10fd1fe1b8064acd4feeee32fa05193c3d89a17609901aa5aeaf0e6ab7\"},\"id\":\"8984237c-61e2-4be5-9fe1-6be9af99512b\",\"version\":3}";
//            Credentials credentials = Wallet.loadCredentialsByString("23111996",src);
//            if (credentials!=null) Log.d("credential",credentials.toString());
            return wallets;
        }
        catch(Exception e) {
            return wallets;
        }
    }

    public static SavedInfor getSavedInforByAddress(String address) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<SavedInfor> realmResults = realm.where(SavedInfor.class)
                    .equalTo("mAddress", address)
                    .findAll()
                    .sort("mAddress",Sort.ASCENDING);
            if (realmResults != null) {
                return realmResults.get(0);
            }
            realm.close();;
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean updateSavedInfor(SavedInfor wallet) {
        try{
            Realm realm = Realm.getDefaultInstance();
            SavedInfor res = realm.where(SavedInfor.class)
                    .equalTo("mAddress",wallet.getAddress())
                    .findFirst();
            realm.beginTransaction();
            if (res != null) {
                res.setPassword(wallet.getPassword());
                res.setAccountName(wallet.getAccountName());
                res.setPrivatekey(wallet.getPrivatekey());
                res.setPublickey(wallet.getPublickey());
                res.setmIsCurrentWallet(wallet.ismIsCurrentWallet());
                realm.commitTransaction();
                realm.close();
                return true;
            }
            else {
                realm.commitTransaction();
                realm.close();
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
}
