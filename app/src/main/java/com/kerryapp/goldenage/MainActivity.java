package com.kerryapp.goldenage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kerryapp.goldenage.common.Global;
import com.kerryapp.goldenage.common.LoginAction;
import com.kerryapp.goldenage.common.PreferenceHelper;
import com.kerryapp.goldenage.common.ContactListManager;

public class MainActivity extends Activity {

    static boolean isBad = false;
    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            String strTmp = "点击Button03";
            ((Button) v).setText(strTmp);

        }//创建监听对象

        public void onClick(Button v) {
            String strTmp = "点击Button02";
            v.setText(strTmp);
//            v.setText(strTmp);
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case Global.LOGIN_SUCCESS:
                btn1.setText(this.getResources().getString(R.string.LoginSuccess));
                break;
        }
    }

    public void LoginProcess(int resultCode) {
        switch (resultCode) {
            case Global.LOGIN_SUCCESS:
                btn1.setText(this.getResources().getString(R.string.LoginSuccess));
                break;
        }
    }

    public void Btn3OnClick(View view) {
        btn1 = (Button) view;
        String strTmp = isBad ? "点击登录" : "已经点击";
        isBad = !isBad;
        btn1.setText(strTmp);
        loginDialog();
    }

    public void Btn4Onclick(View view) {
//        Intent intent = new Intent(MainActivity.this, Main22Activity.class);
//        startActivity(intent);
        ContactListManager.Request(this);
    }

    public void Btn5Onclick(View view) {

        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ContactItemFragment.ARG_LIST_TYPE, 0);

        //Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
    }

    public void BtnFragmentOnclick(View view) {
        Intent intent = new Intent(MainActivity.this, MainFragmentActivity.class);
        startActivity(intent);
    }

    void loginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.login));
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.login_diaglog, null);

        builder.setView(view1);

        final EditText edtName = (EditText) view1.findViewById(R.id.login_passwd);
        final EditText edtPhone = (EditText) view1.findViewById(R.id.edtPhone);

        //确定操作
        builder.setPositiveButton(getResources().getString(R.string.btnOK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String strPasswd = edtName.getText().toString().trim();
                String strPhone = edtPhone.getText().toString().trim();
                if (strPhone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "电话号码为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (strPasswd.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请重新输入密码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginRequest(strPhone, strPasswd);
            }
        });

        //取消操作
        builder.setNegativeButton(getResources().getString(R.string.btnCancel), null);

        builder.show();
    }

    public void loginRequest(String phone, String passwd) {
        LoginAction.Request(this, phone, passwd);
    }

    public void init() {

        Global.setAuthToken(PreferenceHelper.getString(Global.AUTH_TOKEN_KEY, this));
    }
}
