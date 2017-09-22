package zoraiz.fast_past_papers.Rest;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Zoraiz on 8/16/2017.
 */

public class ApiClient {

    public static final String BASE_URL = "http://fastnu.technocraz.net/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("https://androidtutorialpoint.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
}
