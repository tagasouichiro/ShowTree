package crawling.selector;

import crawling.Main;
import crawling.exception.FromBeginningException;
import crawling.file.WriteFile;
import crawling.input.Crawl;
import crawling.input.InputURL;
import crawling.input.URLManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * Created by midori on 2016/05/05.
 */
public class CrawlUrl extends Selector{
    @Override
    public void select() throws IOException, FromBeginningException{
        int hierarchy = 0;
        int MAXHIERARCHY = 3;
        String line = "";
        Main.newLine();
        System.out.println("■ URLを探索します");
        System.out.println("調べる階層を選択してください");
        System.out.println("※ 2以上は時間がかかります");
        Main.newLine();
        System.out.println("1 ~ " + MAXHIERARCHY +" を入力");
        System.out.print(">");

        // 階層取得
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            line = br.readLine();

            if(line == null){
                throw new IOException();
            }

            if(line.equals("back")){
                throw new FromBeginningException();
            }

            for(int i = 1; i <= MAXHIERARCHY; i++){
                if(line.equals(String.valueOf(i))){
                    hierarchy = i;
                    break;
                }
            }

            if(hierarchy <= 0 || hierarchy > MAXHIERARCHY){
                System.out.println("1 ~ " + MAXHIERARCHY + "の数値を再入力してください");
                System.out.print(">");
                continue;
            }

            break;
        }

        // URL取得
        InputURL input = new InputURL();
        URLManager urlManager = new URLManager(input.inputURL());

        // Crawling
        Crawl crawl = new Crawl(urlManager);
        Set<String> urlSet = crawl.getUrlSet(hierarchy);

        // File書き出し
        WriteFile write = new WriteFile();
        write.writeUrlSetFile(urlSet, "URLSet_" + urlManager.getHost(), urlManager.getUrl());

        return;

    }
}
