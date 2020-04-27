package d.tsaplya.guidelead;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;


import org.codehaus.jackson.annotate.JsonProperty;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;


/**
 * Class with static methods to work with API
 */
public class APIRequests {


    private static BlockingQueue<TheradMessage<UserPreferences>> queue = new ArrayBlockingQueue<>(5);

    private final static String prefsURL = "https://whispering-plateau-32180.herokuapp.com/prefs";

    public static UserPreferences getUserPreference(Long id) throws InterruptedException {

        final String currentUrl = prefsURL + "/" + id.toString();

        Thread networkThread = new Thread(new JSONProducer(queue, currentUrl));

        networkThread.start();
        UserPreferences userPreferences = queue.take().getLoad();
        Log.e("JSON:",userPreferences.toString());
        return userPreferences;

    }

    public static boolean addUserPreference(UserPreferences userPreference) throws InterruptedException, IOException {
    return true;

    }
//
//    public static boolean deleteUserPreference(UserPreferences userPreference) throws InterruptedException {
//
//
//    }


    private static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpsURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private static UserPreferences JSONToUserPreferences(JSONObject jsonObject) throws JSONException {

        String catString = jsonObject.getString("categoryList")
                .replaceAll("\\[", "")
                .replaceAll("]", "");

        ArrayList<String> categoryStringList =
                new ArrayList<>(Arrays.asList(catString.split(",")));


        ArrayList<Category> categoryList = categoryStringList.
                stream().map(Category::valueOf).collect(Collectors.toCollection(ArrayList::new));

        return new UserPreferences(categoryList,
                Duration.valueOf(jsonObject.getString("duration")));
    }

    private static UserPreferences getJSONObjectFromURLUsingRESTTemplate(String uri){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        return restTemplate.getForObject(uri, UserPreferences.class);
    }




//    private static String JSONStringFromUserPreferences(UserPreferences userPreferences) throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("categoryList", userPreferences.getCategoryListString());
//        jsonObject.put("duration", userPreferences.getDuration().toString());
//
//
//    }


    static class JSONProducer implements Runnable {

        private BlockingQueue<TheradMessage<UserPreferences>> queue;
        private String currentUrl;

        JSONProducer(BlockingQueue<TheradMessage<UserPreferences>> q, String currentUrl) {
            queue = q;
            this.currentUrl = currentUrl;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            {


                JSONObject jsonObject = null;
                try {
                    jsonObject = APIRequests.getJSONObjectFromURL(currentUrl);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                UserPreferences newUserPrefs = null;
                try {
                    newUserPrefs = APIRequests.JSONToUserPreferences(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // UserPreferences newUserPrefs = APIRequests.getJSONObjectFromURLUsingRESTTemplate(currentUrl);
                    TheradMessage<UserPreferences> message =
                            new TheradMessage<UserPreferences>("msg", newUserPrefs);

                try {
                    queue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
