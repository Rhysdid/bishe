package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CleanInputCache extends Thread {
    private InputStream is;
    private String type;

    public CleanInputCache(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                System.out.println(type + ">>>" + line);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}