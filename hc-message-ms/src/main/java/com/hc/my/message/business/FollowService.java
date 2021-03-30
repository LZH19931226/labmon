package com.hc.my.message.business;

import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 10:04
 * 描述:
 **/
public interface FollowService {

    List<String> getFollowerIds(String followKey);
}
