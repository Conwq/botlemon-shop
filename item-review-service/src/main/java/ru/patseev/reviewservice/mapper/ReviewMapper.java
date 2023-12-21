package ru.patseev.reviewservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.patseev.reviewservice.domain.ReviewEntity;
import ru.patseev.reviewservice.dto.ReviewResponse;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

	@Mappings({
			@Mapping(target = "firstName", source = "userEntity.firstName"),
			@Mapping(target = "lastName", source = "userEntity.lastName")
	})
	ReviewResponse toDto(ReviewEntity reviewEntity);
}
