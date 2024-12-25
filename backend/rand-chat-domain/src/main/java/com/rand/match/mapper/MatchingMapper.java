package com.rand.match.mapper;

import com.rand.match.model.Match;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchingMapper {
    public void chatRoomCreate(Match match);
    public void chatRoomMemCreate(Match match);
    public Match isExistChatRoom(Match match);
}
