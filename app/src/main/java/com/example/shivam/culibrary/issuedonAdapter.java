package com.example.shivam.culibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shivam on 5/1/18.
 */

public class issuedonAdapter extends RecyclerView.Adapter<issuedonAdapter.MyViewHolder> {
    private List<books_issued> issueList;
    public Context mcontext;

    public issuedonAdapter(List<books_issued> issueList, Context mcontext) {
        this.issueList=issueList;
        this.mcontext = mcontext;
    }

    @Override
    public issuedonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.booksissued,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(issuedonAdapter.MyViewHolder holder, int position) {
       books_issued book_issued=issueList.get(position);
        holder.title.setText(book_issued.getTitle());
        holder.author.setText(book_issued.getAuthor());
        holder.issuedatetextview.setText(book_issued.getIssue_Date());

    }

    @Override
    public int getItemCount() {
        return issueList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,author,issuedatetextview;

        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.titles);
            author=(TextView)itemView.findViewById(R.id.author);
            issuedatetextview=(TextView) itemView.findViewById(R.id.textview_issuedate);

        }
    }
}
