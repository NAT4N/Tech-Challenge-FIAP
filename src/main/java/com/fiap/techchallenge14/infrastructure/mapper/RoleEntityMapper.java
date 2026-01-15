package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleEntityMapper {

    @Mapping(target = "type", source = "name", qualifiedByName = "nameToRoleTypeString")
    Role toDomain(RoleEntity entity);

    /**
     * Estratégia:
     * - Se domain.type vier preenchido, prioriza ele pra gerar o name.
     * - Senão usa domain.name direto.
     */
    @Mapping(target = "name", expression = "java(resolveRoleName(domain))")
    RoleEntity toEntity(Role domain);

    @Mapping(target = "name", expression = "java(resolveRoleName(domain))")
    void updateEntityFromDomain(Role domain, @MappingTarget RoleEntity entity);

    @Named("nameToRoleTypeString")
    default String nameToRoleTypeString(String name) {
        if (name == null) return null;
        try {
            return RoleType.valueOf(name).name();
        } catch (Exception ignored) {
            return null;
        }
    }

    default String resolveRoleName(Role domain) {
        if (domain == null) return null;

        // se tiver type, usa como fonte de verdade
        if (domain.getType() != null && !domain.getType().isBlank()) {
            try {
                return RoleType.valueOf(domain.getType()).name();
            } catch (Exception ignored) {
                // se type vier inválido, cai pro name
            }
        }

        // senão usa name
        if (domain.getName() != null && !domain.getName().isBlank()) {
            // se for um enum válido, normaliza
            try {
                return RoleType.valueOf(domain.getName()).name();
            } catch (Exception ignored) {
                return domain.getName().trim();
            }
        }

        return null;
    }
}
