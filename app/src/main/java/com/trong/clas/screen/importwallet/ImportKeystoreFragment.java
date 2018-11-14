package com.trong.clas.screen.importwallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.trong.clas.doan_web3j.R;
import com.trong.clas.doan_web3j.Wallet;
import com.trong.clas.model.SavedInfor;
import com.trong.clas.util.DialogFactory;

import org.web3j.crypto.Credentials;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportKeystoreFragment extends Fragment {

    private EditText mKeyStoreEdt;
    private EditText mPasswordEdt;
    private Button mBtnImport;
    public ImportKeystoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_import_keystore, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        mKeyStoreEdt = v.findViewById(R.id.import_key_store_edt);
        mPasswordEdt = v.findViewById(R.id.import_password_edt);
        mBtnImport = v.findViewById(R.id.import_key_store_btn);
        mBtnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyStore = mKeyStoreEdt.getText().toString();
                String password = mPasswordEdt.getText().toString();
                if (keyStore == null || keyStore.equals("") ||
                        password == null || password.equals("")) {
                    DialogFactory.simpleMessWithBtnOk(getContext(),"",getResources().getString(R.string.you_must_enter_all_infor)).show();
                }
                else {
                    Credentials credentials = Wallet.loadCredentialsByString(password,keyStore);
                    SavedInfor infor = new SavedInfor();
                    if (credentials == null) {
                        DialogFactory.simpleMessWithBtnOk(getContext(),"",getResources().getString(R.string.inputed_infor_is_incorrect)).show();
                    }
                    else {
                        infor.setAddress(credentials.getAddress());
                        infor.setPrivatekey(credentials.getEcKeyPair().getPrivateKey().toString());
                        infor.setPublickey(credentials.getEcKeyPair().getPublicKey().toString());
                        infor.setmIsCurrentWallet(true);
                        ((ImportWalletActivity) getActivity()).onImport(infor);
                    }
                }
            }
        });
    }

}
