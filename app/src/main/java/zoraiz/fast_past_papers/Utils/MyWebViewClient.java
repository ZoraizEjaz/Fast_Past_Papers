package zoraiz.fast_past_papers.Utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Zoraiz on 9/17/2017.
 */

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }
}
