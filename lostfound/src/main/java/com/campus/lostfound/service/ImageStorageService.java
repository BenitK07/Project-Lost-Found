package com.campus.lostfound.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {

    String store(
            MultipartFile file,
            String itemType,
            Long itemId
    ) throws IOException;
}
