package com.example.shivam.culibrary;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivam on 4/1/18.
 */

public class locationAdapter extends RecyclerView.Adapter<locationAdapter.MyViewHolder> {
    private List<location> locationList;
    location loc;
    int x=0;
    ImageView expand;
    RelativeLayout rel;
    Button b;
    public Context mcontext;
    public static final String URL2="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/waiting.php";
    public static final String RESERVE_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/reservation.php";
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       loc =locationList.get(position);
        holder.campus.setText(loc.getCampus());
        holder.locate.setText(loc.getLocate());
        holder.status.setText(loc.getStatus());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(mcontext,loc.getLocate(),Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(mcontext,loc.getStatus(),Toast.LENGTH_SHORT).show();
                    expand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (rel.getVisibility() == View.VISIBLE)
                            {
                                rel.setVisibility(View.INVISIBLE);
                                expand.setImageResource(android.R.drawable.arrow_down_float);
                            }
                            else
                            {
                                rel.setVisibility(View.VISIBLE);
                                expand.setImageResource(android.R.drawable.arrow_up_float);
                            }
                        }
                    });

                }
            }
        });
        if(loc.getStatus().equals("Available"))
        {
            holder.relativeLayout.setBackgroundColor(Color.argb(200,130,224,170));
            b.setText("Reserve book");
        }
        else if (loc.getStatus().equals("Unavailable"))
        {
            holder.relativeLayout.setBackgroundColor(Color.argb(200,241,148,138));
            b.setText("Waiting list");
        }

    }

    private void addNotification() {
        NotificationCompat.Builder builder=(NotificationCompat.Builder)new NotificationCompat.Builder(mcontext).setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle("Book reserved").setContentText("You have 30 minutes to issue the book");

        Intent notificationIntent=new Intent(mcontext,home.class);
        PendingIntent contentIntent=PendingIntent.getActivity(mcontext,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager=(NotificationManager)mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }

    private void addReservelist(final String campus) {
        
        SearchResult sr= new SearchResult();

        final String username;//=sr.add_user();
        final String ISBN;//=sr.add_ISBN();
        final Context ctx=sr.getBaseContext();
        SharedPreferences sharedpreferences = this.mcontext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username= sharedpreferences.getString("user","default");
        ISBN= sharedpreferences.getString("ISBN","default");
       // Toast.makeText(mcontext, ISBN, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,RESERVE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                Toast.makeText(mcontext, response, Toast.LENGTH_SHORT).show();
                if(response.equals("Successfully Reserved"))
                {
                    x=x+1;
                }

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
                params.put("ISBN",ISBN);
                params.put("campus",loc.getCampus());
                return params;
            }
        };
        MySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }


    private void addwaitingList() {
        SearchResult sr= new SearchResult();

        final String username;//=sr.add_user();
        final String ISBN;//=sr.add_ISBN();
        final Context ctx=sr.getBaseContext();
        SharedPreferences sharedpreferences = this.mcontext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username= sharedpreferences.getString("user","default");
        ISBN= sharedpreferences.getString("ISBN","default");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                Toast.makeText(mcontext, response, Toast.LENGTH_SHORT).show();
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
                params.put("ISBN",ISBN);

                return params;
            }
        };
        MySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView campus,locate,status;
        public RelativeLayout relativeLayout;
        private ItemClickListener clickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            campus=(TextView) itemView.findViewById(R.id.campus);
            locate=(TextView) itemView.findViewById(R.id.locate);
            status=(TextView) itemView.findViewById(R.id.status);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relative_row);
            expand=(ImageView) itemView.findViewById(R.id.expand);
            rel=(RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            b=(Button) itemView.findViewById(R.id.reservebutton);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (loc.getStatus().equals("Available")) {
                        final AlertDialog.Builder popDialog = new AlertDialog.Builder(mcontext);
                        // popDialog.setView(inflater.inflate(R.layout.rat,null));
                        popDialog.setMessage("Do you want to reserve the book");
                        popDialog.setTitle("Confirm");

                        popDialog.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (x == 0) {
                                            addReservelist(loc.getCampus());
                                            addNotification();


                                            dialog.cancel();
                                        }else {
                                            Toast.makeText(mcontext, "You already have one book reserved", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alertDialog = popDialog.create();
                        alertDialog.getWindow().setType(WindowManager.LayoutParams.
                                TYPE_SYSTEM_ALERT);
                        alertDialog.show();

                    } else if (loc.getStatus().equals("Unavailable")) {

                        final AlertDialog.Builder popDialog = new AlertDialog.Builder(mcontext);
                        // popDialog.setView(inflater.inflate(R.layout.rat,null));
                        popDialog.setMessage("Do you want to put your name on waiting list");
                        popDialog.setTitle("Confirm");
                        popDialog.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        addwaitingList();


                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        AlertDialog alertDialog = popDialog.create();
                        alertDialog.getWindow().setType(WindowManager.LayoutParams.
                                TYPE_SYSTEM_ALERT);
                        alertDialog.show();
                    }
                }
            });

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
    public locationAdapter(List<location> locationList,Context context) {
        this.locationList = locationList;
        this.mcontext=context;
    }
    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
