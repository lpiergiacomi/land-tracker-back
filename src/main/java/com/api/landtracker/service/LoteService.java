package com.api.landtracker.service;

import com.api.landtracker.model.dto.LoteDTO;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.mappers.LoteMapper;
import com.api.landtracker.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final LoteMapper loteMapper;

    public List<LoteDTO> obtenerTodosLosLotes() {
        List<Lote> lotes = loteRepository.findAll();
        List<LoteDTO> loteDTOS = loteMapper.lotesToLotesDTO(lotes);
        return loteDTOS;
    }
    public LoteDTO guardarLote(LoteDTO loteDTO) {
        Lote lote = loteMapper.loteDTOToLote(loteDTO);
        LoteDTO loteResponse = loteMapper.loteToLoteDTO(loteRepository.save(lote));
        return loteResponse;
    }

    public LoteDTO obtenerLotePorId(Long id) {
        Lote lote = loteRepository.getReferenceById(id);
        LoteDTO loteDTO = loteMapper.loteToLoteDTO(lote);
        return loteDTO;
    }
}
