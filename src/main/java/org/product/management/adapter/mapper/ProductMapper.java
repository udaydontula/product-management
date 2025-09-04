package org.product.management.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.product.management.adapter.in.rest.dto.ProductRequestResponseDto;
import org.product.management.domain.entity.Product;

@Mapper(componentModel = "cdi")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product convertToProductEntity(ProductRequestResponseDto dto);

    ProductRequestResponseDto convertToProductRequestResponseDTO(Product entity);
}
