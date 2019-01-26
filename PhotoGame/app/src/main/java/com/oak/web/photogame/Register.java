package com.oak.web.photogame;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Register extends Fragment {


    private RequestQueue requestQueue;
    private SessionManager session;
    private EditText email,Uname;
    Button save;
    int flag=0;
    private static final String TAG_SUCCESS = "success";
    private Fragment fragment;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view2=inflater.inflate(R.layout.register, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        session = new SessionManager(getActivity());

        Uname = (EditText) view2.findViewById(R.id.Uname);
        email = (EditText) view2.findViewById(R.id.email);
        save = (Button) view2.findViewById(R.id.button2);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid();

            }
        });

        return view2;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("");

    }



    void valid(){


        if (email.getText().toString().trim().length() > 0 && Uname.getText().toString().trim().length() > 0) {
            // login user
            savedatadb(Uname.getText().toString(),email.getText().toString());

        } else {
            // Prompt user to enter credentials
            Toast.makeText(getContext(),
                    "Please enter the credentials!", Toast.LENGTH_LONG)
                    .show();

        }

    }


    public void savedatadb(final String unames, final String emails) {



        StringRequest jor = new StringRequest(Request.Method.POST, Constant.GET_USER_REG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(TAG_SUCCESS);
                    String mesage=jObj.getString("message");
                    if (success == 1)
                    {
                        Toast.makeText(getContext(),mesage, Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        flag=0;
                        Toast.makeText(getContext(),mesage, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Username", unames);
                params.put("email", emails);

                return params;
            }

        };


        requestQueue.add(jor);

        Uname.setText("");
        email.setText("");
    }







}