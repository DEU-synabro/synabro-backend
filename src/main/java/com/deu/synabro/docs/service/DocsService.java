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

    public DocsEntity saveDocs(DocsEntity reqEntity, MultipartFile file) throws IOException {

        if( !file.isEmpty() ) {
            File newFile = new File(file.getName());
            file.transferTo(newFile);
        }

        DocsEntity docsEntity= DocsEntity.builder()
                .work_id(reqEntity.getWork_id())
                .contents(reqEntity.getContents())
                .page(reqEntity.getPage())
                .file_name(file.getOriginalFilename())
                .build();
        return docsRepository.save(docsEntity);
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
