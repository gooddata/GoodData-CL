package com.gooddata.processor;

import com.gooddata.pivotal.PivotalApi;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PT {

    private static Logger l = Logger.getLogger(PT.class);

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        String inDirs = "./data";
        FileWriter script = new FileWriter("run.sh");
        script.write("#! /bin/bash\n");
        script.write("\n");

        File inDir = new File(inDirs);
        File[] csvFiles = inDir.listFiles();
        PivotalApi papi = new PivotalApi("", "", "");
        for(File csvFile : csvFiles) {
            if(csvFile.isDirectory()) {
                String pd = csvFile.getPath();
                String processingDir = pd+"/pivotal/";
                Date dt = fmt.parse(csvFile.getName().split("\\_")[0]);
                System.err.println("Processing file "+pd+"/pivotal/38292.csv");
                papi.parse(pd+"/pivotal/38292.csv",
                    processingDir + System.getProperty("file.separator") + "stories.csv",
                    processingDir + System.getProperty("file.separator") + "labels.csv",
                    processingDir + System.getProperty("file.separator") + "labelsToStories.csv",
                    processingDir + System.getProperty("file.separator") + "snapshots.csv", dt);
                script.write("cd "+processingDir+"\n");
                script.write("cp ../../../cl/examples/pt/*.xml .\n");
                script.write("cp ../../../cl/examples/pt/*.txt .\n");
                script.write("../../../cl/bin/gdi.sh -u zd@gooddata.com -p jindrisska -h zd.users.getgooddata.com -f zd.users.getgooddata.com cmd.txt\n");
                script.write("cd ../../..\n");

                script.write("\n");
                script.flush();
            }
        }
        script.close();

    }
    

}
