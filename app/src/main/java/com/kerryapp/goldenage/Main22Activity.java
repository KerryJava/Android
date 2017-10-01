package com.kerryapp.goldenage;

import android.app.Activity;
import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main22Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        final ListView listview = (ListView) findViewById(R.id.listView);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

//        MySimpleArrayAdapter adapter2 = new MySimpleArrayAdapter(this, values);
//        listview.setAdapter(adapter2);

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
//        listview.setAdapter(adapter);

        final  MyPerformanceArrayAdapter adaper3 = new MyPerformanceArrayAdapter(this, values);
        listview.setAdapter(adaper3);

//        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                final View selectView = view;
//                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(2000).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                list.remove(item);
//                                adapter.notifyDataSetChanged();
//                                selectView.setAlpha(1);
//                            }
//                        });
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.d("NO thing myapp", "onNothingSelected ");
//            }
//        });

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view,
//                                    int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(2000).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                list.remove(item);
//                                adapter.notifyDataSetChanged();
//                                view.setAlpha(1);
//                            }
//                        });
//            }
//
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main22, menu);
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

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public MySimpleArrayAdapter(Context context, String[] values) {
            super(context, R.layout.rowlayout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.label);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            textView.setText(values[position]);
            // change the icon for Windows and iPhone
            String s = values[position];
            if (s.startsWith("iPhone")) {
                imageView.setImageResource(R.drawable.dxng_intro);
            } else {
                imageView.setImageResource(R.drawable.open_icon_dgzq);
            }

            return rowView;
        }
    }

    public class MyPerformanceArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private String[] names;
        private HashMap<Integer, Boolean> mIdMap = new HashMap<>();

        class ViewHolder {
            public TextView text;
            public ImageView image;
            public CheckBox checkbox;
        }

        public MyPerformanceArrayAdapter(Context context, String[] names) {
            super(context, R.layout.rowlayout, names);
            this.context = context;
            this.names = names;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.rowlayout, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.label);
                viewHolder.image = (ImageView) rowView
                        .findViewById(R.id.icon);
                viewHolder.checkbox = (CheckBox)rowView.findViewById(R.id.check);
                rowView.setTag(viewHolder);
                viewHolder.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("my app", "onClick ");
                    }
                });
//                viewHolder.text.setClickable(false);



//                viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                        if (mIdMap.containsKey(position)) {
//                            mIdMap.remove(position);
//                        } else {
//                            mIdMap.put(position, isChecked);
//                        }
//                        Log.d("my app", "onCheckedChanged ");
//                    }
//                });
            }

            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();
            String s = names[position];
            holder.text.setText(s);
            if (s.startsWith("Windows7") || s.startsWith("iPhone")
                    || s.startsWith("Solaris")) {
                holder.image.setImageResource(R.drawable.jietaobao_package);
            } else {
                holder.image.setImageResource(R.drawable.open_icon_dgzq);
            }

            boolean isCheck;

            isCheck = mIdMap.containsKey(position);

            holder.checkbox.setOnClickListener(null);
            holder.checkbox.setChecked(isCheck);

            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIdMap.containsKey(position)) {
                        mIdMap.remove(position);
                    } else {
                        mIdMap.put(position, true);
                    }
                    Log.d("my app", "setOnClickListener ");
                }
            });


//            rowView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    v.animate().setDuration(2000).alpha(0)
//                            .withEndAction(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    ArrayList temp = new ArrayList();
//
//                                    for (int i = 0 ; i < names.length; i++)
//                                    {
//                                        temp.add(i, names[i]);
//                                    }
//                                    temp.remove(position);
//                                    names = (String[])temp.toArray(new String[temp.size()]);
////                                    list.remove(item);
//                                    notifyDataSetInvalidated();
//                                    notifyDataSetChanged();
//                                    v.setAlpha(1);
//
//                                }
//                            });
//
//                    final int pos = position;
//                    Toast.makeText(getApplicationContext(),
//                            "Click ListItem Number " + pos, Toast.LENGTH_LONG)
//                            .show();
//
//                }
//            });

            return rowView;
        }
    }

}
