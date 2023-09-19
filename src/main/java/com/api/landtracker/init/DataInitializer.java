package com.api.landtracker.init;

import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.repository.ClienteRepository;
import com.api.landtracker.repository.LoteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final LoteRepository loteRepository;
    private final ClienteRepository clienteRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        cargarDesdeJson("clientes.json", Cliente.class, clienteRepository);
        cargarDesdeJson("lotes.json", Lote.class, loteRepository);
    }

    private <T> void cargarDesdeJson(String jsonFilePath, Class<?> clase,  JpaRepository<T, ?> repository) {
        try {
            ClassPathResource resource = new ClassPathResource(jsonFilePath);

            System.out.println("Ruta del archivo JSON: " + resource.getURL().getPath());

            List<T> objetos = objectMapper.readValue(resource.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clase));
            repository.saveAll(objetos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
