package com.qg24.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SDKService {

    boolean connect(int projectId);
}
