package com.campus.lostfound.controller;

import com.campus.lostfound.entity.ItemImage;
import com.campus.lostfound.entity.LostItem;
import com.campus.lostfound.entity.FoundItem;
import com.campus.lostfound.repository.*;
import com.campus.lostfound.security.RoleUtil;
import com.campus.lostfound.service.ImageStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;

@RestController
@RequestMapping("/api/images")
public class ItemImageController {

    private final ImageStorageService storageService;
    private final ItemImageRepository imageRepo;
    private final LostItemRepository lostRepo;
    private final FoundItemRepository foundRepo;

    public ItemImageController(
            ImageStorageService storageService,
            ItemImageRepository imageRepo,
            LostItemRepository lostRepo,
            FoundItemRepository foundRepo
    ) {
        this.storageService = storageService;
        this.imageRepo = imageRepo;
        this.lostRepo = lostRepo;
        this.foundRepo = foundRepo;
    }

    // 📸 UPLOAD IMAGE
    @PostMapping("/{type}/{itemId}")
    public String upload(
            @PathVariable String type,
            @PathVariable Long itemId,
            @RequestParam MultipartFile file,
            HttpServletRequest request
    ) throws IOException {

        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Students only");
        }

        String email = (String) request.getAttribute("userEmail");

        if ("lost".equals(type)) {
            LostItem item = lostRepo.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Lost item not found"));
            if (!email.equals(item.getOwnerEmail())) {
                throw new RuntimeException("Not owner");
            }
        } else if ("found".equals(type)) {
            FoundItem item = foundRepo.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Found item not found"));
            if (!email.equals(item.getFinderEmail())) {
                throw new RuntimeException("Not owner");
            }
        } else {
            throw new RuntimeException("Invalid type");
        }

        String path = storageService.store(file, type, itemId);

        ItemImage img = new ItemImage();
        img.setItemType(type.toUpperCase());
        img.setItemId(itemId);
        img.setFilePath(path);

        imageRepo.save(img);

        return "Image uploaded";
    }

    // 🖼️ VIEW IMAGES
    @GetMapping("/{type}/{itemId}")
    public List<ItemImage> images(
            @PathVariable String type,
            @PathVariable Long itemId
    ) {
        return imageRepo.findByItemTypeAndItemId(
                type.toUpperCase(), itemId
        );
    }

    @GetMapping("/view/{imageId}")
    public ResponseEntity<FileSystemResource> viewImage(@PathVariable Long imageId) {

        ItemImage img = imageRepo.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        File file = new File(img.getFilePath());

        if (!file.exists()) {
            throw new RuntimeException("File missing on server");
        }

        MediaType mediaType;

        if (img.getFilePath().endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (img.getFilePath().endsWith(".jpg") || img.getFilePath().endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }
}
