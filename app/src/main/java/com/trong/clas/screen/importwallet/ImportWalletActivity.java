package com.trong.clas.screen.importwallet;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trong.clas.adapter.ImportWalletVpgAdapter;
import com.trong.clas.doan_web3j.R;
import com.trong.clas.model.SavedInfor;
import com.trong.clas.screen.start.StartActivity;
import com.trong.clas.util.Constant;

public class ImportWalletActivity extends AppCompatActivity {
    private ViewPager mVpg;
    private TabLayout mTab;
    private ImportWalletVpgAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
        mVpg = findViewById(R.id.import_wallet_vpg);
        mTab = findViewById(R.id.import_wallet_tab_layout);
        mAdapter = new ImportWalletVpgAdapter(getSupportFragmentManager());
        mVpg.setAdapter(mAdapter);
        mTab.setupWithViewPager(mVpg);
    }

    public void onImport(SavedInfor infor) {
        Intent t = new Intent();
        t.putExtra(Constant.IMPORTED_WALLET_INFO, infor);
        setResult(RESULT_OK, t);
        finish();
    }
}
