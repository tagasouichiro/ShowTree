package crawling.selector;

import crawling.Main;
import crawling.exception.FromBeginningException;
import crawling.file.InputFile;
import crawling.input.URLManager;
import crawling.nodes.NodeFormatter;
import crawling.nodes.NodeList;
import crawling.output.OutputFormat;
import crawling.output.WriteTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by midori on 2016/05/05.
 */
public class ShowTree extends Selector {
    @Override
    public void select() throws IOException, FromBeginningException{
        Main.newLine();
        File dir = new File("data/urlset");
        File files[] = dir.listFiles();
        String in = "";
        int fileNum = 0;

        // File input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.println("■ 取得したいファイルを選択してください");
            for (int i = 0; i < files.length; i++) {
                System.out.println(i + "." + files[i].getName());
            }
            Main.newLine();
            System.out.println("0 ~ " + (files.length - 1) + " を入力");
            System.out.print(">");

            in = br.readLine();

            if(in.equals("back")){
                throw new FromBeginningException();
            }

            if(Pattern.matches("[0-9]*", in)){
                if(Integer.parseInt(in) >= 0 && Integer.parseInt(in) < files.length){
                    break;
                }
            }

            Main.newLine();
            System.out.println("※指定する数を入力してください");
            Main.newLine();
        }

        // urlList取得
        fileNum = Integer.parseInt(in);
        String filepath = files[fileNum].toString();
        InputFile inFile = new InputFile();
        List<String> urlList = new ArrayList<>();
        inFile.inputFile(filepath, urlList);

        // ファイルの1行目はhostURL
        URLManager urlManager = new URLManager(urlList.get(0));
        urlList.remove(0);

        // nodelist 作成
        NodeList nodeList = NodeList.generateNodeList(urlManager.getHost());
        NodeFormatter nodeFormat = new NodeFormatter(nodeList.getHost());
        nodeFormat.addUrlNodeList(urlList);

        // 出力
        OutputFormat outputFormat;
        while(true){
            String regexOut = "[0-" + OutputFormat.values().length + "]";
            Main.newLine();
            System.out.println("■ 出力形式を選択してください");
            int number = 0;
            for(OutputFormat value: OutputFormat.values()){
                System.out.println(number + ". " + value.getName());
                number++;
            }

            Main.newLine();
            System.out.println("0 ~ " + OutputFormat.values().length + " を入力");
            System.out.print(">");
            in = br.readLine();

            if(in == null){
                continue;
            }

            if(in.equals("back")){
                throw new FromBeginningException();
            }

            if(Pattern.matches(regexOut, in)){
                outputFormat = OutputFormat.values()[Integer.parseInt(in)];
                break;
            }
        }

        WriteTree.writeTree(nodeList.getNodeList(), outputFormat, urlManager.getHost());

    }
}
