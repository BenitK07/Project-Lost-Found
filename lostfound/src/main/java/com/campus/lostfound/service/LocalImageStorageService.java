package com.campus.lostfound.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class LocalImageStorageService implements ImageStorageService {

    private static final String ROOT = "uploads";

    @Override
    public String store(
            MultipartFile file,
            String itemType,
            Long itemId
    ) throws IOException {

        String ext = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));

        String fileName = UUID.randomUUID() + ext;

        File dir = new File(
                ROOT + "/" + itemType.toLowerCase() + "/" + itemId
        );

        if (!dir.exists()) dir.mkdirs();

        File dest = new File(dir, fileName);
        Files.copy(file.getInputStream(), dest.toPath());

        return dest.getPath(); // store path in DB
    }
}
