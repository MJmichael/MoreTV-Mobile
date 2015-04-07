package yang.mobile.api;

import retrofit.RestAdapter;

/**
 * Created by yang on 15/3/28.
 */
public class RestClient {

    private static final String END_POINT = "http://";

    private static MoreTVApi api;

    private RestClient() {}

    public static MoreTVApi get() {
        if (api == null) {
            synchronized (RestClient.class) {
                if (api == null) {
                    RestAdapter rest = new RestAdapter.Builder()
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .setEndpoint(END_POINT).build();

                    api = rest.create(MoreTVApi.class);
                }
            }
        }
        return api;
    }
}
