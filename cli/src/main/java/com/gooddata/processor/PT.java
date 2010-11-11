/*
 * Copyright (c) 2009, GoodData Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and
 *        the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the GoodData Corporation nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.processor;

import com.gooddata.pivotal.PivotalApi;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileWriter;

public class PT {

    private static Logger l = Logger.getLogger(PT.class);

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");

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
                DateTime dt = fmt.parseDateTime(csvFile.getName().split("\\_")[0]);
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
