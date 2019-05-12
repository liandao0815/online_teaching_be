package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.LiveRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LiveRoomMapper {
    LiveRoom findByUserId(int userId);
    LiveRoom findById(int Id);
    void create(LiveRoom liveRoom);
    void update(LiveRoom liveRoom);
    List<LiveRoom> getLiveRoomList(LiveRoom liveRoom);
}
