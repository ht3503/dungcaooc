package com.javamongo.moviebooktickets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.javamongo.moviebooktickets.service.ImageService;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    
    @Autowired
    private ImageService imageService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile[] files) {
        return ResponseEntity.ok(imageService.addImage(files));
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.deleteImage(id));
    }

}
