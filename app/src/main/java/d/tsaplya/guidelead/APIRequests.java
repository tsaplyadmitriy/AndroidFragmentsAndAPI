package d.tsaplya.guidelead;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;


import org.codehaus.jackson.annotate.JsonProperty;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;


/**
 * Class with static methods to work with API
 */
public class APIRequests {


    //Queue to send information between Producer and Consumer
    private static BlockingQueue<TheradMessage<UserPreferences>> queue = new ArrayBlockingQueue<>(5);

    private final static String prefsURL = "https://whispering-plateau-32180.herokuapp.com/prefs";

    public static UserPreferences getUserPreference(Long id) throws InterruptedException, HttpClientErrorException {



            final String currentUrl = prefsURL + "/" + id.toString();

            Thread networkThread = new Thread(new JSONProducer(queue, currentUrl, RequestType.GET));

            networkThread.start();
            UserPreferences userPreferences = queue.take().getLoad();
            Log.e("JSON:", userPreferences.toString());
            return userPreferences;

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<UserPreferences> getAlluserPrefernces(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

        ResponseEntity<TemporaryParsePreferenceContainer[]> response =
                restTemplate.getForEntity(
                        prefsURL,
                        TemporaryParsePreferenceContainer[].class);
        TemporaryParsePreferenceContainer[] employees = response.getBody();

        return Arrays.stream(employees)
                .map(TemporaryParsePreferenceContainer::convertToUserPreference)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static  UserPreferences addUserPreference(UserPreferences userPreferences) throws InterruptedException {

        Thread networkThread = new Thread(new JSONProducer(queue, prefsURL,RequestType.POST,userPreferences));

        networkThread.start();
        UserPreferences newUserPrefs = queue.take().getLoad();
        Log.e("JSON_Posted:",newUserPrefs.toString());
        return newUserPrefs;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    static UserPreferences getJSONObjectFromURLUsingRESTTemplate(String uri){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());


        TemporaryParsePreferenceContainer temp = restTemplate.getForObject(uri, TemporaryParsePreferenceContainer.class);

        Log.d(temp+":list",temp.categoryList+":list");
        return temp.convertToUserPreference();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static UserPreferences postJSONObjectFromURLUsingRESTTemplate(UserPreferences prefs, String uri){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        TemporaryParsePreferenceContainer temp = TemporaryParsePreferenceContainer.getInstance(prefs);
        return restTemplate.postForObject(uri, temp,TemporaryParsePreferenceContainer.class).convertToUserPreference();
    }




//    private static String JSONStringFromUserPreferences(UserPreferences userPreferences) throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("categoryList", userPreferences.getCategoryListString());
//        jsonObject.put("duration", userPreferences.getDuration().toString());
//
//
//    }

    static class TemporaryParsePreferenceContainer{

        public long id;
        private String categoryList;

        public String getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(String categoryList) {
            this.categoryList = categoryList;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        private String duration;

TemporaryParsePreferenceContainer(){}

        TemporaryParsePreferenceContainer(String categoryList,String duration){
            this.categoryList = categoryList;
            this.duration = duration;



        }


        public static TemporaryParsePreferenceContainer getInstance(UserPreferences preferences){
           TemporaryParsePreferenceContainer temp = new TemporaryParsePreferenceContainer(
                   preferences.getCategoryListString(),preferences.getDuration().toString()
           );

            return  temp;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private ArrayList<Category> convertToCategotyList(){
            String[]parsedCategories = categoryList.substring(1,categoryList.length()-1).split(",");

            return (ArrayList<Category>) Arrays.stream(parsedCategories)
                            .map(e->e.replaceAll(" ","")).map(Category::valueOf)
                            .collect(Collectors.toList());


        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public UserPreferences convertToUserPreference(){
                return  new UserPreferences(convertToCategotyList(),Duration.valueOf(duration));


        }

    }


    enum RequestType{
        POST,GET,DELETE
    }


}
 class JSONProducer implements Runnable {

    private BlockingQueue<TheradMessage<UserPreferences>> queue;
    private String currentUrl;
    static private APIRequests.RequestType type;
    private UserPreferences userPreferences = null;

    JSONProducer(BlockingQueue<TheradMessage<UserPreferences>> q, String currentUrl, APIRequests.RequestType type) {
        queue = q;
        this.currentUrl = currentUrl;
        JSONProducer.type = type;
        Log.d("type: ",type.toString());
    }
    JSONProducer(BlockingQueue<TheradMessage<UserPreferences>> q, String currentUrl, APIRequests.RequestType type, UserPreferences userPreferences) {
        queue = q;
        this.currentUrl = currentUrl;
        JSONProducer.type = type;
        this.userPreferences = userPreferences;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {

        switch(type){
            case GET:GET();
            case POST:POST();

        }








    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void POST(){
        if(userPreferences!=null){
        UserPreferences newUserPrefs = APIRequests.postJSONObjectFromURLUsingRESTTemplate(userPreferences,currentUrl);
        TheradMessage<UserPreferences> message =
                new TheradMessage<UserPreferences>("msg", newUserPrefs);

        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void GET(){
        UserPreferences newUserPrefs = APIRequests.getJSONObjectFromURLUsingRESTTemplate(currentUrl);
        TheradMessage<UserPreferences> message =
                new TheradMessage<UserPreferences>("msg", newUserPrefs);

        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private  void GET_ALL(){


    }


}