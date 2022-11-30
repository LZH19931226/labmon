package com.hc.service;

import com.hc.po.Userright;

import java.util.List;

public interface UserrightService {
    List<Userright> findALLUserRightInfoByHC(String hospitalcode);
}
