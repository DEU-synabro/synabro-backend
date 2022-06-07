package com.deu.synabro.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileUtil {

    private final String uploadPath=System.getProperty("user.dir")+"\\download";

    public void File_init(){
        try{
            Files.createDirectories(Paths.get(uploadPath));
        }catch (IOException e){
            throw new RuntimeException("Not Create");
        }
    }

    public String saveDocs(MultipartFile file) throws IOException {
        try{
            if( file.isEmpty() ) {
                throw new Exception("ERROR : File is empty");
            }
            Path root = Paths.get(uploadPath);
            if(!Files.exists(root)){
                File_init();
                log.info("Create \'download\' file to save uploaded files");
            }
            try(InputStream inputStream = file.getInputStream()){
                log.trace("Uploaded file location : " + uploadPath + file.getOriginalFilename());
                Files.copy(inputStream, root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                return file.getOriginalFilename();
            }
        }catch (Exception e){
            throw new RuntimeException("Fail to store the file [" + file.getOriginalFilename() + "]");
        }
    }

    public Resource downloadFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadPath + filename);
        Resource resource = new FileSystemResource(filePath);
        log.info("Download file path, uri: " + uploadPath + "\\" + filename);
        if(resource.exists()) {
            return resource;
        } else {
            log.error("File not found : " + filename);
        }
        return null;
    }
}
