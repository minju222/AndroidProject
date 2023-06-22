package com.scsa.andr.memo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MemoAdapter extends ArrayAdapter<MemoDTO> {
    public MemoAdapter(Context context, ArrayList<MemoDTO> memos) {
        super(context, 0, memos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoDTO memo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        title.setText(memo.getTitle());
        content.setText(memo.getContent());
        date.setText(memo.getDate());

        return convertView;
    }
}


