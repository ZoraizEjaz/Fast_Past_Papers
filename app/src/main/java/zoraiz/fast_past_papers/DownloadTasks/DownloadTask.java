package zoraiz.fast_past_papers.DownloadTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import zoraiz.fast_past_papers.R;
import zoraiz.fast_past_papers.SdCard.CheckForSDCard;
import zoraiz.fast_past_papers.Utils.Utils;

/**
 * Created by Zoraiz on 8/7/2017.
 */

public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "", downloadFileName = "";

    public DownloadTask(Context context, Button buttonText, String downloadUrl) {
        this.context = context;
        this.buttonText = buttonText;
        this.downloadUrl = downloadUrl;

        downloadFileName = downloadUrl.replace(Utils.mainUrl, "");//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
            buttonText.setText(R.string.downloadStarted);//Set Button Text when download started
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {

                File mydir = context.getDir("books", Context.MODE_PRIVATE);
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }

                InputStream is=c.getInputStream();
                File file = new File(mydir, "Test.pdf");

                FileOutputStream fos = new FileOutputStream(file);
                byte data[] = new byte[1024];

                int current = 0;
                while ((current = is.read(data)) != -1) {
                    fos.write(data, 0, current);
                }
                is.close();
                fos.flush();
                fos.close();



            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();

                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {


        }


    }
}
