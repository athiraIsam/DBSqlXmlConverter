package nami.apps.dbsqlxmlconverter.model.fileManager;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

    private static ArrayList<String> allFilesFound = new ArrayList<>();
    private static ArrayList<String> allFilesFoundDir = new ArrayList<>();
    private static String rootDir;


    //false = empty, true = not empty
    public static boolean checkPathIsNotEmpty(String directory)
    {
        try {
            File folder = new File(directory);
            File[] listOfFiles = folder.listFiles();

            if(listOfFiles==null)
                return false;
            if(listOfFiles.length==0)
                return false;

            return true;

        } catch (Exception e)
        {
            System.out.println("Unable to get AllFiles in Directory: " + e);
            return false;
        }
    }
    public static void getAllListOfFile(final String directory)
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getAllFilesInDirectory(directory);
                Thread.interrupted();
            }
        });thread.start();
    }

    public  static HashMap<ArrayList<String>,ArrayList<String>> getItem(String dirPath) {
        ArrayList<String> item;
        ArrayList<String> path;
        HashMap<ArrayList<String>,ArrayList<String>>  storeFile = new HashMap<>();

        if(rootDir == null)
            return null;

        try {
            if (!dirPath.equals("") || dirPath != null) {
                item = new ArrayList<>();
                path = new ArrayList<>();
                File f = new File(dirPath);
                File[] files = f.listFiles();

                if (files != null) {
                    if (!dirPath.equals(rootDir)) {
                        item.add(rootDir);
                        path.add(rootDir);
                        item.add("../");
                        path.add(f.getParent());
                    }

                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];

                        if (!file.isHidden() && file.canRead()) {
                            path.add(file.getPath());
                            if (file.isDirectory()) {
                                item.add(file.getName() + "/");
                            } else {
                                item.add(file.getName());
                            }
                        }
                    }
                    storeFile.put(item,path);
                    return storeFile;
                }
            }

        } catch (Exception e) {
            Log.e("File Manager","Unable to get item: " + e);
        }
        finally {
            return storeFile;
        }
    }

    public static void setRootDir(String directory) {
        FileManager.rootDir = directory;
    }

    /*
    Check if the file is db extension. Example : pkg_user.db
     */
    public static boolean checkIsDbExtension(String fileName)
    {
        if(fileName == null)
            return false;
        if(fileName.equals(""))
            return false;

        String[] split = fileName.split("\\.");

        if(split == null)
            return false;
        if(split.length == 0)
            return false;
        if(!split[split.length-1].equals("db"))
            return false;

        return true;
    }
    private static File getAllFilesInDirectory(String directory) {

        try {
            File folder = new File(directory);
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        //System.out.println("File " + file.getName());
                        allFilesFound.add(file.getName());
                        allFilesFoundDir.add(file.getAbsolutePath());
                    } else if (file.isDirectory()) {
                        File found = getAllFilesInDirectory(file.getAbsolutePath());
                        if (found != null) {
                            // System.out.println("Directory " + file.getName());
                            return found;
                        }
                    }
                }
            } else {
                return null;
            }
            return null;
        } catch (Exception e)
        {
            System.out.println("Unable to get AllFiles in Directory: " + e);
            return null;
        }
    }

    public static ArrayList<String> getAllFilesFound() {
        return allFilesFound;
    }

    public static ArrayList<String> getAllFilesFoundDir() {
        return allFilesFoundDir;
    }
}
