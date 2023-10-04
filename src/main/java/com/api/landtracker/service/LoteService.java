package com.api.landtracker.service;

import com.api.landtracker.model.dto.LoteDTO;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.filter.LoteFilterParams;
import com.api.landtracker.model.filter.LoteSpecification;
import com.api.landtracker.model.mappers.LoteMapper;
import com.api.landtracker.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final LoteMapper loteMapper;

    public List<LoteDTO> obtenerTodosLosLotes() {
        List<Lote> lotes = (List<Lote>) loteRepository.findAll();
        List<LoteDTO> loteDTOS = loteMapper.lotesToLotesDTO(lotes);
        return loteDTOS;
    }
    @Transactional
    public LoteDTO guardarLote(LoteDTO loteDTO) {
        Lote lote = loteMapper.loteDTOToLote(loteDTO);
        LoteDTO loteResponse = loteMapper.loteToLoteDTO(loteRepository.save(lote));
        return loteResponse;
    }

    public LoteDTO obtenerLotePorId(Long id) {
        Lote lote = loteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No se encontró un lote con ese id"));
        LoteDTO loteDTO = loteMapper.loteToLoteDTO(lote);
        return loteDTO;
    }

    public Page<LoteDTO> obtenerLotesConFiltro(LoteFilterParams params, Pageable pageable) {

        Specification<Lote> stagesEquals = LoteSpecification.estadoIgualIn(params.getEstados());
        Specification<Lote> nombreLike = LoteSpecification.loteNombreLike(params.getNombre());
        Specification<Lote> nroCuentaMunicipalLike = LoteSpecification.loteNroCuentaMunicipalLike(params.getNombre());
        Specification<Lote> nroCuentaCatastralLike = LoteSpecification.loteNroCuentaCatastralLike(params.getNombre());
        Specification<Lote> precioBetweenMinMax = LoteSpecification.precioEntreMinMax(params.getPrecioMin(), params.getPrecioMax());

        Page<Lote> lotePage = this.loteRepository.findAll(
                Specification.where(stagesEquals)
                .and(nombreLike.or(nroCuentaMunicipalLike).or(nroCuentaCatastralLike))
                .and(precioBetweenMinMax),
                pageable);

        List<LoteDTO> result = new ArrayList<>();

        result.addAll(loteMapper.lotesToLotesDTO(lotePage.getContent()));

        return new PageImpl<LoteDTO>(result, pageable, lotePage.getTotalElements());
    }
}
