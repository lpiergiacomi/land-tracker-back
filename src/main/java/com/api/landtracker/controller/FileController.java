package com.api.landtracker.controller;

import com.api.landtracker.model.entities.File;
import com.api.landtracker.model.entities.ResponseFile;
import com.api.landtracker.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseFile uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("lotId") Long lotId) throws IOException {
        return fileService.store(file, lotId);
    }

    @GetMapping("/list")
    public List<ResponseFile> getListFiles() {

        return fileService.getAllFiles().map(dbFile -> new ResponseFile(
                dbFile.getName(),
                dbFile.getId(),
                dbFile.getType(),
                dbFile.getData().length)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        File file = fileService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

}
