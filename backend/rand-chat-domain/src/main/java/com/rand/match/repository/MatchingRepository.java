package com.rand.match.repository;

import com.rand.match.model.Match;

public interface MatchingRepository {
    public void chatRoomCreate(Match match);
    public void chatRoomMemCreate(Match match);
    public Match isExistChatRoom(Match match);
}
