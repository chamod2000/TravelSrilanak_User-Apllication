package com.jiat.usertravellanka;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jiat.usertravellanka.model.Place;

import java.util.ArrayList;

public class Cultureadapter extends RecyclerView.Adapter<Cultureadapter.ViewHolder2> {

    private ArrayList<Place> places;
    FirebaseStorage storege;
    private Context context;

    public Cultureadapter(ArrayList<Place> places,Context context){

        this.places = places;
        this.storege = FirebaseStorage.getInstance();
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.culture_row,parent,false);
        return new Cultureadapter.ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
        Place place = places.get(position);
        holder.textname.setText(place.getPname());

        storege.getReference("location-images/"+place.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }




    public static class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {


        private static final String TAG ="Pars" ;
        TextView textname;
        ImageView image;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            textname = itemView.findViewById(R.id.ctextViewPname);
            image = itemView.findViewById(R.id.cimageViewPhoto);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String name = textname.getText().toString();
            Log.i(TAG,name);

            Context context = view.getContext();
            Intent intent = new Intent(context, cultureSingleView.class);
            intent.putExtra("locationName",name);
            intent.putExtra("categoty","culture");
            context.startActivity(intent);



        }
    }

}
