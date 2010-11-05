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

package com.gooddata.time;

import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Generates the time dimension CSV
 * @author zd@gooddata.com
 * @version 1.0
 */
public class TimeDimensionGenerator {

    public static void main(String[] args) throws IOException {
        TimeDimensionGenerator tg = new TimeDimensionGenerator();
        tg.generate("/Users/zdenek/temp/data.csv");
    }

    DecimalFormat nf = new DecimalFormat("00");

    public void generate(String outputCsv) throws IOException {
        String[] header = {"second_of_day","second","minute_of_day","minute","hour", "hour12", "am_pm","time","time12"};
        CSVWriter cw = FileUtil.createUtf8CsvEscapingWriter(new File(outputCsv));
        cw.writeNext(header);
        for(int sec_of_day = 0; sec_of_day < 24*60*60; sec_of_day ++) {
            String[] row = new String[header.length];
            row[0] = nf.format(sec_of_day);
            int sec = sec_of_day % 60;
            row[1] = nf.format(sec);
            int minute_of_day = sec_of_day/60;
            row[2] = nf.format(minute_of_day);
            int minute = minute_of_day % 60;
            row[3] = nf.format(minute);
            int hour = minute_of_day / 60;
            row[4] = nf.format(hour);
            int hour12 = (hour==12)?(12):(hour % 12);
            row[5] = nf.format(hour12);
            String ampm = (hour < 12)?("AM"):("PM");
            row[6] = ampm;
            String time = nf.format(hour) + ":" + nf.format(minute) + ":" + nf.format(sec);
            row[7] = time;
            String time12 = nf.format(hour12) + ":" + nf.format(minute) + ":" + nf.format(sec);
            row[8] = time12;
            cw.writeNext(row);
        }
        cw.flush();
        cw.close();
    }

}
