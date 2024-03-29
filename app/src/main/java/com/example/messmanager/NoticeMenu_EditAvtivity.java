package com.example.messmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messmanager.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticeMenu_EditAvtivity extends AppCompatActivity {

    private CardView menu_card;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseRef;
    private TextView notice;
    SharedPreferences sharedPreferences;
    private DayMenuAdapter mAdapter;
    private List<DayMenuCard> mUploads;
    private String mess_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_menu__edit_avtivity);

        notice = findViewById(R.id.notice_board_text);
        mRecyclerView = findViewById(R.id.recycler_menucard);
        mRecyclerView.setHasFixedSize(true);
        mUploads = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
         mess_no = sharedPreferences.getString(Constants.mess,"Mess1");
        Log.d("mess1234",mess_no);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Mess");

        mDatabaseRef.child("Mess1").child("board_sheet").child("notice_board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String notice1 = dataSnapshot.child("message").getValue(String.class);
            notice.setText(notice1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NoticeMenu_EditAvtivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        mDatabaseRef.child("Mess1").child("board_sheet").child("menu_board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                   DayMenuCard upload = postSnapshot.getValue(DayMenuCard.class);
                   // Log.d("upload12",upload.toString());
                   mUploads.add(upload);
                    String breakfast = postSnapshot.child("breakfast").getValue(String.class);
                    Log.d("breakfast",breakfast);
                }
                mAdapter = new DayMenuAdapter(mUploads);
                mRecyclerView.setAdapter(mAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NoticeMenu_EditAvtivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
