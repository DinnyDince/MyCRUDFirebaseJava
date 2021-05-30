package com.example.mycrudfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDataActivity extends AppCompatActivity {
    private EditText nimBaru, namaBaru, prodiBaru;
    private Button update;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private String cekNIM, cekNama, cekProdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        getSupportActionBar().setTitle("Update Data");
        nimBaru = findViewById(R.id.new_nim);
        namaBaru = findViewById(R.id.new_nama);
        prodiBaru = findViewById(R.id.new_prodi);
        update = findViewById(R.id.update);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekNIM = nimBaru.getText().toString();
                cekNama = namaBaru.getText().toString();
                cekProdi = prodiBaru.getText().toString();

                if(isEmpty(cekNIM) || isEmpty(cekNama) || isEmpty(cekProdi)) {
                    Toast.makeText(UpdateDataActivity.this, "Data tidak boleh kosong",
                            Toast.LENGTH_SHORT).show();
                } else {
                    data_mahasiswa setMahasiswa = new data_mahasiswa();
                    setMahasiswa.setNim(nimBaru.getText().toString());
                    setMahasiswa.setNama(namaBaru.getText().toString());
                    setMahasiswa.setProdi(prodiBaru.getText().toString());
                    updateMahasiswa(setMahasiswa);
                }
            }
        });
    }
    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void getData() {
        final String getNIM = getIntent().getExtras().getString("dataNIM");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getProdi = getIntent().getExtras().getString("dataProdi");
        nimBaru.setText(getNIM);
        namaBaru.setText(getNama);
        prodiBaru.setText(getProdi);
    }

    private void updateMahasiswa(data_mahasiswa mahasiswa) {
        String userID = auth.getUid();
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Admin")
                .child(userID)
                .child("Mahasiswa")
                .child(getKey)
                .setValue(mahasiswa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nimBaru.setText("");
                        namaBaru.setText("");
                        prodiBaru.setText("");
                        Toast.makeText(UpdateDataActivity.this, "Data berhasil diubah",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}