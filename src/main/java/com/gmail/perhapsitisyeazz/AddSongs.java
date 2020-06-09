package com.gmail.perhapsitisyeazz;

import java.io.*;

import static com.gmail.perhapsitisyeazz.GuitarPlugin.plugin;

public class AddSongs { //THIS SHIT DOESN'T WORK AT ALL

    public static void addSongs(String path) {
        try {
            InputStream stream = plugin.getResource(path);
            byte[] bytes = new byte[0];
            if (stream != null) {
                bytes = new byte[stream.available()];
                stream.read(bytes);
                File file = new File("plugins/GuitarPlugin/" + path);
                OutputStream out = new FileOutputStream(file);
                out.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
