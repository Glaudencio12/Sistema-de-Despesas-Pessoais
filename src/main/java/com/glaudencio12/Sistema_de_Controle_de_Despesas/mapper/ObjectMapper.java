package com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <O, D> D parseObject(O origem, Class<D> destino){
        return modelMapper.map(origem, destino);
    }

    public static <O, D> List<D> parseObjects(List<O> origem, Class<D> destino){
        List<D> destinoList = new ArrayList<>();
        origem.forEach(o -> destinoList.add(modelMapper.map(o, destino)));
        return destinoList;
    }
}
