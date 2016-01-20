package com.forewarn.forewarn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by persinme on 11/2/2015.
 */

public class MyCustomAdapter extends ArrayAdapter<StringList> {

    private LinkedList<StringList> stringList;

    public MyCustomAdapter(Context context, int textViewResourceId, LinkedList<StringList> stateList)
    {
        super(context, textViewResourceId, stateList);
        this.stringList = new LinkedList<StringList>();
        this.stringList.addAll(stateList);
    }

    private class ViewHolder
    {
        TextView text;
        CheckBox box;
    }

    public void updateStringList(LinkedList<StringList> list) {
        this.stringList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = null;

        if (convertView == null)
        {

            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.list_items, null);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.code);
            holder.box = (CheckBox) convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);

            holder.box.setOnClickListener( new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    StringList list = (StringList) cb.getTag();
                    list.changeSelected();
                    //Update the global list
//                        System.out.println("Checkbox was selected");
//                        System.out.println(list.getSelected());
                }
            });

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        StringList sList = this.stringList.get(position);
        holder.text.setText(sList.getName());
        holder.box.setChecked(sList.getSelected());
        holder.box.setTag(sList);

        return convertView;
    }

}
