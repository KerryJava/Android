package com.example.lipei.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mListType = 0;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<DummyItem> mItems = new ArrayList<DummyItem>();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mListType = getArguments().getInt(ARG_LIST_TYPE);

            ContactData data = ContactData.getInstance();
            data.getContacts(getActivity());

            List<ContactData.ContactItem> list;

            switch (mListType) {
                case 0:
                    list = data.mItems;
                    for (ContactData.ContactItem item :
                            list) {
                        DummyItem dummyItem = new DummyItem(item.id, " 字开头" + " 数量  " + item.content);

                        mItems.add(dummyItem);
                    }
                    break;
                default:
                    list = data.mCatergoryItems.get(mListType - 1);
                    for (ContactData.ContactItem item :
                            list) {
                        DummyItem dummyItem = new DummyItem(item.id, item.content);
                        mItems.add(dummyItem);
                    }
                    break;
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_contact, container, false);

        View targetView = view.findViewById(R.id.contact_list);
        // Set the adapter
        if (targetView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) targetView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(mItems, mListener));
        }
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
}
