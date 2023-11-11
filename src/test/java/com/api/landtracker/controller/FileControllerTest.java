package com.api.landtracker.controller;

import com.api.landtracker.model.entities.File;
import com.api.landtracker.model.entities.ResponseFile;
import com.api.landtracker.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
@ActiveProfiles("test")
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileController fileController;

    @MockBean
    private FileService fileService;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
    }

    @Test
    void uploadFileTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes(StandardCharsets.UTF_8));

        long lotId = 1L;
        ResponseFile responseFile = new ResponseFile("test.txt", "1", "text/plain", 13);

        when(fileService.store(any(), anyLong())).thenReturn(responseFile);

        mockMvc.perform(multipart("/files/upload")
                        .file(file)
                        .param("lotId", Long.toString(lotId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test.txt"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.type").value("text/plain"))
                .andExpect(jsonPath("$.size").value(13));

        verify(fileService, times(1)).store(any(), anyLong());
    }

    @Test
    void getListFilesTest() throws Exception {
        File file1 = new File("file1.txt", "text/plain", "Hello, World!".getBytes(), 1L);
        file1.setId("File1");
        File file2 = new File("file2.txt", "text/plain", "Test content".getBytes(), 2L);
        file2.setId("File2");
        List<File> files = Arrays.asList(
                file1,
                file2
        );

        when(fileService.getAllFiles()).thenReturn(files.stream());

        mockMvc.perform(get("/files/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(files.size())))
                .andExpect(jsonPath("$[0].name").value("file1.txt"))
                .andExpect(jsonPath("$[0].id").value("File1"))
                .andExpect(jsonPath("$[0].type").value("text/plain"))
                .andExpect(jsonPath("$[0].size").value(13))
                .andExpect(jsonPath("$[1].name").value("file2.txt"))
                .andExpect(jsonPath("$[1].id").value("File2"))
                .andExpect(jsonPath("$[1].type").value("text/plain"))
                .andExpect(jsonPath("$[1].size").value(12));

        verify(fileService, times(1)).getAllFiles();
    }

    @Test
    void getListFilesByLotIdTest() throws Exception {
        Long lotId = 1L;
        File file1 = new File("file1.txt", "text/plain", "Hello, World!".getBytes(), 1L);
        file1.setId("File1");
        File file2 = new File("file2.txt", "text/plain", "Test content".getBytes(), 2L);
        file2.setId("File2");
        List<File> files = Arrays.asList(
                file1,
                file2
        );

        when(fileService.getListFilesByLotId(lotId)).thenReturn(files.stream());

        mockMvc.perform(get("/files/list/{lotId}", lotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(files.size())))
                .andExpect(jsonPath("$[0].name").value("file1.txt"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].type").value("text/plain"))
                .andExpect(jsonPath("$[0].size").exists())
                .andExpect(jsonPath("$[1].name").value("file2.txt"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].type").value("text/plain"))
                .andExpect(jsonPath("$[1].size").exists());

        verify(fileService, times(1)).getListFilesByLotId(lotId);
    }

    @Test
    void getFileTest() throws Exception {
        String fileId = UUID.randomUUID().toString();
        byte[] fileContent = "Hello, World!".getBytes();
        String fileName = "example.txt";

        when(fileService.getFile(fileId)).thenReturn(new File(fileName, "text/plain", fileContent, 1L));

        mockMvc.perform(get("/files/{id}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(fileContent));

        verify(fileService, times(1)).getFile(fileId);
    }

    @Test
    void deleteFileTest() throws Exception {
        String fileId = "example-file-id";

        mockMvc.perform(delete("/files/{id}", fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(fileService, times(1)).deleteFile(fileId);
    }
}
