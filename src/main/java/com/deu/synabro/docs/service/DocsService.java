package com.deu.synabro.docs.service;

import com.deu.synabro.docs.domain.entity.DocsEntity;
import com.deu.synabro.docs.domain.repository.DocsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocsService {

    @Autowired
    DocsRepository docsRepository;

    public DocsEntity saveDocs(DocsEntity reqEntity, String filename){
        DocsEntity docsEntity= DocsEntity.builder()
                .work_id(reqEntity.getWork_id())
                .contents(reqEntity.getContents())
                .page(reqEntity.getPage())
                .file_name(filename)
                .build();
        return docsRepository.save(docsEntity);
    }
}
