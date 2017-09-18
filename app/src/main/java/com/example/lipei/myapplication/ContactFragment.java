package com.example.lipei.myapplication;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private Button btnAdd;
    private ListView lvPhones;
    private TextView tvPhoneName;
    private TextView tvPhoneNumber;
    private EditText edtPhone;

    private List<Map<String, Object>> ContactsList;  //存储所有通讯录信息

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        lvPhones = (ListView) v.findViewById(R.id.lvPhones);
        btnAdd = (Button) v.findViewById(R.id.btnAdd);      //添加

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //显示联系人
        InitData();
        request();

        Gson gson=new Gson();
        gson.toJson(null);

        //添加联系人
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getResources().getString(R.string.addContact));
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.contact_add, null);

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
                            Toast.makeText(getContext(), "电话号码为空，添加失败!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String telRegex = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";  //
                        if (!edtPhone.getText().toString().matches(telRegex)) {
                            Toast.makeText(getContext(), "请重新输入正确的电话号码，添加失败!", Toast.LENGTH_SHORT).show();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getResources().getString(R.string.qxz));
                final String[] contactFun = new String[]{getResources().getString(R.string.callPhone), getResources().getString(R.string.sendMessage)};
                builder.setItems(contactFun, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strFun = contactFun[i];
                        if (strFun.equals(getResources().getString(R.string.callPhone))) {
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            //startActivity(phoneIntent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber + ""));
                            intent.putExtra("sms_body", "");
                            //startActivity(intent);
                        }
                    }
                });
                //取消操作
                builder.setNegativeButton(getResources().getString(R.string.btnCancel), null);
                builder.show();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //写入通讯录
    public void writeContacts(String name, String phoneNumber) {


        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值

        ContentResolver resolver = getActivity().getContentResolver();

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

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        // 联系人的Email地址
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "zhangphil@xxx.com");
        // 电子邮件的类型
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);

        Toast.makeText(getContext(), "联系人数据添加成功", Toast.LENGTH_SHORT).show();

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
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
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
        adapterPhones = new SimpleAdapter(getContext(), contacts,
                android.R.layout.simple_list_item_2,
                new String[]{"phoneName", "phoneNumber"},
                new int[]{android.R.id.text1, android.R.id.text2});

        lvPhones.setAdapter(adapterPhones);
    }

    public void request(){
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request

//        RequestBody formBody = new FormBody.Builder()
//                .add("method", "Other.GetWithSize")
//                .add("name", "bug")
//                .build();
        Gson json = new Gson();

        Hashtable subTable = new Hashtable();
        subTable.put("size", "200");
        subTable.put("name", "home");

        Hashtable table = new Hashtable();
        table.put("method", "Banner.GetWithSize");
        table.put("jsonrpc", "2.0");
        table.put("id", "54321");
        table.put("params",subTable);

        String jsonStr = json.toJson(table);

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);

        final String url = "http://app.api.gupiaoxianji.com/v3.8ios";
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-type", "application/json")
                .post(requestBody)
                .build();

        Call mcall= mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("wangshu", "onFailure---" + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String bodyStr = response.body().toString();

                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.d("wangshu", "cache---" + str);
                } else {
                    Log.d("wangshu", "network---" + bodyStr);
//                    final String cacheStr = response.cacheResponse().body().toString();
                    Log.d("wangshu", "bodyStr---" + bodyStr + " net work ");
//                    Log.d("wangshu", "cacheStr---" + cacheStr);


//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final String str = "body" + bodyStr;
//                            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
                getActivity().runOnUiThread(new Runnable() {
//                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
}
}
