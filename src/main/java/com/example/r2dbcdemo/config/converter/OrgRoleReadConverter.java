package com.example.r2dbcdemo.config.converter;

import com.example.r2dbcdemo.domain.OrgRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mapping.MappingException;

@ReadingConverter
public class OrgRoleReadConverter implements Converter<String, OrgRole> {

    @Override
    public OrgRole convert(String source) {
        for (OrgRole role : OrgRole.values()) {
            if (source.equals(role.getCode())) {
                return role;
            }
        }
        throw new MappingException(String.format("Could not mapping code %s to OrgRole", source));
    }
}
