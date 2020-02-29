package com.bamboo.service;

import com.bamboo.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type queryType(Long id);

    Type queryTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    Type modifyType(Long id, Type type);

    void removeType(Long id);
}
