package com.example.adplatform.service;

import com.example.adplatform.dto.SystemUserCreateRequest;
import com.example.adplatform.vo.SystemUserVO;

import java.util.List;

public interface SystemUserService {

    SystemUserVO create(SystemUserCreateRequest request);

    List<SystemUserVO> list();

    SystemUserVO getById(Long id);
}
