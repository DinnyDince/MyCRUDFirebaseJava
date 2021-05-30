package com.example.mycrudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private EditText NIM, Nama, Prodi;
    private FirebaseAuth auth;
    private Button Login, Simpan, Logout, ShowData;

    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        Login = findViewById(R.id.login);
        Login.setOnClickListener(this);

        Simpan = findViewById(R.id.save);
        Simpan.setOnClickListener(this);

        Logout = findViewById(R.id.logout);
        Logout.setOnClickListener(this);

        ShowData = findViewById(R.id.showdata);
        ShowData.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        NIM = findViewById(R.id.nim);
        Nama = findViewById(R.id.nama);
        Prodi = findViewById(R.id.prodi);

        if(auth.getCurrentUser() == null) {
            defaultUI();
        } else {
            updateUI();
        }
    }

    private void defaultUI() {
        Login.setEnabled(true);
        Simpan.setEnabled(false);
        Logout.setEnabled(false);
        ShowData.setEnabled(false);
        NIM.setEnabled(false);
        Nama.setEnabled(false);
        Prodi.setEnabled(false);
    }

    private void updateUI() {
        Login.setEnabled(false);
        Simpan.setEnabled(true);
        Logout.setEnabled(true);
        ShowData.setEnabled(true);
        NIM.setEnabled(true);
        Nama.setEnabled(true);
        Prodi.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    protected void onActivityResult(int requestcode, int resultCode, Intent data) {
        super.onActivityResult(requestcode, resultCode, data);
        if(requestcode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                updateUI();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Login Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false).build(), RC_SIGN_IN);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.save:
                String getUserId = auth.getCurrentUser().getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference getReference;

                String getNIM = NIM.getText().toString();
                String getNama = Nama.getText().toString();
                String getProdi = Prodi.getText().toString();

                getReference = database.getReference();

                if(isEmpty(getNIM) && isEmpty(getNama) && isEmpty(getProdi)) {
                    Toast.makeText(MainActivity.this, "Data tidak boleh ada yang kosong",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getReference.child("Admin").child(getUserId).child("Mahasiswa").push()
                            .setValue(new data_mahasiswa(getNIM, getNama, getProdi))
                            .addOnSuccessListener(this, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    NIM.setText("");
                                    Nama.setText("");
                                    Prodi.setText("");
                                    Toast.makeText(MainActivity.this, "Data Tersimpan",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                } break;
            case R.id.logout:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(MainActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case R.id.showdata:
                startActivity(new Intent(MainActivity.this, MyListDataActivity.class ));
                break;
        }
    }
}