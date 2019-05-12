package com.liandao.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liandao.onlineteaching.config.CustomException;
import com.liandao.onlineteaching.dao.LiveRoomMapper;
import com.liandao.onlineteaching.dao.MessageMapper;
import com.liandao.onlineteaching.dao.UserMapper;
import com.liandao.onlineteaching.entity.LiveRoom;
import com.liandao.onlineteaching.entity.Message;
import com.liandao.onlineteaching.entity.User;
import com.liandao.onlineteaching.param.QueryLiveRoom;
import com.liandao.onlineteaching.param.UpdateLiveRoom;
import com.liandao.onlineteaching.utils.QiniuyunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiveRoomService {
    private LiveRoomMapper liveRoomMapper;
    private UserMapper userMapper;
    private QiniuyunUtil qiniuyunUtil;
    private PasswordEncoder passwordEncoder;
    private MessageMapper messageMapper;

    @Autowired
    public LiveRoomService(LiveRoomMapper liveRoomMapper,
                           QiniuyunUtil qiniuyunUtil,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           MessageMapper messageMapper) {
        this.liveRoomMapper = liveRoomMapper;
        this.qiniuyunUtil = qiniuyunUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.messageMapper = messageMapper;
    }

    public LiveRoom getLiveRoom(int userId) {
        return liveRoomMapper.findByUserId(userId);
    }

    @Transactional
    public void createLiveRoom(MultipartFile poster, String title, String banner, int userId) {
        String posterSrc = qiniuyunUtil.uploadImage(poster);

        LiveRoom liveRoom = liveRoomMapper.findByUserId(userId);
        if (liveRoom != null) throw new CustomException("当前用户已经存在直播间，无需再开通");

        LiveRoom newLiveRoom = new LiveRoom();
        newLiveRoom.setUserId(userId);
        newLiveRoom.setTitle(title);
        newLiveRoom.setBanner(banner);
        newLiveRoom.setPoster(posterSrc);

        liveRoomMapper.create(newLiveRoom);
    }

    @Transactional
    public void updateLiveRoom(MultipartFile poster, int id, String title, String banner, int userId) {
        String posterSrc = null;
        if (poster != null) posterSrc = qiniuyunUtil.uploadImage(poster);

        LiveRoom newLiveRoom = new LiveRoom();

        LiveRoom liveRoom = liveRoomMapper.findById(id);
        String status = liveRoom.getStatus();
        if (status.equals("3")) throw new CustomException("直播间已被冻结，无法修改直播间信息");
        if (status.equals("2")) newLiveRoom.setStatus("0");

        newLiveRoom.setId(id);
        newLiveRoom.setPoster(posterSrc);
        newLiveRoom.setTitle(title);
        newLiveRoom.setBanner(banner);
        newLiveRoom.setUserId(userId);

        liveRoomMapper.update(newLiveRoom);
    }

    public LiveRoom getLiveRoomByAdmin(int id) {
        return liveRoomMapper.findById(id);
    }

    public Map<String, Object> getLiveRoomList(QueryLiveRoom queryLiveRoomParams) {
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setId(queryLiveRoomParams.getId());
        liveRoom.setStatus(queryLiveRoomParams.getStatus());
        liveRoom.setLiving(queryLiveRoomParams.getLiving());

        PageHelper.startPage(queryLiveRoomParams.getPageNum(), queryLiveRoomParams.getPageSize());
        List<LiveRoom> liveRoomList = liveRoomMapper.getLiveRoomList(liveRoom);

        PageInfo<LiveRoom> page = new PageInfo<>(liveRoomList);

        Map<String, Object> map = new HashMap<>();
        map.put("list", liveRoomList);
        map.put("total", page.getTotal());

        return map;
    }

    @Transactional
    public void updateLiveRoomStatus(UpdateLiveRoom updateLiveRoomParams) {
        int id = updateLiveRoomParams.getId();
        String status = updateLiveRoomParams.getStatus();

        LiveRoom liveRoomDetail = liveRoomMapper.findById(id);
        int userId = liveRoomDetail.getUserId();

        Message message = new Message();
        message.setUserId(userId);

        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setId(id);
        liveRoom.setStatus(status);

        String content = "";
        switch (status) {
            case "1":
                if(liveRoomDetail.getStatus().equals("0"))
                    content = "恭喜您，您提交的申请开通直播间已经审核通过，现在可以开通直播了";
                if(liveRoomDetail.getStatus().equals("3"))
                    content = "恭喜您，您被冻结的直播间已被管理员恢复，现在可以开通直播了";
                break;
            case "2":
                content = updateLiveRoomParams.getReason();
                break;
            case "3":
                content = "很遗憾，由于您的直播间存在违规信息，现已被管理员冻结，如需恢复，" +
                        "请<a href='Mailto:liandao0815@gmail.com'>点击此处</a>联系管理员";
        }

        message.setContent(content);
        messageMapper.create(message);
        liveRoomMapper.update(liveRoom);
    }

    public LiveRoom getLiveRoomById(int id) {
        LiveRoom liveRoom = liveRoomMapper.findById(id);

        if (liveRoom == null)
            throw new CustomException("找不到该直播间信息");

        if (liveRoom.getStatus().equals("0") || liveRoom.getStatus().equals("2"))
            throw new CustomException("该直播间正在审核");

        if (liveRoom.getStatus().equals("3"))
            throw new CustomException("该直播间已被冻结");

        return liveRoom;
    }

    @Transactional
    public boolean onPublish(String name, String account, String password) {
        User user = userMapper.findByAccount(account);
        if (user == null || !passwordEncoder.matches(password, user.getPassword()))
            return false;

        LiveRoom liveRoom = liveRoomMapper.findByUserId(user.getId());
        if (liveRoom == null || liveRoom.getId() != Integer.parseInt(name) || !liveRoom.getStatus().equals("1"))
            return false;

        liveRoom.setLiveTime(new Timestamp(System.currentTimeMillis()));
        liveRoom.setLiving("1");
        liveRoomMapper.update(liveRoom);

        return true;
    }

    @Transactional
    public void onPublishDone(String name) {
        LiveRoom liveRoom = liveRoomMapper.findById(Integer.parseInt(name));

        if (liveRoom == null) throw new CustomException("没有找到指定直播间");

        liveRoom.setLiving("0");
        liveRoomMapper.update(liveRoom);
    }
}
