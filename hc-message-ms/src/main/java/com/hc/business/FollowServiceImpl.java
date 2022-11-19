package com.hc.business;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 10:05
 * 描述:
 **/
import com.hc.entity.MessengerFollowRelation;
import com.hc.mapper.MessengerFollowRelationMapper;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.struct.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    @Resource
    private MessengerFollowRelationMapper followRelationDao;

    @Override
    public List<String> getFollowerIds(String followKey) {
        List<MessengerFollowRelation> relations = followRelationDao.selectByMap(new Maps("follow_key", followKey));
        return Optional
                .ofNullable(relations)
                .map(fs -> fs.stream().map(MessengerFollowRelation::getFollowerId))
                .orElseThrow(() -> new IedsException("service {} not exist!", followKey))
                .collect(Collectors.toList());
    }
}