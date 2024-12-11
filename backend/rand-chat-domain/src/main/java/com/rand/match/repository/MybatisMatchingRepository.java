package com.rand.match.repository;

import com.rand.config.rds.ReadOnly;
import com.rand.config.rds.WriteOnly;
import com.rand.match.mapper.MatchingMapper;
import com.rand.match.model.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MybatisMatchingRepository implements MatchingRepository{

    private final MatchingMapper matchingMapper;

    @Override
    @WriteOnly
    public void chatRoomCreate(Match match) {
        matchingMapper.chatRoomCreate(match);
    }

    @Override
    @ReadOnly
    public Match isExistChatRoom(Match match) {
        return matchingMapper.isExistChatRoom(match);
    }
}
