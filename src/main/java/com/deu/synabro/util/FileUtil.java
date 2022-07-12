package com.deu.synabro.util;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.repository.DocsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 파일 저장을 도와주는 클래스입니다.
 *
 * @author tkfdkskarl56
 * @since 1.0
 */
@Service
public class FileUtil {

    @Autowired
    DocsRepository docsRepository;

    private String uploadPath="files";

    /**
     * 첨부 파일을 저장시킬 Docs 객체를 만들어주는 메소드입니다.
     *
     * @param file 저장할 첨부 파일입니다.
     * @return 첨부 파일이 저장된 Docs 객체를 반환합니다.
     */
    public Docs saveDocs(MultipartFile file) {

        try{
            if( file.isEmpty() ) {
                throw new Exception("ERROR : Fil is empty");
            }
            Path root = Paths.get(uploadPath);
            if(!new File(uploadPath).exists()){
                new File(uploadPath).mkdir();
                System.out.println(file.getOriginalFilename());
            }
            try(InputStream inputStream = file.getInputStream()){
                Docs docs = Docs.builder()
                        .fileName(file.getOriginalFilename())
                        .build();
                docsRepository.save(docs);
                Files.copy(inputStream, root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                return docs;
            }
        }catch (Exception e){
            throw new RuntimeException("Not store the file ");
        }
    }

    /**
     * 첨부 파일을 다운로드 해주는 메소드입니다.
     *
     * @param uuid 첨부 파일이 들어갈 글의 uuid 입니다
     * @return 첨부 파일을 반환합니다.
     */
    @CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, maxAge = 3600)
    public ResponseEntity<Object> downDocs(UUID uuid){
        Docs docs = docsRepository.findByWork_Idx(uuid);
        try {
            String FILE_PATH = System.getProperty("user.dir")+"/"+uploadPath + "/" + docs.getFileName();

            Path filePath = Paths.get(FILE_PATH);
            String contentType = Files.probeContentType(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(docs.getFileName()).build());
            headers.set("Content-Disposition", "attachment; filename=" + docs.getFileName());   // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new FileSystemResource(filePath);

            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }
}
