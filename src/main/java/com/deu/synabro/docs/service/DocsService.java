package com.deu.synabro.docs.service;

import com.deu.synabro.docs.domain.entity.DocsEntity;
import com.deu.synabro.docs.domain.repository.DocsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class DocsService {

    @Autowired
    DocsRepository docsRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public void File_init(){
        try{
            Files.createDirectories(Paths.get(uploadPath));
            System.out.print(uploadPath);
        }catch (IOException e){
            throw new RuntimeException("Not Create");
        }
    }

    public void saveDocs(MultipartFile file, String work_id, String contents, String page) throws IOException {
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
                DocsEntity docsEntity = new DocsEntity(work_id,contents,page, file.getOriginalFilename());
//                DocsEntity docsEntity= DocsEntity.builder()
//                        .work_id(work_id)
//                        .contents(contents)
//                        .page(page)
//                        .file_name(file.getOriginalFilename())
//                        .build();
                docsRepository.save(docsEntity);
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
