package com.trong.clas.screen.importwallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.trong.clas.doan_web3j.R;
import com.trong.clas.model.SavedInfor;
import com.trong.clas.util.DialogFactory;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportPrvKeyFragment extends Fragment {

    private EditText mPrivateKeyEdt;
    private Button mImportBtn;
    public ImportPrvKeyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_import_prv_key, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        mPrivateKeyEdt = v.findViewById(R.id.import_private_key_edt);
        mImportBtn = v.findViewById(R.id.import_private_key_btn);

        mImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prvKey = mPrivateKeyEdt.getText().toString();
                if (prvKey == null || prvKey.equals("")) {
                    DialogFactory.simpleMessWithBtnOk(getContext(),"",getResources().getString(R.string.you_must_enter_all_infor)).show();
                }
                else {
                    try{
                        ECKeyPair keyPair = ECKeyPair.create(new BigInteger(prvKey));
                        Credentials credentials = Credentials.create(keyPair);
                        SavedInfor infor = new SavedInfor();
                        infor.setAddress(credentials.getAddress());
                        infor.setmIsCurrentWallet(true);
                        infor.setPrivatekey(credentials.getEcKeyPair().getPrivateKey().toString());
                        infor.setPublickey(credentials.getEcKeyPair().getPublicKey().toString());
                        ((ImportWalletActivity) getActivity()).onImport(infor);
                    }
                    catch(Exception e) {
                        DialogFactory.simpleMessWithBtnOk(getContext(),"",getResources().getString(R.string.error_happened)).show();
                    }
                }
            }
        });
    }

}
