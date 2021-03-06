package crawling.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by midori on 2016/05/05.
 */
public class InputFile{
    public void inputFile(String filepath, Collection<String> col) throws IOException{
        Path path = Paths.get(filepath);
        String line = "";
        BufferedReader br = Files.newBufferedReader(path);
        while (true){
            line = br.readLine();
            if(line == null){
                break;
            }

            if(line.equals("")){
                continue;
            }
            col.add(line);
        }
    }
}
