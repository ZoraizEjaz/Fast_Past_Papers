package zoraiz.fast_past_papers.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zoraiz.fast_past_papers.Activities.FilterPaper;
import zoraiz.fast_past_papers.Activities.MainActivity;
import zoraiz.fast_past_papers.Model.Paper;
import zoraiz.fast_past_papers.R;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Zoraiz on 8/5/2017.
 */

public class PaperAdapter extends RecyclerView.Adapter<PaperAdapter.channelHolder> {

    private static Context mContext;
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<Paper> mDataset;
    private static MyClickListener myClickListener;



    //channelHolder

    public static class channelHolder extends RecyclerView.ViewHolder {


        TextView year;
        TextView type;
        TextView semester;
        TextView paper_name;
        ImageView download;


        public channelHolder(View itemView)
        {
            super(itemView);
            paper_name = (TextView) itemView.findViewById(R.id.paper_name);
            year = (TextView) itemView.findViewById(R.id.year);
            type = (TextView) itemView.findViewById(R.id.type);
            download =(ImageView) itemView.findViewById(R.id.download);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });

        }


    }





    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PaperAdapter(Context mContext, ArrayList<Paper> myDataset) {
        this.mContext = mContext;
        mDataset = myDataset;
    }

    @Override
    public channelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.papers_cardview_row, parent, false);

        channelHolder dataObjectHolder = new channelHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(channelHolder holder, final int position) {

        holder.paper_name.setText(mDataset.get(position).getPaperName());

        holder.year.setText(mDataset.get(position).getYear());
       // holder.semester.setText(mDataset.get(position).getSemester());
        holder.type.setText(mDataset.get(position).getDocument_type());

        final String downloadLink = MainActivity.paper_list.get(position).getLink1();
        final String paperName = MainActivity.paper_list.get(position).getPaperName();
        final String doucmentType = MainActivity.paper_list.get(position).getDocument_type();

        holder.download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                long downloadReference;
                Uri uri = Uri.parse(downloadLink);
                Toast.makeText(mContext,"Please Wait",Toast.LENGTH_LONG).show();

                DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                //Setting title of request
                request.setTitle(paperName);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                //Setting description of request
                request.setDescription("Downloading Paper");

                //Set the local destination for the downloaded file to a path within the application's external files directory

                request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,
                        paperName+"."+doucmentType);


                request.allowScanningByMediaScanner();
                //Enqueue download and save the referenceId
                downloadReference = downloadManager.enqueue(request);
            }
        });
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