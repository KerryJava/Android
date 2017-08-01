package com.example.lipei.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    static boolean isBad = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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



    public OnClickListener listener = new OnClickListener(){
        @Override
        public void onClick(View v) {

            String strTmp="点击Button03";
            ((Button)v).setText(strTmp);

        }//创建监听对象
        public void onClick(Button v){
            String strTmp="点击Button02";
            v.setText(strTmp);
//            v.setText(strTmp);
        }

    };

    public void Btn3OnClick(View view){
        String strTmp= isBad ? "点击Button04" : "zha chichi";
        isBad = !isBad;
        ((Button)view).setText(strTmp);

    }

    public void Btn4Onclick(View view) {
        Intent intent = new Intent(MainActivity.this, Main22Activity.class);
        startActivity(intent);
    }

    public void Btn5Onclick(View view) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }


}
