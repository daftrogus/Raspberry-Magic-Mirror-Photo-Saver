package com.example.photo_saver;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {

    OutputStream outputStream;
    private ArrayList<ParseItem> parseItems;
    private Context context;
    CardView cardView;
    ImageView imageView;
    TextView textView;
    String src;
    Bitmap bitmap;

    public ParseAdapter(ArrayList<ParseItem> parseItems, Context context){
        this.parseItems = parseItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ParseAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_adapter, parent,false);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout.addView(LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item, parent, false));

        cardView = view.findViewById(R.id.card_view);
        imageView = view.findViewById(R.id.imageViewFotoAnak);
        textView = view.findViewById(R.id.textViewNamaAnak);

//        cardView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
////                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
////                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//
//                File filepath = Environment.getExternalStorageDirectory();
//                File dir = new File(filepath.getAbsolutePath()+"/PhotoSaver/");
//                dir.mkdir();
//                File file = new File(dir, textView+".jpg");
//                DownloadImageTask downloadImageTask = new DownloadImageTask();
//                downloadImageTask.execute(file);
//                try {
//                    outputStream = new FileOutputStream(file);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
////                file.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//                try {
//                    outputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapter.ViewHolder holder, int position) {
        ParseItem parseItem = parseItems.get(position);
        holder.textView.setText(parseItem.getTitle());
        Picasso.get().load("http://daftrogus.com"+parseItem.getImgUrl()).into(holder.imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ONCLICKKONTOL","PEPEK");
                bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getAbsolutePath()+"/PhotoSaver/");
                dir.mkdir();
                File file = new File(dir, textView.getText()+".jpg");
                try{
                    Log.i("ONCLICKKONTOL","PEPEK2");
                    outputStream = new FileOutputStream(file);
                    Log.i("ONCLICKKONTOL","PEPEK4");
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    Log.i("ONCLICKKONTOL","PEPEK5");
                    outputStream.flush();
                    outputStream.close();
                    Log.i("ONCLICKKONTOL","PEPEK6");
                    Toast.makeText(view.getContext(), "Gambar berhasil disimpan!", LENGTH_SHORT).show();
                }catch(Exception e){
                    Log.i("ONCLICKKONTOL",e.toString());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewFotoAnak);
            textView = itemView.findViewById(R.id.textViewNamaAnak);
        }

    }

}
