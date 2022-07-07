package com.shailu.onlineoffline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

public class add_news extends AppCompatActivity {

    public Uri imageUri;
    private String myUrl="";
    private StorageTask uploadtask;
    StorageReference storageprofilepictureref;
    private ImageView newsimg;
    private TextView add_news_btn;
    private EditText newsTitle;
    private EditText newsDescription;
    private  FirebaseDatabase databasdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        databasdatabase=FirebaseDatabase.getInstance();
        storageprofilepictureref = FirebaseStorage.getInstance().getReference().child("News");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ADD NEWS");
        setSupportActionBar(toolbar);
        newsimg=(ImageView)findViewById(R.id.add_image_in_recycler);
        newsTitle=(EditText) findViewById(R.id.add_text_title_in_recycler);
        newsDescription=(EditText) findViewById(R.id.add_text_discription_in_recycler);
        add_news_btn=(TextView) findViewById(R.id.add_news_btn);

        newsimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(add_news.this);
            }
        });

        add_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                check_condition();
            }
        });
    }

    private void check_condition()
    {
        if(TextUtils.isEmpty(newsTitle.getText().toString()))
        {
            newsTitle.setError("Title is madenetory..");
            newsTitle.requestFocus();
        }
        else if(TextUtils.isEmpty(newsDescription.getText().toString()))
        {
            newsDescription.setError("The Description is madenatory..");
            newsDescription.requestFocus();
        }
        else if(imageUri==null)
        {
            Toast.makeText(this, "Image is madenetory...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadimage();
           /* DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("News");

            HashMap<String,Object> usermap = new HashMap<>();

            usermap.put("News_Title",newsTitle.getText().toString());
            usermap.put("News_Description",newsDescription.getText().toString());
            ref.child(newsTitle.getText().toString()).updateChildren(usermap);
            startActivity(new Intent(add_news.this,MainActivity.class));
            Toast.makeText(add_news.this, "updated successfully...", Toast.LENGTH_SHORT).show();

            */

        }
    }


    private void uploadimage()
    {
        if(imageUri!=null)
        {
           // Toast.makeText(this, "!=null", Toast.LENGTH_SHORT).show();
            final StorageReference Ref=storageprofilepictureref.child(newsTitle.getText().toString()+".jpg");
            uploadtask=Ref.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        //Toast.makeText(add_news.this, "not succ", Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }

                    return Ref.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if(task.isSuccessful())
                            {
                               // Toast.makeText(add_news.this, "succ", Toast.LENGTH_SHORT).show();
                                Uri downloadurl= task.getResult();

                                myUrl= Objects.requireNonNull(downloadurl).toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("News");

                                HashMap<String,Object> usermap = new HashMap<>();
                                usermap.put("News_Title",newsTitle.getText().toString());
                                usermap.put("News_Description",newsDescription.getText().toString());
                                usermap.put("Image",myUrl);

                                ref.child(newsTitle.getText().toString()).updateChildren(usermap);
                                startActivity(new Intent(add_news.this,MainActivity.class));
                                Toast.makeText(add_news.this, "Info updated successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(add_news.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Image is not Selected...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            newsimg.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error try again...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(add_news.this, add_news.class));
            finish();
        }
    }
}
