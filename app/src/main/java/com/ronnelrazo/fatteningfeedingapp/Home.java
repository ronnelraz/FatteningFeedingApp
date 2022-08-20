package com.ronnelrazo.fatteningfeedingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ronnelrazo.fatteningfeedingapp.Adapter.Adapter_menu_list;
import com.ronnelrazo.fatteningfeedingapp.model.model_menu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    private GlobalMethod globalMethod;
    @BindView(R.id.hamburgerIcon) ImageView Logout;



    String[] Menu_title = new String[]{
            "Title 1", "Title 2", "Title 3", "Title 4",
            "Title 5", "Title 6", "Title 7", "Title 8"
    };

    int[] Menu_icon = new int[]{
            R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,
            R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.ic_icons8_image
    };

    @BindView(R.id.menulist)
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<model_menu> menulist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        globalMethod = new GlobalMethod(this);


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "out", Toast.LENGTH_SHORT).show();
            }
        });
        Menu_initialized();

    }


    protected void Menu_initialized(){
        for(int i = 0; i < Menu_title.length; i++){
            model_menu list = new model_menu(
                    i,
                    Menu_icon[i],
                    Menu_title[i]
            );
            menulist.add(list);
        }

        adapter = new Adapter_menu_list(menulist,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        recyclerView.setAdapter(adapter);

    }
}