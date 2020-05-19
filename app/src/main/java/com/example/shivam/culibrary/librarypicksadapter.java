package com.example.shivam.culibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class librarypicksadapter extends RecyclerView.Adapter<librarypicksadapter.MyViewHolder> {
    private List<librarypicks> libpics;
    public Context mcontext;
    public static final String URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/renew.php";
    public static final String Image_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/";

    public librarypicksadapter(List<librarypicks> libpics, Context mcontext) {
        this.libpics = libpics;
        this.mcontext = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.librarypics,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final librarypicks lib = libpics.get(position);
        holder.title.setText(lib.getTitle());
        holder.author.setText(lib.getAuthor());
        //holder.images.setText(librarypicks.getImage());

        String load = Image_URL + lib.getImage();
        URL url = null;
        try {
            url = new URL(load);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.images.setImageBitmap(bmp);
        holder.setClickListener(new ItemClickListener() {

            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                String sendtitle=lib.getTitle();
                Intent k=new Intent(mcontext,SearchResult.class);
                k.putExtra("bookname",sendtitle);
                mcontext.startActivity(k);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libpics.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView title,author;
        public ImageView images;
        private librarypicksadapter.ItemClickListener clickListener;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.booktitle);
            author=(TextView)itemView.findViewById(R.id.bookauthor);
            images=(ImageView) itemView.findViewById(R.id.bookimage);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


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

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
    }



    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
