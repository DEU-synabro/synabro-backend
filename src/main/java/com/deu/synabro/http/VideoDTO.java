package com.deu.synabro.http;

import com.deu.synabro.entity.VideoEntity;
import lombok.Builder;

public class VideoDTO {
    private String work_id;
    private String contents;
    private String page;
    private String url;
    private String file_name;

    public VideoEntity toEntity(){
        VideoEntity videoEntity = VideoEntity.builder()
                .work_id(work_id)
                .contents(contents)
                .page(page)
                .url(url)
                .file_name(file_name)
                .build();
        return videoEntity;
    }


    @Builder
    public VideoDTO(String work_id, String contents, String page, String url, String file_name) {
        this.work_id = work_id;
        this.contents = contents;
        this.page = page;
        this.url = url;
        this.file_name = file_name;
    }
}
