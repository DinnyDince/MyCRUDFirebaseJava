package com.example.mycrudfirebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<data_mahasiswa> listMahasiswa;
    private Context context;
    dataListener Listener;

    public RecyclerViewAdapter(ArrayList<data_mahasiswa> listMahasiswa, Context context) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
        listener = (MyListDataActivity)context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String NIM = listMahasiswa.get(position).getNim();
        final String Nama = listMahasiswa.get(position).getNama();
        final String Prodi = listMahasiswa.get(position).getProdi();

        holder.NIM.setText("NIM: "+NIM);
        holder.Nama.setText("Nama: "+Nama);
        holder.Prodi.setText("Prodi: "+Prodi);

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                       switch (i) {
                           case 0:
                               Bundle bundle = new Bundle();
                               bundle.putString("dataNIM", listMahasiswa.get(position).getNim());
                               bundle.putString("dataNama", listMahasiswa.get(position).getNama());
                               bundle.putString("dataProdi", listMahasiswa.get(position).getProdi());
                               bundle.putString("getPrimaryKey", listMahasiswa.get(position).getKey());
                               Intent intent = new Intent(view.getContext(), UpdateDataActivity.class);
                               intent.putExtras(bundle);
                               context.startActivity(intent);
                               break;
                           case 1:
                               listener.onDeleteData(listMahasiswa.get(position), position);
                               break;
                       }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView NIM, Nama, Prodi;
        private LinearLayout ListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NIM = itemView.findViewById(R.id.nim);
            Nama = itemView.findViewById(R.id.nama);
            Prodi = itemView.findViewById(R.id.prodi);
            ListItem = itemView.findViewById(R.id.list_item);
        }
    }

    public interface dataListener {
        void onDeleteData(data_mahasiswa mahasiswa, int position);
    }

    dataListener listener;
}
