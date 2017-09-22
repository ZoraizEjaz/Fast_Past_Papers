package zoraiz.fast_past_papers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

import zoraiz.fast_past_papers.Activities.CoursesName;
import zoraiz.fast_past_papers.Model.HomeDepartmentAlbum;
import zoraiz.fast_past_papers.R;

/**
 * Created by Grabbit Media on 8/2/2017.
 */

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder> {

    private Context mContext;
    private List<HomeDepartmentAlbum> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, CoursesName.class);
                    intent.putExtra("departmentId",String.valueOf(albumList.get(getAdapterPosition()).getDepartmentId()));
                    intent.putExtra("departmentName", String.valueOf(albumList.get(getAdapterPosition()).getDepartment_name()));
                    mContext.startActivity(intent);

                }
            });

        }


    }


    public DepartmentAdapter(Context mContext, List<HomeDepartmentAlbum> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.department_cardview_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HomeDepartmentAlbum album = albumList.get(position);
        holder.title.setText(album.getDepartment_name());
        holder.count.setText(album.getDepartmentId() + " links");

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopupMenu(holder.overflow);
            }
        });


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        android.support.v7.widget.PopupMenu popup = new android.support.v7.widget.PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_department, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    // Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    //Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
