package com.example.agenda.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.models.Contacto;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.VH> {

    public interface OnItemClickListener {
        void onClick(Contacto c);
        void onLongClick(Contacto c);
    }

    private List<Contacto> data;
    private final OnItemClickListener listener;

    public ContactoAdapter(List<Contacto> data, OnItemClickListener l) {
        this.data = data;
        this.listener = l;
    }

    public void update(List<Contacto> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Contacto c = data.get(position);
        h.tvName.setText(c.getNombre());
        h.tvPhone.setText(c.getTelefono());
        h.tvCategory.setText("Categoría: " + (c.getIdCategoria() == null ? "-" : c.getIdCategoria()));
        h.itemView.setOnClickListener(v -> listener.onClick(c));
        h.itemView.setOnLongClickListener(v -> { listener.onLongClick(c); return true; });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvCategory;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}
