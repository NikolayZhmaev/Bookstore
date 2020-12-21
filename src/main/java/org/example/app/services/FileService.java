package org.example.app.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// class for working with files
@Service
public class FileService {

    Logger logger = Logger.getLogger(FileService.class);
    private List<File> fileList = new ArrayList<>();
    private List<String> nameFiles = new ArrayList<>();

    @Autowired
    public FileService() {
        setFileList();
        setNameFiles();
    }

    public void uploadFile(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename(); // получим название оригинального файла, вместе с расширением
        byte[] bytes = file.getBytes(); // сохраняем файл в виде массива байтов

        //создаем директорию
        String rootPath = System.getProperty("catalina.home"); // "catalina.home" - свойство отражающее путь до папки сервера содержащей файлы
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //создание файла, сохраняемого на сервер
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
            stream.write(bytes);
        }
        setFileList();
        setNameFiles();
        logger.info("new file saved at: " + serverFile.getAbsolutePath());
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList() {
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        fileList.clear();

        for (File file : dir.listFiles()) {
            if (file.isFile()) { // проверяем что найденный объект именно файл
                fileList.add(file);
            }
        }
        logger.info("number of files in the directory" + fileList.size());
    }

    public List<String> getNameFiles() {
        return nameFiles;
    }

    public void setNameFiles() {
        nameFiles.clear();
        Iterator<File> iter = fileList.iterator();
        while (iter.hasNext()) {
            nameFiles.add(iter.next().getName());
        }
        logger.info("number of names in the directory" + nameFiles.size());
    }
}