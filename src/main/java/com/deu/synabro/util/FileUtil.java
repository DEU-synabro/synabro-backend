package com.deu.synabro.util;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.entity.Video;
import com.deu.synabro.repository.DocsRepository;
import com.deu.synabro.repository.VideoRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    VideoRepository videoRepository;

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

    public List<Docs> saveFiles(List<MultipartFile> files) {
        List<Docs> docsList = new ArrayList<>();
        for (MultipartFile file : files){
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
                    docsList.add(docs);
                }
            }catch (Exception e){
                throw new RuntimeException("Not store the file ");
            }
        }
        return docsList;
    }

    public Video saveVideo(MultipartFile file) {

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
                Video video = Video.builder()
                        .fileName(file.getOriginalFilename())
                        .build();
                videoRepository.save(video);
                Files.copy(inputStream, root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                return video;
            }
        }catch (Exception e){
            throw new RuntimeException("Not store the file ");
        }
    }

    public Resource downloadFile(String filename) {
        Path filePath = Paths.get(uploadPath + filename);
        Resource resource = new FileSystemResource(filePath);
        if(resource.exists()) {
            return resource;
        }
        return null;
    }

    /**
     * 첨부 파일을 다운로드 해주는 메소드입니다.
     *
     * @param uuid 첨부 파일이 들어갈 글의 uuid 입니다
     * @return 첨부 파일을 반환합니다.
     */
    public ResponseEntity<Object> downDocs(UUID uuid){
        Docs docs = docsRepository.findById(uuid).orElseThrow(() -> new NullPointerException());
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
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url ",resource.getURL());
            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }

    @CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"}, maxAge = 3600)
    public ResponseEntity<Object> downVideo(UUID uuid){
        Video video = videoRepository.findByWork_Idx(uuid);
        try {
            String FILE_PATH = System.getProperty("user.dir")+"/"+uploadPath + "/" + video.getFileName();

            Path filePath = Paths.get(FILE_PATH);
            String contentType = Files.probeContentType(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(video.getFileName()).build());
            headers.set("Content-Disposition", "attachment; filename=" + video.getFileName());   // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new FileSystemResource(filePath);

            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }
}
