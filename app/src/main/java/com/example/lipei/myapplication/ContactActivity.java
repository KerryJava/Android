package com.example.lipei.myapplication;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {

    private Button btnAdd;
    private ListView lvPhones;
    private TextView tvPhoneName;
    private TextView tvPhoneNumber;
    private EditText edtPhone;

    private List<Map<String, Object>> ContactsList;  //存储所有通讯录信息

    //获取系统自定义字符串
    @Override
    public Resources getResources() {
        return super.getResources();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        btnAdd = (Button) findViewById(R.id.btnAdd);      //添加
        lvPhones = (ListView) findViewById(R.id.lvPhones);//显示

        //显示联系人
        InitData();

        //添加联系人
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
                builder.setTitle(getResources().getString(R.string.addContact));
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view1 = LayoutInflater.from(ContactActivity.this).inflate(R.layout.contact_add, null);

                builder.setView(view1);

                final EditText edtName = (EditText) view1.findViewById(R.id.edtName);
                final EditText edtPhone = (EditText) view1.findViewById(R.id.edtPhone);

                //确定操作
                builder.setPositiveButton(getResources().getString(R.string.btnOK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strName = edtName.getText().toString().trim();
                        String strPhone = edtPhone.getText().toString().trim();
                        if (strPhone.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "电话号码为空，添加失败!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String telRegex = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";  //
                        if (!edtPhone.getText().toString().matches(telRegex)) {
                            Toast.makeText(getApplicationContext(), "请重新输入正确的电话号码，添加失败!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        writeContacts(strName, strPhone);    //添加联系人
                        InitData();
                    }
                });

                //取消操作
                builder.setNegativeButton(getResources().getString(R.string.btnCancel), null);

                builder.show();

            }
        });

        //为ListView的列表项选中事件绑定事件监听器
        lvPhones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //取得电话号码
                final String phoneNumber = ContactsList.get(i).get("phoneNumber").toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
                builder.setTitle(getResources().getString(R.string.qxz));
                final String[] contactFun = new String[]{getResources().getString(R.string.callPhone), getResources().getString(R.string.sendMessage)};
                builder.setItems(contactFun, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strFun = contactFun[i];
                        if (strFun.equals(getResources().getString(R.string.callPhone))) {
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            startActivity(phoneIntent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber + ""));
                            intent.putExtra("sms_body", "");
                            startActivity(intent);
                        }
                    }
                });
                //取消操作
                builder.setNegativeButton(getResources().getString(R.string.btnCancel), null);
                builder.show();
            }
        });

    }


    //写入通讯录
    public void writeContacts(String name, String phoneNumber) {


        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        // 联系人的Email地址
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "zhangphil@xxx.com");
        // 电子邮件的类型
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        Toast.makeText(this, "联系人数据添加成功", Toast.LENGTH_SHORT).show();

//        ContentResolver resolver = getContentResolver();
//        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
//        Uri dataUri = Uri.parse("content://com.android.contacts/data");
//
//        //查出最后一个ID
//        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
//        cursor.moveToLast();
//        int lastId = cursor.getInt(0);
//        int newId = lastId + 1;
//
//        //插入一个联系人id
//        ContentValues values = new ContentValues();
//        values.put("contact_id", newId);
//        resolver.insert(uri, values);
//
//        //插入电话数据
//        values.clear();
//        values.put("raw_contact_id", newId);
//        values.put("mimetype", ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, strPhone);
//        resolver.insert(dataUri, values);
//
//        //插入姓名数据
//        values.clear();
//        values.put("raw_contact_id", newId);
//        values.put("mimetype", ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strName);
//        resolver.insert(dataUri, values);
    }

    //获取通讯录
    public List<Map<String, Object>> getContacts() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String phoneName;
            String phoneNumber;
            Map<String, Object> listItem = new HashMap<String, Object>();
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            listItem.put("phoneName", phoneName);
            listItem.put("phoneNumber", phoneNumber);
            listItems.add(listItem);
        }
        return listItems;
    }

    //初始化ListView数据
    public void InitData() {
        List<Map<String, Object>> contacts = getContacts();  //获取通讯录
        ContactsList = contacts;
        SimpleAdapter adapterPhones;
        adapterPhones = new SimpleAdapter(this, contacts,
                android.R.layout.simple_list_item_2,
                new String[]{"phoneName", "phoneNumber"},
                new int[]{android.R.id.text1, android.R.id.text2});

        ListView lvPhones = (ListView) findViewById(R.id.lvPhones);
        lvPhones.setAdapter(adapterPhones);
    }

}
