package com.oak.web.photogame;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Login extends Fragment {


    private RequestQueue requestQueue;
    private SessionManager session;
    private EditText Uname;
    Button save;
    int flag=0;
    private Fragment fragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view2=inflater.inflate(R.layout.login, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        session = new SessionManager(getActivity());

        Uname = (EditText) view2.findViewById(R.id.Uname);

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
        if (Uname.getText().toString().length()<0 ) {
            Uname.requestFocus();
        }
        else{



            populate(Uname.getText().toString()); }
    }


    public void populate(final String unames) {


        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Constant.GET_USER_LOGIN+"&Username="+unames, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            JSONArray jsonarray = response.getJSONArray("items");

                            Log.d("Error  jsonarray",response+"");

                            for(int i=0; i < jsonarray.length(); i++){
                                response = jsonarray.getJSONObject(i);
                      session.createLoginSession(response.getString("id"), response.getString("uname"), response.getString("email"));

                             startActivity(new Intent(getActivity(),Home.class));

                            }

                        }catch(JSONException e){e.printStackTrace();}


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration Error: " + error.getMessage());
                        Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
               // params.put("Username", unames);


                return params;
            }

        };
        requestQueue.add(jor);
        Uname.setText("");
    }




}




