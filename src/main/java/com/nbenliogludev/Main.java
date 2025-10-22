package com.nbenliogludev;

import com.nbenliogludev.config.ConfigLoader;
import com.nbenliogludev.web.WebServer;

/**
 * @author nbenliogludev
 */
public class Main {
    public static void main(String[] args) {
        new WebServer(ConfigLoader.load()).start();
    }
}