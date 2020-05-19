package com.example.shivam.culibrary;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class support extends Fragment implements View.OnClickListener{

    String q="Question",idea="Idea",is="Issue";

    EditText q1,q2,q3;
    ExpandableRelativeLayout expandableLayout1,expandableLayout2,expandableLayout3;
    Button expandableButton1,expandableButton2,expandableButton3,submitq,submitis,submitid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_support,container,false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("support");

        q1=(EditText)view.findViewById(R.id.q1);
        q2=(EditText)view.findViewById(R.id.q2);
        q3=(EditText)view.findViewById(R.id.q3);

        expandableButton1=(Button)view.findViewById(R.id.expandableButton1);
        expandableButton2=(Button)view.findViewById(R.id.expandableButton2);
        expandableButton3=(Button)view.findViewById(R.id.expandableButton3);

        expandableLayout1 = (ExpandableRelativeLayout)view.findViewById(R.id.expandableLayout1);
        expandableLayout2 = (ExpandableRelativeLayout)view.findViewById(R.id.expandableLayout2);
        expandableLayout3 = (ExpandableRelativeLayout)view.findViewById(R.id.expandableLayout3);

        expandableButton1.setOnClickListener(support.this);
        expandableButton2.setOnClickListener(support.this);
        expandableButton3.setOnClickListener(support.this);

        submitq=(Button)view.findViewById(R.id.submitquestion);
        submitid=(Button)view.findViewById(R.id.submitidea);
        submitis=(Button)view.findViewById(R.id.submitissue);

        submitq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] to = {"abhishek.joshi@science.chrituniversity.in"};
                    String[] cc = {"shivam.gupta@science.christuniversity.in"};
                    String subject = q;
                    String message = q1.getText().toString();
                    Intent eMailIntent = new Intent(Intent.ACTION_SEND);
                    eMailIntent.setData(Uri.parse("mailto:"));
                    eMailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    eMailIntent.putExtra(Intent.EXTRA_CC, cc);
                    eMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    eMailIntent.putExtra(Intent.EXTRA_TEXT, message);
                    eMailIntent.setType("message/rfc822");
                    startActivity(eMailIntent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Email failed to send", Toast.LENGTH_LONG).show();
                }
                q1.setText("");
            }
        });
        submitid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] to = {"abhishek.joshi@science.chrituniversity.in"};
                    String[] cc = {"shivam.gupta@science.christuniversity.in"};
                    String subject = is;
                    String message = q2.getText().toString();
                    Intent eMailIntent = new Intent(Intent.ACTION_SEND);
                    eMailIntent.setData(Uri.parse("mailto:"));
                    eMailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    eMailIntent.putExtra(Intent.EXTRA_CC, cc);
                    eMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    eMailIntent.putExtra(Intent.EXTRA_TEXT, message);
                    eMailIntent.setType("message/rfc822");
                    startActivity(eMailIntent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Email failed to send", Toast.LENGTH_LONG).show();
                }
                q2.setText("");
            }
        });

        submitis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] to = {"abhishek.joshi@science.christuniversity.in"};
                    String[] cc = {"shivam.gupta@science.christuniversity.in"};
                    String subject = idea;
                    String message = q3.getText().toString();
                    Intent eMailIntent = new Intent(Intent.ACTION_SEND);
                    eMailIntent.setData(Uri.parse("mailto:"));
                    eMailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    eMailIntent.putExtra(Intent.EXTRA_CC, cc);
                    eMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    eMailIntent.putExtra(Intent.EXTRA_TEXT, message);
                    eMailIntent.setType("message/rfc822");
                    startActivity(eMailIntent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Email failed to send", Toast.LENGTH_LONG).show();
                }
                q3.setText("");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.expandableButton1:expandableLayout1.toggle();
                expandableLayout2.collapse();
                expandableLayout3.collapse();
                break;

            case R.id.expandableButton2:expandableLayout2.toggle();
                expandableLayout1.collapse();
                expandableLayout3.collapse();
                break;

            case R.id.expandableButton3:expandableLayout3.toggle();
                expandableLayout2.collapse();
                expandableLayout1.collapse();
                break;
        }


    }
}