package com.inovalayer.mada.core.mapper;

import com.inovalayer.mada.core.domain.Cliente;
import com.inovalayer.mada.core.dto.ClienteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "nomeRazaoSocial", source = "razaoSocial")
    @Mapping(target = "nomeCompleto", source = "usuario.nomeCompleto")
    @Mapping(target = "email", source = "usuario.email")
    ClienteResponseDTO toDto(Cliente cliente);
}
