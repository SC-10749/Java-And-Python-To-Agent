package com.example.adplatform.service.impl;

import com.example.adplatform.common.ErrorCode;
import com.example.adplatform.domain.entity.SystemUser;
import com.example.adplatform.dto.SystemUserCreateRequest;
import com.example.adplatform.exception.BusinessException;
import com.example.adplatform.repository.SystemUserRepository;
import com.example.adplatform.service.SystemUserService;
import com.example.adplatform.vo.SystemUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemUserServiceImpl implements SystemUserService {

    private final SystemUserRepository systemUserRepository;

    @Override
    public SystemUserVO create(SystemUserCreateRequest request) {
        systemUserRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new BusinessException(422, "username already exists");
                });

        LocalDateTime now = LocalDateTime.now();
        SystemUser user = SystemUser.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .status(1)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return toVO(systemUserRepository.save(user));
    }

    @Override
    public List<SystemUserVO> list() {
        return systemUserRepository.findAll()
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public SystemUserVO getById(Long id) {
        SystemUser user = systemUserRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        return toVO(user);
    }

    private SystemUserVO toVO(SystemUser user) {
        SystemUserVO vo = new SystemUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());
        vo.setCreatedAt(user.getCreatedAt());
        vo.setUpdatedAt(user.getUpdatedAt());
        return vo;
    }
}
