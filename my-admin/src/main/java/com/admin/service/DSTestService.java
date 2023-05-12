package com.admin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DSTestService {

    String test1();

    String test2();

    String test3();

    String test4();

}
