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

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private ArrayList<Place> places;
    FirebaseStorage storage;
    private Context context;


    public PlaceAdapter(ArrayList<Place> places, Context context){
        this.places =places;
        this.storage = FirebaseStorage.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.place_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       Place place = places.get(position);
       holder.textname.setText(place.getPname());

       storage.getReference("location-images/"+place.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private static final String TAG ="Pars" ;
        TextView textname;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textname = itemView.findViewById(R.id.textViewPname);
            image = itemView.findViewById(R.id.imageViewPhoto);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            String name = textname.getText().toString();
            Log.i(TAG,name);


           Context context = view.getContext();
            Intent intent = new Intent(context, singleLocationViewActivity.class);
            intent.putExtra("locationName",name);
            intent.putExtra("category","wildlife");
            context.startActivity(intent);



        }
    }

}
