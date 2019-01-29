package com.oak.web.photogame;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

public class Upload extends AppCompatActivity {

    EditText title,description,location;
    Spinner categ;
    ImageView imageview;
    private int  CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/PhotoGame";
    Button save;
    private RequestQueue requestQueue;
    private String selectedFilePath;
    private Context context;
    private SessionManager session;
    private String datetaken;
    byte imageInByte[];
    DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caputer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(this.getApplicationContext());
        imageview = (ImageView) findViewById(R.id.iv);
        location = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        title = (EditText) findViewById(R.id.title);
        categ = (Spinner) findViewById(R.id.spinner);
        save = (Button) findViewById(R.id.button2);
         db = new DataBaseHandler(this);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category;
                if(location.getText().toString().trim().length()>0
                && description.getText().toString().trim().length()>0
                && title.getText().toString().trim().length()>0
                 && categ.getSelectedItem().toString().trim().length()>0
                        ){
                    category=categ.getSelectedItem().toString();

                //uploadFile2(selectedFilePath,location,description,title,category);

                    OnlineStorage(selectedFilePath,location,description,title,category);
                }



                else {

                    Toast.makeText(getApplicationContext(),"Please enter some fileds!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

void OnlineStorage(String selectedFilePath, EditText location, EditText description, EditText title,  String category ){

        if(!SessionManager.isNetworkAvailable(this)){
            Log.d("Insert: ", "Inserting ..");
             db.addphoto(new Photoclass("0", session.getprefvalue("uname"), title.getText().toString(), category, description.getText().toString(), location.getText().toString(), imageInByte, datetaken, "0", "0"));
              setTonull();
            Toast.makeText(getApplicationContext(),"uploaded successfully", Toast.LENGTH_LONG).show();


        } else {

            uploadFile2(selectedFilePath,location,description,title,category);
        }
}

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
       if (requestCode == CAMERA) {
       // if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
          // Uri selectedFileUri = data.getData();
                   imageview.setImageBitmap(thumbnail);

           selectedFilePath = saveImage(thumbnail);;
           Log.i("filepath", "Selected File Path:" + selectedFilePath);

// convert bitmap to byte
           ByteArrayOutputStream stream = new ByteArrayOutputStream();
           thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imageInByte = stream.toByteArray();


        }
    }


    public String saveImage(Bitmap myBitmap) {
        File f = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {


            Date date=Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
            datetaken=dateFormat.format(date);


           // Toast.makeText(Upload.this, formattedDate, Toast.LENGTH_SHORT).show();

            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");

            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return f.getAbsolutePath();
    }



    public int uploadFile2(String selectedFilePath, EditText location, EditText description, EditText title,  String category ) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        FileApi service = RetroClient.getApiService();

        File file = new File(selectedFilePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("passport", file.getName(), requestFile);

        RequestBody unames = createPartFromString(session.getprefvalue("uname"));
        RequestBody titles = createPartFromString(title.getText().toString());
        RequestBody categorys = createPartFromString(category);
        RequestBody descptions = createPartFromString(description.getText().toString());

        RequestBody locations = createPartFromString(location.getText().toString());
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

                progressDialog.dismiss();

                // Response Success or Fail
                if (response.isSuccessful()) {
                    if (response.body().getError()==true)

                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        // here file could not move too big
                    else
                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    // here file uploded succesfull
                    setTonull();
                } else {
                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {
                progressDialog.dismiss();
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


    void setTonull(){

        imageview.setImageResource(R.mipmap.ic_launcher);
        location.setText("");
        description.setText("");
        title.setText("");
        categ = (Spinner) findViewById(R.id.spinner);


    }

}
