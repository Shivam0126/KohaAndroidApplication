package com.example.shivam.culibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Map;

import static com.example.shivam.culibrary.LoginActivity.MyPREFERENCES;

/**
 * Created by shivam on 3/1/18.
 */

public class suggest_books extends Fragment {
    EditText Title,Author,note;
    Button submit;
    public static final String SUGGESTION_URL="http://ec2-13-127-183-173.ap-south-1.compute.amazonaws.com/culibrary/suggestion.php";

    ProgressDialog progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestbooks,container,false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Suggest books");

        Title=(EditText)view.findViewById(R.id.title1);
        Author=(EditText)view.findViewById(R.id.author1);
        note=(EditText)view.findViewById(R.id.note);
        submit=(Button)view.findViewById(R.id.subsuggest);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ti1,au1,no1;
                ti1=Title.getText().toString();
                au1=Author.getText().toString();
                no1=note.getText().toString();
                senddata(ti1,au1,no1);
            }
        });


    }

    private void senddata(final String ti1, final String au1, final String no1) {
       SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username=sharedPreferences.getString("user","");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, SUGGESTION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                if(response.equals("success"))
                {
                    Toast.makeText(getActivity(), "Suggestion has been recorded for furthur process", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Technical issue,Try again later!", Toast.LENGTH_SHORT).show();
                }




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
                params.put("title",ti1);
                params.put("author",au1);
                params.put("note",no1);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}