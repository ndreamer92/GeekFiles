package Server;

import zGBFCommon.FSFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class FileSystem {
    private static FileSystem ourInstance = new FileSystem();

    public static FileSystem getInstance() {
        return ourInstance;
    }

    private String rootCatalog;

    private FileSystem() {
        rootCatalog = "src/main/java/Server/FS/";
    }

    public void updateFS(File dir) throws IOException {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory())
                    updateFS(file);
            }
            for (File file : files) {
                int n=0;
                if (file.isFile()) {   //проверяем, файл ли это
                    n++;
                    System.out.println("Файл номер " + n + " найден в " + file.getPath());
                }
            }
        }
    }

    public File getFile(long uid, String filename){
        File file=null;

        return file;
    }

    public LinkedList<FSFile> getUserFileList(String uid){
        LinkedList<FSFile> fileList = new LinkedList();
        File dir = new File(rootCatalog);
        File userDir=null;
        for (File file : dir.listFiles()){
            if (file.isDirectory() & file.getName().equalsIgnoreCase(uid)){
                userDir = new File (rootCatalog + uid.toString());
                break;
            }
        }
        File[] files = userDir.listFiles();
        for (File file: userDir.listFiles()) {
            fileList.add(new FSFile(file.getName(),file.getPath(),file.length(),file.lastModified()));
        }
        return fileList;
    }




}
