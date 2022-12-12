package com.example.thihttp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.thihttp.BaseUrl;
import com.example.thihttp.OnClickItem;
import com.example.thihttp.R;
import com.example.thihttp.model.Moto;
import com.example.thihttp.screen.SuaActivity;

import java.util.List;

public class MotoAdapter extends RecyclerView.Adapter<MotoAdapter.MotoViewHolder> {

    private List<Moto> listMoto;
    private Context mContext;
    public OnClickItem onClickItem;

    public MotoAdapter(List<Moto> listMoto, Context mContext) {
        this.listMoto = listMoto;
        this.mContext = mContext;
    }

    public void setData(List<Moto> listMoto) {
        this.listMoto = listMoto;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moto, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MotoViewHolder holder, int position) {
        Moto moto = listMoto.get(position);
        holder.tvcolor.setText(moto.getColor());
        holder.tvName.setText(moto.getName());
        holder.tvPrice.setText(moto.getPrice() + "");
        Glide.with(mContext)
                .load(moto.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_file_download_off_24)
                .into(holder.img);

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SuaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("moto", moto);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure to Exit")
                        .setMessage("Do you want delete this moto?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onClickItem.onClickDelete(moto);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(mContext.getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();


            }
        });
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

            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvcolor = itemView.findViewById(R.id.tv_color);
            img = itemView.findViewById(R.id.img);
            btnSua = itemView.findViewById(R.id.btn_sua);
            btnXoa = itemView.findViewById(R.id.btn_xoa);
        }
    }

}
