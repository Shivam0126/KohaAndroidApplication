package com.example.shivam.culibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ramotion.foldingcell.FoldingCell;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

/**
 * Created by shivam on 5/1/18.
 */

public class fineadapter extends RecyclerView.Adapter<fineadapter.MyViewHolder> {
    private List<bookfine> fineList;
    public Context mcontext;
    int total=0;

    Button ren;
    public static final String URL = "http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/renew.php";
    public static final String Image_URL = "http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/";
    Button renew;

    public fineadapter(List<bookfine> fineList, Context mcontext) {
        this.fineList = fineList;
        this.mcontext = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookfine, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final bookfine booksfine = fineList.get(position);
        holder.date.setText(booksfine.getDate());
        holder.description.setText(booksfine.getDescription());
        holder.fineamount.setText(booksfine.getFineamount());



    }

    @Override
    public int getItemCount() {
        return fineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView date, description, fineamount;



        private ItemClickListener clickListener;

        public MyViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.finedate);
            description = (TextView) itemView.findViewById(R.id.description);
            fineamount = (TextView) itemView.findViewById(R.id.fineamount);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);



        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getPosition(), true);
            return true;
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}

