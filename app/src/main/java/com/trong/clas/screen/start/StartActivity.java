package com.trong.clas.screen.start;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.trong.clas.adapter.DrawerListWalletRcvAdapter;
import com.trong.clas.adapter.TransactionAdapter;
import com.trong.clas.doan_web3j.R;
import com.trong.clas.doan_web3j.Wallet;
import com.trong.clas.model.SavedInfor;
import com.trong.clas.model.Transaction;
import com.trong.clas.screen.enterpassword.EnterPassword;
import com.trong.clas.screen.importwallet.ImportWalletActivity;
import com.trong.clas.screen.sendtx.TransactionActivity;
import com.trong.clas.util.Constant;
import com.trong.clas.util.DialogFactory;
import com.trong.clas.util.UserUtil;
import com.trong.clas.util.Utilize;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;

import io.realm.Realm;

public class StartActivity extends AppCompatActivity implements StartView{
    public static final int REQUEST_PASSWORD_CODE = 1;
    public static final int IMPORT_WALLET_CODE = 2;
    public static final int SEND_TX_CODE = 3;
    private ProgressDialog mDialog;
    private Button mBtnCreateWallet, mBtnImportWallet;
    private ScrollView mScrollView;
    private LinearLayout mLLCreateImport;
    private Toolbar mHomeToolbar;
    private LinearLayout mLLTabSent;
    private LinearLayout mLLTabToken;
    private TextView mTvBalance;
    private TextView mTvAccount;
    private TextView mTvAddress;
    private TextView mTvError;
    private Button mBtnBuy;
    private Button mBtnSend;
    private RecyclerView mRcvTx;
    private RecyclerView mRcvWallets;
    private DrawerListWalletRcvAdapter mListWalletAdapter;
    private TransactionAdapter mListTxAdapter;
    private LinearLayoutManager mListWalletLayoutManager;
    private LinearLayoutManager mListTxLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ImageView mIvNavigateMenu;
    private StartPresenter mPresenter;
    private SavedInfor mCurrentInfor;
    private ArrayList<SavedInfor> mAllInfors;
    private ArrayList<Transaction> mTxList;
    private ImageView mIvAddWallet;
    private String mBalance;
    private String permissions[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        mDialog = new ProgressDialog(this);
        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissions, Constant.REQUEST_PERMISSION_CODE);
            }
            else {
                initView();
            }
        }
        else {
            initView();
        }

    }

    private void tabSentClicked() {
        mRcvTx.setVisibility(View.VISIBLE);
        mLLTabToken.setBackgroundResource(R.color.white);
        mLLTabSent.setBackgroundResource(R.color.bg_gray);
    }

    private void tabTokenClicked(){
        mRcvTx.setVisibility(View.GONE);
        mLLTabToken.setBackgroundResource(R.color.bg_gray);
        mLLTabSent.setBackgroundResource(R.color.white);
    }

    private boolean isAlreadySignedIn() {
        SharedPreferences prefs = getSharedPreferences(Constant.WALLETS, MODE_PRIVATE);
        String walletAddress = prefs.getString(Constant.CURRENT_WALLET,null);
        mAllInfors = new ArrayList<>();
        mAllInfors = SavedInfor.getSavedWalletsInfor();
        for (SavedInfor infor : mAllInfors){
            if (infor.getAddress().equals(walletAddress)) {
                mCurrentInfor = infor;
                break;
            }
        }
        if (mCurrentInfor != null){
            return true;
        }
        else {
            return false;
        }
    }

    private void initView() {
        setContentView(R.layout.activity_start);
        mPresenter = new StartPresenter();
        mPresenter.attachView(this);
        mIvAddWallet = findViewById(R.id.iv_add_wallet);
        mHomeToolbar = findViewById(R.id.start_screen_toolbar);
        mTvError = findViewById(R.id.start_screen_tv_error);
        mBtnCreateWallet = findViewById(R.id.btn_create_wallet);
        mBtnImportWallet = findViewById(R.id.btn_import_wallet);
//        mScrollView = findViewById(R.id.scroll_main_content);
        mLLCreateImport = findViewById(R.id.ll_create_import);
        mLLTabSent = findViewById(R.id.ll_tab_sent);
        mLLTabToken = findViewById(R.id.ll_tab_token);
        mRcvTx = findViewById(R.id.rcv_tx);
        mTvAccount = findViewById(R.id.tv_account);
        mTvBalance = findViewById(R.id.tv_balance);
        mTvAddress = findViewById(R.id.tv_address);
        mBtnBuy = findViewById(R.id.btn_buy);
        mBtnSend = findViewById(R.id.btn_send_tx);
        mDrawerLayout = findViewById(R.id.drawer_main_content);
        mRcvWallets = findViewById(R.id.rcv_list_wallet);
//        mIvNavigateMenu = findViewById(R.id.navigate_menu);

        setSupportActionBar(mHomeToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mHomeToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mBtnImportWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity.this.startActivityForResult(new Intent(StartActivity.this, ImportWalletActivity.class), IMPORT_WALLET_CODE);
            }
        });

        mBtnCreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity.this.startActivityForResult(new Intent(StartActivity.this, EnterPassword.class), REQUEST_PASSWORD_CODE);
            }
        });

        mLLTabSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabSentClicked();
            }
        });

        mLLTabToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabTokenClicked();
            }
        });
        if (isAlreadySignedIn()) {
            showMainScreen();
        }
        else {
            mDrawerLayout.setVisibility(View.GONE);
            mLLCreateImport.setVisibility(View.VISIBLE);
            mTvError.setVisibility(View.GONE);
        }

        mIvAddWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = LayoutInflater.from(StartActivity.this).inflate(R.layout.dialog_add_wallet,null ,false);
                LinearLayout ll_create_new_wallet = v.findViewById(R.id.ll_create_new_wallet);
                LinearLayout ll_import_wallet = v.findViewById(R.id.ll_import_wallet);
                final AlertDialog dialog = new AlertDialog.Builder(StartActivity.this)
                        .setTitle("Add wallet")
                        .setView(v)
                        .create();
                dialog.show();
                ll_create_new_wallet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        StartActivity.this.startActivityForResult(new Intent(StartActivity.this, EnterPassword.class), REQUEST_PASSWORD_CODE);
                    }
                });

                ll_import_wallet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        StartActivity.this.startActivityForResult(new Intent(StartActivity.this, ImportWalletActivity.class), IMPORT_WALLET_CODE);
                    }
                });
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(StartActivity.this, TransactionActivity.class);
                t.putExtra(Constant.BALANCE, mBalance);
                t.putExtra(Constant.SAVED_INFOR, SavedInfor.getInstance(mCurrentInfor));
                t.putExtra(Constant.BALANCE,mBalance);
                StartActivity.this.startActivityForResult(t, SEND_TX_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PASSWORD_CODE && resultCode == RESULT_OK) {
            String pw = data.getStringExtra(Constant.PASSWORD);
            Credentials credentials = Wallet.createWallet(pw);
            SavedInfor infor = new SavedInfor();
            infor.setAddress(credentials.getAddress());
            Log.d("private key",credentials.getEcKeyPair().getPrivateKey().toString());
            infor.setPrivatekey(credentials.getEcKeyPair().getPrivateKey().toString());
            infor.setPublickey(credentials.getEcKeyPair().getPublicKey().toString());
            infor.setPassword(pw);
            infor.setmIsCurrentWallet(true);
            infor.setAccountName("Account "+(mAllInfors.size()+1));
            boolean b = UserUtil.newInstance().saveUserInfo(getApplicationContext(), infor);
            if (b) {
                if (mCurrentInfor!= null ) {
                    SavedInfor si =  SavedInfor.getInstance(mCurrentInfor);
                    si.setmIsCurrentWallet(false);
                    SavedInfor.updateSavedInfor(si);
                }
                SavedInfor.saveToRealm(infor);
                mAllInfors = SavedInfor.getSavedWalletsInfor();
                mCurrentInfor = infor;

                showMainScreen();
            }
        }
        else if ( requestCode == IMPORT_WALLET_CODE && resultCode == RESULT_OK) {
            SavedInfor infor = (SavedInfor) data.getSerializableExtra(Constant.IMPORTED_WALLET_INFO);
            infor.setAccountName("Account "+(mAllInfors.size()+1));
            if (mCurrentInfor == null) mCurrentInfor = new SavedInfor();
            SavedInfor si = SavedInfor.getInstance(mCurrentInfor);
            si.setmIsCurrentWallet(false);
            SavedInfor.updateSavedInfor(si);
            boolean b = UserUtil.newInstance().saveUserInfo(getApplicationContext(), infor);
            if (b) {
                SavedInfor.saveToRealm(infor);
                mAllInfors = SavedInfor.getSavedWalletsInfor();
                mCurrentInfor = infor;
                showMainScreen();
            }
        }
        else if (requestCode == SEND_TX_CODE ) {
            if (resultCode == RESULT_OK) {
                //show dialog success
                DialogFactory.simpleMessWithBtnOk(StartActivity.this, "","Send success !").show();
            }
            else {
                //show dialog failed
                DialogFactory.simpleMessWithBtnOk(StartActivity.this, "", "Send failed !").show();
            }
            //reload main screen
            showMainScreen();
        }
    }

    private void showMainScreen() {
        mPresenter.getBalance(mCurrentInfor.getAddress(), getResources().getString(R.string.infura_rinkeby_token));

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_PERMISSION_CODE) {
            boolean ok = true;
            for (int i= 0; i < permissions.length ; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    ok = false;
                    break;
                }
            }
            if (ok) {
                initView();
            }
            else {
                checkPermissions();
            }
        }
    }

    @Override
    public void showProgress() {
        mDialog.setMessage(getResources().getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onGetBalanceResult(BigInteger balance) {
        // balance in wei
        mBalance = balance.toString();
        //wei -> eth and get float value
        String displayBalance = Utilize.getDisplayedBalance(StartActivity.this, mBalance);
//        mBalance = Convert.fromWei(balance.toString(),Convert.Unit.ETHER).to;
        mTvBalance.setText(displayBalance+" ETH");
        mTvAddress.setText(mCurrentInfor.getAddress());
        mTvAccount.setText(mCurrentInfor.getAccountName());
        mDrawerLayout.setVisibility(View.VISIBLE);
        mTvError.setVisibility(View.GONE);
        mLLCreateImport.setVisibility(View.GONE);
        mListWalletLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mListWalletAdapter = new DrawerListWalletRcvAdapter(this, mAllInfors, this);
        mRcvWallets.setAdapter(mListWalletAdapter);
        mRcvWallets.setLayoutManager(mListWalletLayoutManager);
        mPresenter.getListTx(mCurrentInfor.getAddress());
    }

    @Override
    public void onItemListWalletClick(View view, int pos) {
        SavedInfor infor = (SavedInfor) view.getTag();
        if (!mCurrentInfor.getAddress().equals(infor.getAddress())) {
            SavedInfor si1 = SavedInfor.getInstance(mCurrentInfor);
            si1.setmIsCurrentWallet(false);
            SavedInfor si2 = SavedInfor.getInstance(infor);
            si2.setmIsCurrentWallet(true);
            boolean b1 = SavedInfor.updateSavedInfor(si1);
            boolean b2 = SavedInfor.updateSavedInfor(si2);
            if (b1 && b2) {
                mCurrentInfor = infor;
                mAllInfors = SavedInfor.getSavedWalletsInfor();
                UserUtil.newInstance().saveUserInfo(StartActivity.this, mCurrentInfor);
                showMainScreen();
            }
        }
    }

    @Override
    public void onItemListTxClick(View view, int pos) {
        // show Detail Tx
    }

    @Override
    public void onGetListTx(ArrayList<Transaction> transactions) {
        if (transactions != null) {
            mTxList = transactions;
            mListTxLayoutManager = new LinearLayoutManager(StartActivity.this, LinearLayoutManager.VERTICAL,false);
            mListTxAdapter = new TransactionAdapter(StartActivity.this, mTxList, this);
            mRcvTx.setLayoutManager(mListTxLayoutManager);
            mRcvTx.setAdapter(mListTxAdapter);
        }
    }
}
