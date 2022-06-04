package com.deu.synabro.service;

import com.deu.synabro.controller.AuthController;
import com.deu.synabro.entity.Work;
import com.deu.synabro.entity.Docs;
import com.deu.synabro.repository.DocsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public void File_init(){
        try{
            Files.createDirectories(Paths.get(uploadPath+"/download"));
            logger.info(uploadPath);
        }catch (IOException e){
            throw new RuntimeException("Not Create");
        }
    }

    public ResponseEntity<Object> downDocs(UUID uuid){
        Docs docs = docsRepository.findByWorkId_Idx(uuid);
        logger.info("현재 작업 경로: " + uploadPath+"/download/");
        try {
            Path filePath = Paths.get(uploadPath+ "/download/" + docs.getFileName());
            logger.info(uploadPath+ "/download/" + docs.getFileName()+ "  ))((");
            String contentType = Files.probeContentType(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(docs.getFileName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));
            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }

    public void saveDocs(MultipartFile file, Work work) throws IOException {
        logger.info("현재 작업 경로: " + uploadPath+"/download");
        try{
            if( file.isEmpty() ) {
                logger.info(file.getOriginalFilename()+" z");
                throw new Exception("ERROR : Fil is empty");
            }
            Path root = Paths.get(uploadPath+"/download");
            if(!Files.exists(root)){
                File_init();
                logger.info(file.getOriginalFilename());
            }
            try(InputStream inputStream = file.getInputStream()){
                Docs docs = new Docs(work,file.getOriginalFilename());
                docsRepository.save(docs);
                Files.copy(inputStream, root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
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
