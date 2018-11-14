package com.trong.clas.doan_web3j;

import android.os.Environment;
import android.provider.UserDictionary;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trong.clas.util.Constant;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.wordlists.English;

public class Wallet {
    public static Credentials createWallet(String password) {
        try{


            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            File file = new File(path);
            file.createNewFile();




            String fileName = WalletUtils.generateWalletFile(password,
                    createECKeyPair(createMnemonics()),new File(path), false);
            Credentials credentials = Wallet.loadCredentials(password, path + "/"+fileName);
            Log.d("credential", credentials.toString());
            File fileToDelete = new File(path + "/"+fileName);
            fileToDelete.delete();
            return credentials;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Credentials loadCredentials( String password, String path) {
        try{
            Credentials credentials = WalletUtils.loadCredentials(password, new File(path));
            Log.d("credential: ","priv key: " + credentials.getEcKeyPair().getPrivateKey());
            Log.d("credential: ","address: " + credentials.getAddress());
            return credentials;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Credentials loadCredentialsByString(String password, String source){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WalletFile walletFile = null;
            walletFile = objectMapper.readValue(source, WalletFile.class);

            return Credentials.create(org.web3j.crypto.Wallet.decrypt(password, walletFile));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getBalance(String address, String tokenTestnest) {
        Web3j web3j = Web3jFactory.build(new HttpService(tokenTestnest));
        try {
            EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            return balance.getBalance().toString();
        }
        catch (Exception e) {
            return null;
        }
    }

    private static ECKeyPair createECKeyPair(String mnemonics){
        String[] pathArray
                = "m/44'/60'/0'/0/0".split("/");
        String passphrase = "";
        String[] list = mnemonics.split(" ");
        long createationTImeSeconds = System.currentTimeMillis() / 10;
        DeterministicSeed ds = new
                DeterministicSeed(Arrays.asList(list), null, passphrase,createationTImeSeconds);
        byte[] seedbytes = ds.getSeedBytes();
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedbytes);
        for (int i = 1; i < pathArray.length; i++){
            ChildNumber childNumber = null;
            if (pathArray[i].endsWith("'")){
                int number = Integer.parseInt(pathArray[i].substring(0, pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(dkKey.getPrivKey());
        return ecKeyPair;
    }

    private static String createMnemonics(){
        final StringBuilder sb  = new StringBuilder();
        byte[] entropy = new byte[128];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, new MnemonicGenerator.Target() {
                    @Override
                    public void append(CharSequence string) {
                        sb.append(string);
                    }
                });
        return sb.toString();
   }
}
