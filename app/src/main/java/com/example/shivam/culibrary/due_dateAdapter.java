package com.example.shivam.culibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
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

public class due_dateAdapter extends RecyclerView.Adapter<due_dateAdapter.MyViewHolder> {
    private List<books_Due> dueList;
    public Context mcontext;

    Button ren;
    public static final String URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/renew.php";
    public static final String Image_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/";
    Button renew;

    public due_dateAdapter(List<books_Due> dueList, Context mcontext) {
        this.dueList = dueList;
        this.mcontext = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.books_due,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final books_Due booksDue=dueList.get(position);
        holder.title.setText(booksDue.getTitle());
        holder.author.setText(booksDue.getAuthor());
        holder.duedatetextview.setText(booksDue.getDue_Date());
        holder.title2.setText(booksDue.getTitle());
        holder.duedatetextview2.setText(booksDue.getDue_Date());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                if(isLongClick)
                {
                    Toast.makeText(mcontext, "click it once", Toast.LENGTH_SHORT).show();
                }else
                {
                    holder.fc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            holder.fc.toggle(false);
                            holder.fc.initialize(1000, Color.rgb(48,144,199), 1);
// or with camera height parameter
                            holder.fc.initialize(30, 1000,Color.rgb(48,144,199), 1);
                        }
                    });
                }
                // attach click listener to folding cell

            }
        });
        String load=Image_URL+booksDue.getImage();
        java.net.URL url = null;
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

        holder.img.setImageBitmap(bmp);

    }

    @Override
    public int getItemCount() {
        return dueList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView title,author,duedatetextview,title2,author2,duedatetextview2;
        ImageView img;
        Button renewbook;
        FoldingCell fc;

        private ItemClickListener clickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            fc = (FoldingCell) itemView.findViewById(R.id.folding_cell1);
            //renewbook=(Button)itemView.findViewById(R.id.renewbt);
            title=(TextView) itemView.findViewById(R.id.titles);
            author=(TextView)itemView.findViewById(R.id.author);
            duedatetextview=(TextView) itemView.findViewById(R.id.textview_duedate);
            title2=(TextView) itemView.findViewById(R.id.titles2);
            duedatetextview2=(TextView) itemView.findViewById(R.id.textview_duedate2);
            img=(ImageView) itemView.findViewById(R.id.img);
            Button renew=(Button)itemView.findViewById(R.id.renewbook);

            // set custom parameters


            renew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datasend(title.getText().toString());
                }
            });
            //renew=(Button)itemView.findViewById(R.id.renew);

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

    private void datasend(final String s) {
        SharedPreferences sharedPreferences=mcontext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                Toast.makeText(mcontext
                        , response, Toast.LENGTH_SHORT).show();

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mcontext,"Network Error",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                params.put("title",s);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(stringRequest);
    }
}