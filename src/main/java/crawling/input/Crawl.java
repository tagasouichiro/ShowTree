package crawling.input;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by midori on 2016/04/19.
 */
public class Crawl {
    private URLManager urlManager  = null;
    final private String SEARCHTAG = "<a href";

    public Crawl(URLManager urlManager){
        this.urlManager = urlManager;
    }

    /*****   ライブラリ利用なし   ****/
    public StringBuffer HtmlToString(){
        StringBuffer str = new StringBuffer();
        try {
            URL url = new URL(urlManager.getUrl());
            String host = url.getHost();

            InputStream input = url.openStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(input));

            StringBuffer line = new StringBuffer();
            while(!line.append(buf.readLine()).toString().equals("null")){
                str.append(line);
                line.setLength(0);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }


    public Set<String> generateUrlSet(StringBuffer str){
        int index = 0;
        int getIndex = 0;
        int CATHEERRORNUM = 10;
        Set<String> urlSet = new HashSet<>();
        char doubleQuotation = '"';
        char singleQuotation = '\'';
        StringBuffer url = new StringBuffer();
        int count = 0;
        while( (getIndex = str.indexOf(SEARCHTAG, getIndex)) != -1 && count < 1000){
            index = getIndex;

            // 最初の”まで進める
            while(str.charAt(index) != doubleQuotation && str.charAt(index) != singleQuotation){
                index++;
                if(index - getIndex > CATHEERRORNUM){
                    break;
                }
            }
            index++;
            if(index - getIndex > CATHEERRORNUM){
                getIndex++;
                continue;
            }

            // assign url URL
            while(str.charAt(index) != doubleQuotation && str.charAt(index) != singleQuotation){
                url.append(str.charAt(index));
                index++;
            }

            // add list
            if(url.indexOf(urlManager.getHost()) != -1 || url.indexOf("/") == 0) {
                urlSet.add(url.toString());
                count++;
            }

            //StringBuffer initialize
            url.setLength(0);
            getIndex++;
        }

        return urlSet;
    }

    /*****  ライブラリ利用なし(ここまで) ****/




    /*****  Jsoup 利用 ****/
    public Set<String> getUrlSet(int hierarchy){
        System.out.println("-----------------");
        Set<String> urlSet = new HashSet<>();
        List<Set<String>> urlSetList = new ArrayList<>();
        fetchUrlSet(urlManager.getUrl(), urlSet);
        urlSetList.add(urlSet);
        hierarchy--;
        if(hierarchy > 0){
            getUrlSet(hierarchy, urlSet, urlSetList);
        }

        HashSet<String> clearUrlSet = new HashSet<>();
        for (Set<String> notCleanUrlSet : urlSetList) {
            for (String urlBuf : notCleanUrlSet) {
                clearUrlSet.add(urlManager.removeUnnecessaryPart(new StringBuffer(urlBuf)));
            }
        }
        return clearUrlSet;
    }

    public void getUrlSet(int hierarchy, Set<String> urlSet, List<Set<String>> urlSetList){
        System.out.println("-----------------");
        Set<String> newUrlSet = new HashSet<>();
        urlSet.forEach(url -> fetchUrlSet(url, newUrlSet));
        urlSetList.add(newUrlSet);
        hierarchy--;
        if(hierarchy > 0){
            getUrlSet(hierarchy, newUrlSet, urlSetList);
        }
    }

    public void fetchUrlSet(String url, Set<String> urlSet){
        String urlStr = "";
        //相対パスの時
        if(url.indexOf(urlManager.getHost()) == -1){
            url = urlManager.getHostUrl() + url;
        }else if(url.indexOf(urlManager.getProtocol()) == -1){
            url = urlManager.getProtocol() + ":"+ url;
        }

        try{
            Document doc = Jsoup.connect(url.trim()).get();
            Thread.sleep(1300);
            Elements links = doc.getElementsByTag("a");
            for (Element link : links){
                urlStr = link.attr("href");
                // host or 相対パス
                if(urlStr.indexOf(urlManager.getHost()) == (urlManager.getProtocol().length() + 3) || (urlStr.indexOf("/") == 0 && urlStr.indexOf("/", 1) != 1) ){
                    urlSet.add(urlStr);
                }
            }

            System.out.println("GetURLs:" + urlSet.size());
        }catch(IOException e){
        }catch (InterruptedException e){}

    }

    /*****  Jsoup 利用(ここまで) ****/

}
