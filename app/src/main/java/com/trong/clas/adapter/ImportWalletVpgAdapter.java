package com.trong.clas.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.trong.clas.screen.importwallet.ImportKeystoreFragment;
import com.trong.clas.screen.importwallet.ImportPrvKeyFragment;

public class ImportWalletVpgAdapter extends FragmentPagerAdapter {
    public ImportWalletVpgAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ImportKeystoreFragment();
        }
        else if (position == 1) {
            return new ImportPrvKeyFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Key store";
        }
        else if (position == 1) {
            return "Private Key";
        }
        return null;
    }
}
