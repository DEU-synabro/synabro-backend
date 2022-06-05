package com.deu.synabro.service;

import com.deu.synabro.controller.AuthController;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.Docs;
import com.deu.synabro.repository.DocsRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class DocsService {

    @Autowired
    DocsRepository docsRepository;

//    @Value("${spring.servlet.multipart.location}")
    private String uploadPath=System.getProperty("user.dir");
    private static final String BASE_PATH = new File("").getAbsolutePath();
    private static String RESOURCE_PATH = "/src/resource";
    private static String FILE_PATH;

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public void File_init(){
        try{
            Files.createDirectories(Paths.get(FILE_PATH));
            logger.info(FILE_PATH);
        }catch (IOException e){
            throw new RuntimeException("Not Create");
        }
    }

    @CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, maxAge = 3600)
    public ResponseEntity<Object> downDocs(UUID uuid){
        Docs docs = docsRepository.findByWorkId_Idx(uuid);
        System.out.println(docs.getFileName());
        try {
            FILE_PATH = BASE_PATH + RESOURCE_PATH + docs.getFileName();
            if (Files.exists(Paths.get(FILE_PATH))) {
                logger.info("Run in IDE!");
            } else {
                FILE_PATH = BASE_PATH + "/resource" + docs.getFileName();
                logger.info("Run executable JAR!\n");
            }

            Path filePath = Paths.get(FILE_PATH);
            String contentType = Files.probeContentType(filePath);
            logger.info("현재 작업 경로: " + filePath);
            System.out.println(filePath+ " _)))");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(docs.getFileName()).build());
            headers.set("Content-Disposition", "attachment; filename=" + docs.getFileName());
//            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(docs.getFileName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new FileSystemResource(filePath);
            logger.info(String.valueOf(headers));
            logger.info(String.valueOf(resource));

            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }

    @CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, maxAge = 3600)
    public JSONObject downDocsLink(UUID uuid){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Docs docs = docsRepository.findByWorkId_Idx(uuid);
        System.out.println(docs.getFileName());
        try {
            Path filePath = Paths.get(uploadPath + "/download/" + docs.getFileName());
            String contentType = Files.probeContentType(filePath);
            logger.info("현재 작업 경로: " + filePath);
            System.out.println(filePath+ " _)))");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(docs.getFileName()).build());
            headers.set("Content-Disposition", "attachment; filename=" + docs.getFileName());
//            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(docs.getFileName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new FileSystemResource(filePath);
            logger.info(String.valueOf(headers));
            logger.info(String.valueOf(resource));
            logger.info(String.valueOf(resource.getURL()));
            logger.info(String.valueOf(resource.getFile()));
            jsonObject.put("url: ","http://18.116.2.111:8080/api/works/download/"+uuid);
            jsonObject.put("url2: ",resource.getFile());
            jsonObject.put("url3: ",resource.getURL());
//            jsonObject.put("resource", resource);
            return jsonObject;
        } catch(Exception e) {
            throw new RuntimeException("Not store the file ");
        }
    }

//    public void saveDocs(MultipartFile file, Work work) throws IOException {
//        logger.info("경로 " +ClassLoader.getSystemClassLoader().getResource(".").getPath());
//
//        logger.info("현재 작업 경로: " + uploadPath + "/download");
//        try{
//            if( file.isEmpty() ) {
//                System.out.println(file.getOriginalFilename());
//                throw new Exception("ERROR : Fil is empty");
//            }
//            Path root = Paths.get(uploadPath+"/download");
//            if(!Files.exists(root)){
//                File_init();
//                System.out.println(file.getOriginalFilename());
//            }
//            try(InputStream inputStream = file.getInputStream()){
//                Docs docs = new Docs(work,file.getOriginalFilename());
//                docsRepository.save(docs);
//                Files.copy(inputStream, root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//                logger.info(String.valueOf(inputStream));
//                logger.info(String.valueOf(root.resolve(file.getOriginalFilename())));
//            }
//        }catch (Exception e){
//            throw new RuntimeException("Not store the file ");
//        }
//    }

    public void saveDocs(MultipartFile file, Work work) throws IOException {

        FILE_PATH = BASE_PATH + RESOURCE_PATH ;
        if (Files.exists(Paths.get(FILE_PATH))) {
            logger.info("Run in IDE!");
        } else {
            FILE_PATH = BASE_PATH + "/resource" ;
            logger.info("Run executable JAR!\n");
        }
        logger.info("경로: "+FILE_PATH);
//        logger.info("현재 작업 경로: " + uploadPath + "/download");
        try{
            if( file.isEmpty() ) {
                System.out.println(file.getOriginalFilename());
                throw new Exception("ERROR : Fil is empty");
            }
            Path root = Paths.get(FILE_PATH);
            if(!Files.exists(root)){
                File_init();
                System.out.println(file.getOriginalFilename());
            }
            try(InputStream inputStream = file.getInputStream()){
                Docs docs = new Docs(work,file.getOriginalFilename());
                docsRepository.save(docs);
                Files.copy(inputStream, root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                logger.info(String.valueOf(inputStream));
                logger.info(String.valueOf(root.resolve(file.getOriginalFilename())));
            }
        }catch (Exception e){
            throw new RuntimeException("Not store the file ");
        }
    }

        public void saveFile(MultipartFile file)  {
       System.out.print(uploadPath);
        try{
            if( file.isEmpty() ) {
                System.out.println(file.getOriginalFilename());
                throw new Exception("ERROR : Fil is empty");
            }
            Path root = Paths.get(uploadPath);
            if(!Files.exists(root)){
                File_init();
                System.out.println(file.getOriginalFilename());
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (Exception e){
            throw new RuntimeException("Not store the file ");
        }
    }
}
