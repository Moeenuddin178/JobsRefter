package com.alert.jobsrefter.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.alert.jobsrefter.BuildConfig;
import com.alert.jobsrefter.Models.Jobs_Model;
import com.alert.jobsrefter.R;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.annotations.NotNull;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Images_Adapter extends RecyclerView.Adapter<Images_Adapter.MyViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback, Filterable {

    List<Jobs_Model> images;
    Context context;
    List<Jobs_Model> filteredImages;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    public Images_Adapter(Context context, List<Jobs_Model> images) {
        super();
        this.context = context;
        this.images = images;
        this.filteredImages = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

       // byte[] decodedString = Base64.decode(filteredImages.get(position).getImage(), Base64.DEFAULT);
      //  final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
      //   Glide.with(context).load(filteredImages.get(position).getImage()).into(holder.imageView);
        Glide.with(context).load(filteredImages.get(position).getImage())
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(holder.imageView);
        // ZoomInImageViewAttacher mIvAttacter = new ZoomInImageViewAttacher();
        //mIvAttacter.attachImageView(holder.imageView);
        PhotoViewAttacher photoAttacher;
        photoAttacher = new PhotoViewAttacher(holder.imageView);
        photoAttacher.update();

        holder.Searcg_tag.setText(images.get(position).getTag());
        holder.current.setText(String.valueOf(position + 1));
        holder.total.setText(String.valueOf(filteredImages.size()));
        holder.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // download();
                shareImage(holder.imageView);
                Toast.makeText(context, "Share Wallpaper", Toast.LENGTH_SHORT).show();


            }

        });
        holder.Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = images.get(position).getLink();
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(openUrlIntent);

            }

        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_CODE);
                    }
                    return;
                }

                // saveImage(decodedByte);
                gallery_save(holder.imageView);

                Toast.makeText(context, "Job Ad Saved", Toast.LENGTH_SHORT).show();


            }

        });
    }

    private void gallery_save(ImageView imageView) {
        //to get the image from the ImageView (say iv)
        BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/Refter_jobs");
        dir.mkdirs();
        String fileName = String.format("image" + "%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(dir));
        context.sendBroadcast(intent);
    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Refter_jobs_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Shutta_" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(@NotNull View view) {

        Bitmap returnBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgDrawable = view.getBackground();

        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnBitmap;
    }

    private void shareImage(ImageView imageView) {
        Bitmap bitmap = getBitmapFromView(imageView);
        try {
            File file = new File(context.getExternalCacheDir(), "black.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file));
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, "Share Image Via"));
            Toast.makeText(context, "Share Wallpaper", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return filteredImages.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredImages = images;
                } else {
                    List<Jobs_Model> filteredList = new ArrayList<>();
                    for (Jobs_Model name : images) {
                        if (name.getTag().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        filteredImages = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredImages;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredImages = (List<Jobs_Model>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button download, Share, Apply;// init the item view's
        ImageView imageView;
        TextView current, total, Searcg_tag;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            download = (Button) itemView.findViewById(R.id.download);
            Share = (Button) itemView.findViewById(R.id.share);
            Apply = (Button) itemView.findViewById(R.id.apply);
            current = itemView.findViewById(R.id.current);
            Searcg_tag = itemView.findViewById(R.id.search_tags);
            total = itemView.findViewById(R.id.total);
            imageView = (ImageView) itemView.findViewById(R.id.iv_images);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;


        }
    }


}
