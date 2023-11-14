package com.api.landtracker.service;

import com.api.landtracker.model.entities.File;
import com.api.landtracker.model.entities.ResponseFile;
import com.api.landtracker.repository.FileRepository;
import com.api.landtracker.utils.exception.RecordNotFoundHttpException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Test
    void testStore() throws IOException {
        String fileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = "Prueba inicial".getBytes();
        Long lotId = 1L;

        MockMultipartFile inputFile = new MockMultipartFile("file", fileName, contentType, content);

        File expectedFile = new File(fileName, contentType, content, lotId);
        expectedFile.setId("UnId");

        when(fileRepository.save(any(File.class))).thenReturn(expectedFile);

        ResponseFile responseFile = fileService.store(inputFile, lotId);

        verify(fileRepository, times(1)).save(any(File.class));
        assertEquals("test.txt", responseFile.getName());
        assertEquals("UnId", responseFile.getId());
        assertEquals("text/plain", responseFile.getType());
        assertEquals("Prueba inicial".getBytes().length, responseFile.getSize());
    }

    @Test
    void testGetFile() {
        String fileId = "test-file-id";
        File expectedFile = new File();
        expectedFile.setId(fileId);

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(expectedFile));

        File result = fileService.getFile(fileId);

        verify(fileRepository).findById(fileId);
        assertEquals(expectedFile, result);
    }

    @Test
    void testGetFile_RecordNotFound() {
        String fileId = "non-existent-file-id";

        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundHttpException.class, () -> fileService.getFile(fileId));
        verify(fileRepository).findById(fileId);
    }

    @Test
    void testGetAllFiles() {
        File file1 = new File();
        File file2 = new File();

        when(fileRepository.findAll()).thenReturn(List.of(file1, file2));

        Stream<File> result = fileService.getAllFiles();

        assertEquals(List.of(file1, file2), result.collect(Collectors.toList()));
        verify(fileRepository).findAll();
    }

    @Test
    void testGetListFilesByLotId() {
        Long lotId = 1L;
        File file1 = new File();
        File file2 = new File();

        when(fileRepository.findByLotId(lotId)).thenReturn(List.of(file1, file2));

        Stream<File> result = fileService.getListFilesByLotId(lotId);

        assertEquals(List.of(file1, file2), result.collect(Collectors.toList()));
        verify(fileRepository).findByLotId(lotId);
    }

    @Test
    void testDeleteFile() {
        String fileId = "test-file-id";
        File fileToDelete = new File();

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(fileToDelete));

        fileService.deleteFile(fileId);

        verify(fileRepository).findById(fileId);
        verify(fileRepository).deleteById(fileId);
    }

    @Test
    void testDeleteFile_RecordNotFound() {
        String fileId = "non-existent-file-id";

        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundHttpException.class, () -> fileService.deleteFile(fileId));
        verify(fileRepository).findById(fileId);
    }

}
