package com.example.thihttp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thihttp.R;
import com.example.thihttp.model.Moto;

import java.util.List;

public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.MotoViewHolder>{

    private List<Moto> listMoto;
    private Context mContext;

    public BackupAdapter(List<Moto> listMoto, Context mContext) {
        this.listMoto = listMoto;
        this.mContext = mContext;
    }

    public void setData(List<Moto> listMoto){
        this.listMoto = listMoto;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_backup, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MotoViewHolder holder, int position) {
        Moto moto = listMoto.get(position);
        holder.tvcolor.setText(moto.getColor());;
        holder.tvName.setText(moto.getName());
        holder.tvPrice.setText(moto.getPrice()+"");
        Glide.with(mContext)
                .load(moto.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_file_download_off_24)
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return listMoto.size();
    }

    class MotoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvcolor;
        ImageView img, btnSua, btnXoa;
        public MotoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName  = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvcolor = itemView.findViewById(R.id.tv_color);
            img = itemView.findViewById(R.id.img);
        }
    }
}
