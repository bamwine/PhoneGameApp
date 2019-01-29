package com.oak.web.photogame;


import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class UploadService extends Service {


    private SessionManager session;
    DataBaseHandler db;
    private Context context;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        OnlineStorage();
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
void OnlineStorage(){
    if(!SessionManager.isNetworkAvailable(this)) {
        session = new SessionManager(getApplicationContext());
        db = new DataBaseHandler(getApplicationContext());
        List<Photoclass> contacts = db.getAllcollectionPhotos();

        for (Photoclass cn : contacts) {
            File  selectedFilePath= session.bytetofle(cn.getFilename());
            uploadFile2(selectedFilePath, cn.getUname(), cn.getDatetaken(), cn.getLocation(), cn.getDescption(), cn.getTitle(), cn.getCategory());

        }
    }

}

    public int uploadFile2(File selectedFilePath,String username,String datetaken,String  location, String description, String title,  String category ) {



        FileApi service = RetroClient.getApiService();

       // File file = new File(selectedFilePath);
        File file =  selectedFilePath;

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("passport", file.getName(), requestFile);

        RequestBody unames = createPartFromString(username);
        RequestBody titles = createPartFromString(title);
        RequestBody categorys = createPartFromString(category);
        RequestBody descptions = createPartFromString(description);

        RequestBody locations = createPartFromString(location);
        RequestBody datetakens = createPartFromString(datetaken);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("uname", unames);
        map.put("title", titles);
        map.put("category", categorys);
        map.put("descption", descptions);
        map.put("location", locations);
        map.put("datetaken", datetakens);

        Call<Respond> resultCall = service.uploadImage(map,body);

        resultCall.enqueue(new Callback<Respond>() {
            @Override
            public void onResponse(Call<Respond> call, Response<Respond> response) {



                // Response Success or Fail
                if (response.isSuccessful()) {
                    if (response.body().getError()==true)

                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        // here file could not move too big
                    else
                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {
               // progressDialog.dismiss();
            }
        });

        return 0;
    }

    private RequestBody createPartFromString(String valueString) {

        return RequestBody.create( MultipartBody.FORM,valueString);
    }


    public interface FileApi {


        @Multipart
        @POST("index.php")
        Call<Respond> uploadImage(
                @PartMap() Map<String, RequestBody> partMap,
                @Part MultipartBody.Part file);
    }

    public class Respond {

        private String message;
        private Boolean error;

        public String getMessage() {
            return message;
        }


        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

    }

    public static class RetroClient {


        public RetroClient() {

        }

        private static Retrofit getRetroClient() {
            return new Retrofit.Builder()
                    .baseUrl(Constant.GET_SAVE_DATA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        public static FileApi getApiService() {
            return getRetroClient().create(FileApi.class);
        }
    }




}
