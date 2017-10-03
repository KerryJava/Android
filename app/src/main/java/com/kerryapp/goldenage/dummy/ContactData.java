package com.kerryapp.goldenage.dummy;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.kerryapp.goldenage.R;
import com.kerryapp.goldenage.common.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mojet on 2017/9/19.
 */

public class ContactData {

    private static ContactData mInstance = null;

    private String mString;

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    public List<ContactItem> mItems = new ArrayList<>();
    public List<List<ContactItem>> mCatergoryItems = new ArrayList<List<ContactItem>>();
    public List<DummyContent.DummyItem> mDummyItems = new ArrayList<>();
    public List<ContactItem> mWholeItems = new ArrayList<>();
    public List<ContactItem> mRemoteItems = new ArrayList<>();


    private ContactData() {
        mString = "Hello";
    }

    public static ContactData getInstance() {
        if (mInstance == null) {
            mInstance = new ContactData();
        }
        return mInstance;
    }

    public String getString() {
        return this.mString;
    }

    public void setString(String value) {
        mString = value;
    }

    //get display data
    public List<DummyContent.DummyItem> fetchDummyItems(int listType) {
        List<ContactData.ContactItem> list;
        mDummyItems.clear();

        switch (listType) {
            case 0:
                list = mInstance.mItems;
                for (ContactData.ContactItem item :
                        list) {
                    DummyContent.DummyItem dummyItem = new DummyContent.DummyItem(item.name, " 字开头" + " 数量  " + item.phonestr);
                    mDummyItems.add(dummyItem);
                }
                break;
            default:
                list = mInstance.mCatergoryItems.get(listType - 1);
                for (ContactData.ContactItem item :
                        list) {
                    String content;
                    if (item.phonestr.startsWith("0")) {
                        content = item.phonestr;
                    }
                    else {
                        content = item.phonestr.substring(0,3) + " " + item.phonestr.substring(3);
                    }

                    DummyContent.DummyItem dummyItem = new DummyContent.DummyItem(item.name, content);
                    mDummyItems.add(dummyItem);
                }
                break;
        }

        return mDummyItems;
    }

    //获取通讯录
    public List<Map<String, Object>> getContacts(Context context) {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> catergorylistItems = new ArrayList<Map<String, Object>>();

        Map<String, Object> wholelistItem = new HashMap<String, Object>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String phoneName;
            String phoneNumber;
            Map<String, Object> listItem = new HashMap<String, Object>();
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            listItem.put(Global.NAME_KEY, phoneName);
            listItem.put(Global.NUM_KEY, phoneNumber);
            listItems.add(listItem);

            ContactItem item = new ContactItem(phoneName, phoneNumber);
            mWholeItems.add(item);

            List<ContactItem> list;
            String firstLetter = phoneName.toLowerCase().substring(0, 1);
            if (wholelistItem.containsKey(firstLetter)) {
                Log.d(Global.DEBUG_TAG, firstLetter);
                list = (List<ContactItem>) wholelistItem.get(firstLetter);
            } else {
                list = new ArrayList<ContactItem>();
                wholelistItem.put(firstLetter, list);
            }

            list.add(item);
//            list.add(listItem);
        }

        mCatergoryItems.clear();
        mItems.clear();

        for (Map.Entry<String, Object> entry : wholelistItem.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            Map<String, Object> returnlistItem = new HashMap<String, Object>();
            returnlistItem.put(Global.NAME_KEY, entry.getKey());
            List<ContactItem> target = (List<ContactItem>) entry.getValue();
            returnlistItem.put(Global.NUM_KEY, target.size());
            catergorylistItems.add(returnlistItem);

            ContactItem item = new ContactItem(entry.getKey(), "" + target.size());
            mItems.add(item);
            mCatergoryItems.add(target);
        }

        return catergorylistItems;
//        return listItems;
    }

    //写入通讯录
    public void writeContacts(Context context, String name, String phoneNumber) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值

        ContentResolver resolver = context.getContentResolver();

        Uri rawContactUri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

//        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
//        // 联系人的Email地址
//        values.put(ContactsContract.CommonDataKinds.Email.DATA, "zhangphil@xxx.com");
//        // 电子邮件的类型
//        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
//        // 向联系人Email URI添加Email数据
//        resolver.insert(ContactsContract.Data.CONTENT_URI, values);

        Toast.makeText(context, R.string.addContactSuccess, Toast.LENGTH_SHORT).show();

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

    public void WriteDataFromRemote(Context context) {
        for (ContactItem item: mRemoteItems
             ) {
            writeContacts(context, item.name, item.phonestr);
        }
    }

    public static class ContactItem {
        public String name;
        public String phonestr;
        public long phone;


        public ContactItem(String id, String phonestr) {
            this.name = id;
            this.phonestr = phonestr;
            this.phone = Long.parseLong(phonestr.replace("-","").replaceAll("\\s*", "").trim());
        }

        public ContactItem(String id, int phone) {
            this.name = id;
            this.phonestr = "" + phone;
            this.phone = phone;
        }

        @Override
        public String toString() {
            return phonestr;
        }
    }
}
