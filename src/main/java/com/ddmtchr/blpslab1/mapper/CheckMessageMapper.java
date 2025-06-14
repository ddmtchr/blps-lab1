package com.ddmtchr.blpslab1.mapper;

import com.ddmtchr.blpslab1.dto.producer.CheckDto;
import com.ddmtchr.blpslab1.entity.CheckMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CheckMessageMapper {
    CheckMessageMapper INSTANCE = Mappers.getMapper(CheckMessageMapper.class);

    @Mapping(target = "sent", ignore = true)
    CheckMessage toEntity(CheckDto dto);

    CheckDto toDto(CheckMessage e);
}
