/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class ConfigFilesGenerator {

    private Map<String, String> keywordsReplaceMap = new HashMap<>();
    private List<String> appendLinesList = new ArrayList<String>();

    String fromFilePathStr = "";

    String toFilePathStr = "";

    public ConfigFilesGenerator(String fromFilePath, String toFilePath) {
        this.fromFilePathStr = fromFilePath;
        this.toFilePathStr = toFilePath;
    }

    public ConfigFilesGenerator(String toFilePath) {
        this.toFilePathStr = toFilePath;
    }

    public void generateConfigFile() {
        Path fromfilePath = Paths.get(fromFilePathStr);
        Path tofilePath = Paths.get(toFilePathStr);

        try {
            if (!fromFilePathStr.isEmpty()) {
                List<String> replacedLine = null;
                Stream<String> lines = Files.lines(fromfilePath, Charset.forName("UTF-8"));
                System.out.println("read from file:" + fromfilePath);

                if (!keywordsReplaceMap.isEmpty()) {
                    replacedLine = lines.map(line -> replaceWords(line, keywordsReplaceMap)).collect(Collectors.toList());
                    if (!appendLinesList.isEmpty()) {
                        replacedLine.addAll(appendLinesList);
                    }
                } else {
                    replacedLine = appendLinesList;
                }
                Files.write(tofilePath, replacedLine, Charset.forName("UTF-8"));
                lines.close();
            } else {  // no from file
                Files.write(tofilePath, appendLinesList, Charset.forName("UTF-8"));
            }

            System.out.println("write to file:" + toFilePathStr);

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replaceWords(String str, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {

            if (str.contains(entry.getKey())) {
                str = str.replace(entry.getKey(), entry.getValue());
            }
        }
        return str;
    }

    public Map<String, String> getKeywordsReplaceMap() {
        return keywordsReplaceMap;
    }

    public List<String> getAppendLinesList() {
        return appendLinesList;
    }

}
