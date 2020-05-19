package com.example.shivam.culibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class waiting_dueAdapter extends RecyclerView.Adapter<waiting_dueAdapter.MyViewHolder> {
    private List<waiting_due> dueList;
    public Context mcontext;

    public waiting_dueAdapter(List<waiting_due> dueList, Context mcontext) {
        this.dueList = dueList;
        this.mcontext = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_due,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    final waiting_due waitingDue=dueList.get(position);
    holder.title.setText(waitingDue.getTitle());
    holder.author.setText(waitingDue.getAuthor());
    holder.duedatetextview.setText(waitingDue.getDue_Date());
    }

    @Override
    public int getItemCount() {
        return dueList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,author,duedatetextview;

        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.titles);
            author=(TextView)itemView.findViewById(R.id.author);
            duedatetextview=(TextView) itemView.findViewById(R.id.textview_duedate);

        }
    }
}
