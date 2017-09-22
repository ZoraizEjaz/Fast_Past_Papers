package zoraiz.fast_past_papers.SdCard;

import android.os.Environment;

/**
 * Created by Zoraiz on 8/7/2017.
 */

public class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}