package com.shailu.onlineoffline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference news_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news_ref = FirebaseDatabase.getInstance().getReference().child("News");
        news_ref.keepSynced(true);

        Paper.init(this);
        Toolbar maintoolbar = findViewById(R.id.main_toolbar);
        maintoolbar.setTitle("NEWS");
        setSupportActionBar(maintoolbar);

        recyclerView=findViewById(R.id.news_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<News> options=
                new FirebaseRecyclerOptions.Builder<News>()
                        .setQuery(news_ref,News.class)
                        .build();

        FirebaseRecyclerAdapter<News, news_detail_holder> adapter=
                new FirebaseRecyclerAdapter<News, news_detail_holder>(options)
                {
                    @NonNull
                    @Override
                    public news_detail_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_news_recycler_view,parent,false);
                        news_detail_holder holder=new news_detail_holder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final news_detail_holder news_detail_holder, int i, @NonNull final News news)
                    {
                        news_detail_holder.title.setText(news.getNews_Title());
                        news_detail_holder.description.setText(news.getNews_Description());
                        Picasso.get().load(news.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(news_detail_holder.image, new Callback() {
                            @Override
                            public void onSuccess() {
                               // Toast.makeText(MainActivity.this, "OFFLINE LOADING DONE...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e)
                            {
                                Picasso.get().load(news.getImage()).into(news_detail_holder.image);
                            }
                        });
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_news)
        {
            Intent intent=new Intent(MainActivity.this,add_news.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
