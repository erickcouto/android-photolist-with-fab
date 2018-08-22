package br.com.erickalves.photolist.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.erickalves.photolist.Model.PhotoItem;
import br.com.erickalves.photolist.R;
import br.com.erickalves.photolist.Util.DateUtil;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<PhotoItem> photos;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(PhotoItem item, ImageView iv);
    }

    public PhotoListAdapter(List<PhotoItem> photos, OnItemClickListener listener) {
        this.photos = photos;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView created;
        public ImageView thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.photoName);
            created = itemView.findViewById(R.id.photoCreated);
            thumb = itemView.findViewById(R.id.photoThumb);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final PhotoItem  item = photos.get(position);

        if(!TextUtils.isEmpty(item.getName())) {
            holder.name.setText(item.getName());
        }

        if(item.getCreated()!=null) {
            holder.created.setText(DateUtil.getDate(item.getCreated()));
        }

        if(!TextUtils.isEmpty(item.getImageSource())) {
            Bitmap bitmap = BitmapFactory.decodeFile(item.getImageSource());
            holder.thumb.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(item, holder.thumb);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.photos != null ? this.photos.size() : 0;
    }
}