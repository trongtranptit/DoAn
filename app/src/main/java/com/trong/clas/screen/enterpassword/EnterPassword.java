package com.trong.clas.screen.enterpassword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.trong.clas.doan_web3j.R;
import com.trong.clas.util.Constant;
import com.trong.clas.util.DialogFactory;

public class EnterPassword extends AppCompatActivity {
    private EditText mEdtPassword;
    private EditText mEdtConfirmPw;
    private Button mBtnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        initView();
    }

    private void initView() {
        mEdtConfirmPw = findViewById(R.id.edt_confirm_pw);
        mEdtPassword = findViewById(R.id.edt_pw);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw = mEdtPassword.getText().toString();
                String cfPw = mEdtConfirmPw.getText().toString();

                if (pw== null || pw.equals("")) {
                    DialogFactory.simpleMessWithBtnOk(EnterPassword.this, "", getResources().getString(R.string.you_must_enter_password)).show();
                }
                else if (!pw.equals(cfPw)) {
                    DialogFactory.simpleMessWithBtnOk(EnterPassword.this, "", getResources().getString(R.string.confirm_password_does_not_match)).show();
                }
                else if (pw.equals(cfPw)){
                    Intent t = new Intent();
                    t.putExtra(Constant.PASSWORD, pw);
                    setResult(RESULT_OK, t);
                    finish();
                }
            }
        });
    }
}
