/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.fekrado.movejpeg.transferFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author fekrado
 */
public class TransferFile {

    private String srcFolder; // Source Folder
    private String destFolder; // Destination Folder

    private String file_extension; // file extensions
    private ArrayList<String> extensionList;//List of extensions
    private boolean AllFiles = false;

    private int filecount = 0;
    private final PrintStream logStream = System.out; //Log Stream

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        args = new String[3];
        args[0] = "D:\\mobile\\download";
        args[1] = "D:\\out\\";
        args[2] = ".pdf,.jpg";
        try {

            if (args.length == 2 || args.length == 3) {
                TransferFile transfer = new TransferFile();

                transfer.srcFolder = args[0];
                transfer.destFolder = args[1];
                if (args.length == 3) {
                    transfer.file_extension = args[2];
                    transfer.ReadFileExtension();
                }
                transfer.PrintParameters();
                transfer.Init();
                transfer.Transfer(transfer.srcFolder);
                transfer.Done();
            } else {
                System.out.println("TransferFile run example:");
                System.out.println("java -jar TransferFile.jar <Source Folder> <Destination Folder>");
                System.out.println("java -jar TransferFile.jar <Source Folder> <Destination Folder> <file extensions with comma separated>");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }
    }

    /**
     *
     */
    private TransferFile() {

    }

    /**
     *
     * @param dir
     * @return
     */
    private boolean CheckDir(String dir) {
        return Files.exists(Paths.get(dir));
    }

    /**
     *
     */
    private void Init() {
        if (CheckDir(srcFolder) || CheckDir(destFolder)) {
        }

    }

    /**
     *
     * @param path
     */
    private void Transfer(String path) {

        File root = new File(path);
        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                Transfer(f.getAbsolutePath());
                log("Directory " + f.toString());
            } else {
                try {
                    
                    Optional<String> extension = getExtensionByStringHandling(f.toString());
                    boolean contains = extensionList.contains("."+extension.get());
                    System.out.println(""+f.toString()+"  "+contains);
                    File destFile = new File(destFolder + f.getName());
                    Files.copy(f.toPath(), destFile.toPath());
                    filecount++;

                } catch (FileAlreadyExistsException ex) {
                    log("File: " + f.getName() + " AlreadyExists");
                } catch (IOException ex) {
                    log(ex.toString());
                }

            }
        }
    }

    /**
     *
     */
    private void ReadFileExtension() {
        extensionList = new ArrayList<>();
        if (!"".equals(file_extension)) {
            if (file_extension.contains(",")) {
                String[] extensions = file_extension.split(",");
                if (extensions.length > 1) {
                    for (String ext : extensions) {
                        ext = ext.trim();
                        extensionList.add(ext);
                    }
                } else {
                    extensionList.add(extensions[0]);
                }
            } else {
                AllFiles = true;
            }
        }
    }

    /**
     *
     * @param logstring
     */
    private void log(String logstring) {
        logStream.println(logstring);
    }

    /**
     *
     */
    private void PrintParameters() {
        log("Parameters: ");
        log("Source directory: " + srcFolder);
        log("Destination directory: " + destFolder);
        log("File types :");
        if (extensionList != null) {
            int size = extensionList.size();
            if (size != 0) {
                for (String string : extensionList) {
                    log("\t " + string);
                }
            } else {
                log("All types");
            }
        }

    }
    
    public Optional<String> getExtensionByStringHandling(String filename) {
        
            return Optional.ofNullable(filename)
      .filter(f -> f.contains("."))
      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
}

    /**
     *
     */
    private void Done() {
        System.out.println("Transfer is done");
        System.out.println("Transfer " + filecount + " files");

    }

}
