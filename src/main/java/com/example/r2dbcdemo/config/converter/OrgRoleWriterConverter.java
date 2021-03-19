package com.example.r2dbcdemo.config.converter;

import com.example.r2dbcdemo.domain.OrgRole;
import org.springframework.core.convert.converter.Converter;

public class OrgRoleWriterConverter implements Converter<OrgRole, String> {
    @Override
    public String convert(OrgRole source) {
        return source.getCode();
    }
}
