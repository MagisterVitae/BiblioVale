package dev.sturmtruppen.bibliovale.presentationLogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import dev.sturmtruppen.bibliovale.R;
import dev.sturmtruppen.bibliovale.ResultsActivity;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;

/**
 * Created by Matteo on 18/04/2017.
 */

public class ResultsListAdapter extends BaseAdapter {
    private ArrayList<Book> listData;
    private LayoutInflater layoutInflater;

    public ResultsListAdapter(Context aContext, ArrayList<Book> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.results_list_row_layout, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.txtRowTitle);
            holder.subItem = (TextView) convertView.findViewById(R.id.txtRowAuthor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headlineView.setText(listData.get(position).getTitle());
        holder.subItem.setText(String.format("%s, %s", listData.get(position).getAuthors().get(0).getSurname(), listData.get(position).getAuthors().get(0).getName()));
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView subItem;
        // possibile aggiungere ulteriori livelli
    }
}
