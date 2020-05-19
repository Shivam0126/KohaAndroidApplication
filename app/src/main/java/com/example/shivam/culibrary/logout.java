package com.example.shivam.culibrary;

        import android.app.AlertDialog;
        import android.support.v4.app.Fragment;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;

        import java.util.HashMap;
        import java.util.Map;

        import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

/**
 * Created by shivam on 3/1/18.
 */

public class logout extends Fragment {
    public static final String LOG_OUT="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/logout.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logout,container,false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("logout");
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
        // popDialog.setView(inflater.inflate(R.layout.rat,null));
        popDialog.setMessage("Are you sure you want to logout?");
        popDialog.setTitle("LOG OUT");

        popDialog.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        confirm();

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cancel();
                            }
                        });
        AlertDialog alertDialog = popDialog.create();
        alertDialog.show();



    }

    private void confirm() {
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, LOG_OUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();




            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("userloggedin",false);
        editor.clear();
        editor.commit();
        Intent i=new Intent(getActivity(),LoginActivity.class);
        startActivity(i);
    }

    private void cancel() {
        Intent i=new Intent(getActivity(),MainActivity.class);
        startActivity(i);

    }


}

