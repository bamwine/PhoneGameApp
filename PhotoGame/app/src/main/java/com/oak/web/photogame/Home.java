package com.oak.web.photogame;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    private SessionManager session;
    private RecyclerView mRecyclerView;
    private AlbumsAdapter adapter;
    private OflineAdapter adapter2;
    private List<Photoclass> photoclassList;
    private RequestQueue requestQueue;
    private Context context;
    private Uri uri;
    DataBaseHandler db;
    private String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        session = new SessionManager(this.getApplicationContext());
         db = new DataBaseHandler(this);
        session.checkLogin();

        requestQueue = Volley.newRequestQueue(this);
        context = this;

        //populate();
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        OnlineStorage();
        //populate();
    }


    void OnlineStorage(){

        if(!SessionManager.isNetworkAvailable(this)){
            adapter2 = new OflineAdapter(this, db.getAllContacts());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter2);


        } else {

            populate();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lanch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            session.logoutUser();
        }

        if (id == R.id.upload) {
           startActivity(new Intent(this,Upload.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public  class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

        private Context mContext;
        private List<Photoclass> photoclassList;
        private ItemClickListener clickListener;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView title, downs,ups,content;
            public ImageView image;
            public Object clickListener;

            public MyViewHolder(View view) {
                super(view);

                title = (TextView) view.findViewById(R.id.card_title);
                content = (TextView) view.findViewById(R.id.card_content);
                ups = (TextView) view.findViewById(R.id.action_upvote);
                downs = (TextView) view.findViewById(R.id.action_downvote);
                image = (ImageView) view.findViewById(R.id.card_image);
               // view.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {



            }
        }


        public AlbumsAdapter(Context mContext, List<Photoclass> photoclassList) {
            this.mContext = mContext;
            this.photoclassList = photoclassList;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflator, parent, false);

            return new MyViewHolder(itemView);
        }



        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final Photoclass photoclass = photoclassList.get(position);

uri = Uri.parse(Constant.GET_SAVE_IMAGES+photoclass.getFilename());
holder.title.setText(photoclass.getTitle());
holder.content.setText(photoclass.getDescption());
holder.ups.setText(photoclass.getUpvotes()); ;
holder.downs.setText(photoclass.getDownvotes());
//holder.image.setImageURI(uri);

            Picasso.get()
                    .load(Constant.GET_SAVE_IMAGES+photoclass.getFilename())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.image);

final String users=session.getprefvalue("uname");
final String article_id=photoclass.getId();


            holder.downs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Vote(users,article_id,"n");
                }
            });

            holder.ups.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vote(users,article_id,"y");
                }
            });

            }

          @Override
        public int getItemCount() {
            return photoclassList.size();
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

    }



    public class Photoclass {
        private String uname,id;
        private String title;
        private String category;
        private String descption,location,filename,datetaken,upvotes,downvotes;

        public Photoclass(String id,String uname, String title, String category, String descption, String location, String filename, String datetaken, String upvotes, String downvotes) {
            this.id = id;
            this.uname = uname;
            this.title = title;
            this.category = category;
            this.descption = descption;
            this.location = location;
            this.filename = filename;
            this.datetaken = datetaken;
            this.upvotes = upvotes;
            this.downvotes = downvotes;
        }

        public String getId() {
            return id;
        }

        public void setid(String id) {
            this.id = id;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescption() {
            return descption;
        }

        public void setDescption(String descption) {
            this.descption = descption;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getDatetaken() {
            return datetaken;
        }

        public void setDatetaken(String datetaken) {
            this.datetaken = datetaken;
        }

        public String getUpvotes() {
            return upvotes;
        }

        public void setUpvotes(String upvotes) {
            this.upvotes = upvotes;
        }

        public String getDownvotes() {
            return downvotes;
        }

        public void setDownvotes(String downvotes) {
            this.downvotes = downvotes;
        }
    }


    public void populate() {


        photoclassList = new ArrayList<>();

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Constant.GET_App_Data, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            JSONArray jsonarray = response.getJSONArray("items");

                            Log.d("Error  jsonarray",response+"");

                            for(int i=0; i < jsonarray.length(); i++){
                                response = jsonarray.getJSONObject(i);


                                 String s = VoteCount("1", "n");

                                photoclassList.add( new Photoclass(response.getString("id"),response.getString("uname"),response.getString("title"), response.getString("category"), response.getString("descption"), response.getString("location"),response.getString("filename"),response.getString("datetaken"),VoteCount(response.getString("id"),"y"),VoteCount(response.getString("id"),"n")));

                            }

                            adapter = new AlbumsAdapter(getApplicationContext(), photoclassList);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(adapter);


                            Log.d("Error  jsonarray", photoclassList.size()+"");
                        }catch(JSONException e){e.printStackTrace();}

                        //adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        requestQueue.add(jor);
    }




    public void Vote(final String unames,String article,String vote) {


        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Constant.GET_App_Vote+"&username="+unames+"&article="+article+"&vote="+vote, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        startActivity(new Intent(context,Home.class));


                    }
    },
            new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

           // Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();

        }
    }) {

        @Override
        protected Map<String, String> getParams() {

            Map<String, String> params = new HashMap<String, String>();



            return params;
        }

    };
        requestQueue.add(jor);

}



    public String VoteCount(String articleid,String value) {


        final String[] k = new String[1];

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Constant.GET_App_Votecount+"&articleid="+articleid+"&value="+value, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try{

                            JSONArray jsonarray = response.getJSONArray("items");

                            Log.d("Error  jsonarray",response+"");

                            for(int i=0; i < jsonarray.length(); i++){
                                response = jsonarray.getJSONObject(i);
                             k[0] = a =response.getString("count");


                            }




                        } catch(JSONException e){e.printStackTrace();}


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();



                return params;
            }

        };

         requestQueue.add(jor);
        Toast.makeText(context, k[0], Toast.LENGTH_SHORT).show();

        return k[0];
    }




}
