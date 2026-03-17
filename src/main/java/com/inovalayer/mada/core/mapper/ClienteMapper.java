package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.Cliente;
import com.inovalayer.mada.core.dto.ClienteResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteResponseDTO toDto(Cliente cliente);
}
