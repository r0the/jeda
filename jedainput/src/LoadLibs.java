
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

class LoadLibs {

    void loadLibs() {
        loadNativeLibrary("jinput-dx8.dll");
        loadNativeLibrary("jinput-dx8_64.dll");
        loadNativeLibrary("jinput-raw_64.dll");
        loadNativeLibrary("jinput-raw.dll");
        loadNativeLibrary("jinput-wintab.dll");
        loadNativeLibrary("libjinput-linux.so");
        loadNativeLibrary("libjinput-linux64.so");
        loadNativeLibrary("libjinput-osx.jnilib");
    }

    private static void loadNativeLibrary(String libraryName) {
        final InputStream inputStream = LoadLibs.class.getClassLoader().getResourceAsStream(libraryName);
        try {
            final String tempDir = System.getProperty("java.io.tmpdir");
            System.setProperty("net.java.games.input.librarypath", tempDir);
            final File libraryFile = new File(tempDir, libraryName);
            libraryFile.deleteOnExit();
            FileOutputStream fileOutputStream = new FileOutputStream(libraryFile);
            final byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();
        }
        catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
}
