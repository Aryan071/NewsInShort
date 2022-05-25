package com.example.newsinshort;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    // 733689325a5c449ead012762ff5cd16a


    private RecyclerView newsRV, categoryRV;
    private ProgressBar LoadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        LoadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList, this, this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getCategories() {
        categoryRVModelArrayList.add(new CategoryRVModel("All", "https://images.unsplash.com/photo-1588681664899-f142ff2dc9b1?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=80&raw_url=true&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology", "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=80&raw_url=true&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1169\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science", "https://media.istockphoto.com/photos/medical-science-laboratory-handsome-latin-male-scientist-writes-picture-id1346675624?s=612x612\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports", "https://media.istockphoto.com/photos/cricket-batsman-about-to-strike-ball-picture-id180868820?s=612x612\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("General", "https://media.istockphoto.com/photos/headline-picture-id184625088\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business", "https://images.unsplash.com/photo-1584964139384-8baf818ba6c8?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=80&raw_url=true&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment", "https://media.istockphoto.com/photos/party-people-enjoy-concert-at-festival-summer-music-festival-picture-id1324561072\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health", "https://media.istockphoto.com/photos/fresh-vegetables-and-legumes-in-wooden-box-picture-id1025179654?s=612x612\n"));
        categoryRVAdapter.notifyDataSetChanged();
    }


    private void getNews(String category) {
        LoadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apiKey=733689325a5c449ead012762ff5cd16a";
        String url = "https://newsapi.org/v2/top-headlines?country=in&ec=xcludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=733689325a5c449ead012762ff5cd16a";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPI retrofitapi = retrofit.create(retrofitAPI.class);
        Call<NewsModel> call;
        if (category.equals("All")){
            call = retrofitapi.getAllNews(url);
        }
        else {
            call = retrofitapi.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                LoadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for (int i=0;i<articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));
                }

                newsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                Toast.makeText(MainActivity.this,"Fail to get news",Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onCategoryClick(int position) {

        String category = categoryRVModelArrayList.get(position).getCategory();
        getNews(category);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}



