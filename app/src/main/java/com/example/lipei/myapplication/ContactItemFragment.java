package com.example.lipei.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lipei.myapplication.dummy.ContactData;
import com.example.lipei.myapplication.dummy.DummyContent;
import com.example.lipei.myapplication.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ContactItemFragment extends Fragment {

    public static final String ARG_LIST_TYPE = "ARG_LIST_TYPE";
    public static final String  ARG_CALLBACK = "ARG_CALLBACK";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mListType = 0;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<DummyItem> mItems = new ArrayList<DummyItem>();
    private Button btnAdd;
    private MyItemRecyclerViewAdapter adapter;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ContactItemFragment newInstance(int type) {
        ContactItemFragment fragment = new ContactItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mListType = getArguments().getInt(ARG_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_contact, container, false);

        View targetView = view.findViewById(R.id.contact_list);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);      //添加

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

                        ContactData contactData = ContactData.getInstance();
                        contactData.writeContacts(getActivity(), strName, strPhone);    //添加联系人
                        contactData.getContacts(getActivity());
                        updateList();
                    }
                });

                //取消操作
                builder.setNegativeButton(getResources().getString(R.string.btnCancel), null);

                builder.show();

            }
        });

        // Set the adapter
        if (targetView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) targetView;
            mRecyclerView = recyclerView;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyItemRecyclerViewAdapter(mItems, mListener);
            recyclerView.setAdapter(adapter);
        }

        ContactData data = ContactData.getInstance();
        data.getContacts(getActivity());
        updateList();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item, int position);
    }

    public void updateList() {
        mItems.clear();
        mItems.addAll(ContactData.getInstance().fetchDummyItems(mListType));
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void updateList2() {

//        ((MyItemRecyclerViewAdapter)mRecyclerView.getAdapter()).setmValues(mItems);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
