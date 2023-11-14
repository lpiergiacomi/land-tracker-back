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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals("Prueba inicial".getBytes().length, responseFile.getSize());    }
}
