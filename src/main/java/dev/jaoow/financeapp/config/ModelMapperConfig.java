package dev.jaoow.financeapp.config;

import dev.jaoow.financeapp.entity.Role;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Define the mapping for Role to String based on the "name" field
        TypeMap<Role, String> roleToStringTypeMap = modelMapper.createTypeMap(Role.class, String.class);
        roleToStringTypeMap.setConverter(context -> context.getSource().getName());

        return modelMapper;
    }
}
