package zoraiz.fast_past_papers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zoraiz.fast_past_papers.Activities.FilterPaper;
import zoraiz.fast_past_papers.Activities.MainActivity;
import zoraiz.fast_past_papers.Activities.Papers;
import zoraiz.fast_past_papers.Model.CoursesResponse;
import zoraiz.fast_past_papers.R;

/**
 * Created by Grabbit Media on 8/3/2017.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.channelHolder> {

    private static Context mContext;
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<CoursesResponse> mDataset;
    private static MyClickListener myClickListener;


    //channelHolder

    public static class channelHolder extends RecyclerView.ViewHolder {

        TextView course_name;
        ImageView image;

        public channelHolder(View itemView)
        {
            super(itemView);

            course_name = (TextView) itemView.findViewById(R.id.course_name);
            image =(ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String courseName = MainActivity.courses_list.get(getAdapterPosition()).getCourse_name();
                    Intent i = new Intent(mContext, Papers.class);
                    i.putExtra("courseId", MainActivity.courses_list.get(getAdapterPosition()).getCourseId());
                    mContext.startActivity(i);
                }
            });
        }


    }


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CourseAdapter(Context mContext, ArrayList<CoursesResponse> myDataset) {
        this.mContext = mContext;
        mDataset = myDataset;
    }

    @Override
    public channelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_name_cardview_row, parent, false);

        channelHolder dataObjectHolder = new channelHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(channelHolder holder, int position) {
        holder.course_name.setText(mDataset.get(position).getCourse_name());
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}