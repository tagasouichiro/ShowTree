package crawling.input;

import crawling.Main;
import crawling.exception.FromBeginningException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by midori on 2016/04/29.
 */
public class InputURL {
    public String inputURL() throws IOException, FromBeginningException{
        StringBuilder url = null;
        int URLMAXLENGTH  = 200;
        URLManager urlManager;

        Main.newLine();
        System.out.println("■ URLを入力してください");
        System.out.println("例: https://ja.wikipedia.org/wiki/メインページ");
        System.out.print(">");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            try{
                url = new StringBuilder(br.readLine().trim());
                if (url == null || url.length() == 0 || url.length() > URLMAXLENGTH) {
                    throw new IOException();
                }

                if(url.toString().equals("back")){
                    throw new FromBeginningException();
                }

                if(isExistURL(url.toString())){
                    urlManager = new URLManager(url.toString());
                    urlManager.removeChildren(url);
                    break;
                }else{
                    throw new IOException();
                }
            }catch (IOException e){
                System.out.println("再入力してください");
                System.out.print(">");
            }
        }

        return url.toString();

    }


    public boolean isExistURL(String urlstr){
        HttpURLConnection conn = null;
        int status = 0;
        try{
            URL url = new URL(urlstr);
            if(urlstr.indexOf("http:") == 0){
                conn = (HttpURLConnection) url.openConnection();
            }else if(urlstr.indexOf("https:") == 0){
                conn = (HttpsURLConnection) url.openConnection();
            }else{
                return false;
            }

            conn.setRequestMethod("HEAD");
            conn.connect();
            status = conn.getResponseCode();
            conn.disconnect();
        }catch (MalformedURLException e){
            return false;
        }catch (IOException e){
            return false;
        }

        if(status == HttpURLConnection.HTTP_OK){
            return true;
        }else {
            return false;
        }
    }
}
