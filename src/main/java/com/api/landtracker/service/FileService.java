package com.api.landtracker.service;

import com.api.landtracker.model.entities.File;
import com.api.landtracker.model.entities.ResponseFile;
import com.api.landtracker.repository.FileRepository;
import com.api.landtracker.utils.exception.RecordNotFoundHttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public ResponseFile store(MultipartFile inputFile, Long lotId) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(inputFile.getOriginalFilename()));
        File dbFile = fileRepository.save(new File(fileName, inputFile.getContentType(), inputFile.getBytes(), lotId));

        return new ResponseFile(
                dbFile.getName(),
                dbFile.getId(),
                dbFile.getType(),
                dbFile.getData().length);
    }

    public File getFile(String id) {
        return fileRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundHttpException("No existe un archivo con Id: " + id));
    }

    public Stream<File> getListFilesByLotId(Long lotId) {
        return fileRepository.findByLotId(lotId).stream();
    }

    public void deleteFile(String id) {
        fileRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundHttpException("No existe un archivo con Id: " + id));
        fileRepository.deleteById(id);
    }
}
