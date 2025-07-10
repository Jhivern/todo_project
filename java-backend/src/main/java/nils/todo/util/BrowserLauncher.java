package nils.todo.util;

import java.io.IOException;

public class BrowserLauncher {

    // Add OS detection in the future
    /**
     * Opens a browser on a Windows machine using the terminal
     * @param url The url to visit in the browser
     */
    public static void open(String url) {
        try {
            System.out.println("Opening browser to url: " + url);
            Runtime.getRuntime().exec(new String[]{
                    "cmd", "/c", "start", "\"\"", "\"" + url + "\""
            });
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to open browser", e);
        }
    }
}
