package com.example.mycrudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyListDataActivity extends AppCompatActivity implements RecyclerViewAdapter.dataListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<data_mahasiswa> dataMahasiswas;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_data);

        recyclerView = findViewById(R.id.datalist);
        getSupportActionBar().setTitle("Data Mahasiswa");
        auth = FirebaseAuth.getInstance();
        MyRecyclerView();
        GetData();
    }

    private void MyRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);

        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void GetData() {
        Toast.makeText(getApplicationContext(), "Mohon tunggu sebentar...", Toast.LENGTH_LONG).show();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child(auth.getUid()).child("Mahasiswa")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataMahasiswas = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            data_mahasiswa mahasiswa = snapshot.getValue(data_mahasiswa.class);
                            mahasiswa.setKey(snapshot.getKey());
                            dataMahasiswas.add(mahasiswa);

                            adapter = new RecyclerViewAdapter(dataMahasiswas, MyListDataActivity.this);

                            recyclerView.setAdapter(adapter);

                            Toast.makeText(getApplicationContext(), "Data berhasil dimuat", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Data gagal dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails()+""+databaseError.getMessage());
                    }
                });
    }

    @Override
    public void onDeleteData(data_mahasiswa data, int position) {
        String userID = auth.getUid();
        if(reference != null) {
            reference.child("Admin")
                    .child(userID)
                    .child("Mahasiswa")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MyListDataActivity.this, "Data berhasil dihapus",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}