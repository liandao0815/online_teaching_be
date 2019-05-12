package com.liandao.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liandao.onlineteaching.config.CustomException;
import com.liandao.onlineteaching.param.Login;
import com.liandao.onlineteaching.dao.UserMapper;
import com.liandao.onlineteaching.entity.User;
import com.liandao.onlineteaching.param.QueryUser;
import com.liandao.onlineteaching.param.UpdateUser;
import com.liandao.onlineteaching.utils.QiniuyunUtil;
import com.liandao.onlineteaching.utils.ResponseUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService {
    private final String DEFAULT_PASSWORD = "123456";

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secret}")
    private String secret;

    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private QiniuyunUtil qiniuyunUtil;

    @Autowired
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, QiniuyunUtil qiniuyunUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.qiniuyunUtil = qiniuyunUtil;
    }

    @Transactional
    public User login(Login loginParams) {
        User user = userMapper.findByAccount(loginParams.getAccount());

        if (user == null || !loginParams.getRole().equals(user.getRole()))
            throw new CustomException("该用户不存在，请重试");
        if (!passwordEncoder.matches(loginParams.getPassword(), user.getPassword()))
            throw new CustomException("账户名或者密码不正确");

        String token = Jwts.builder()
                .setSubject(user.getAccount() + ":" + Integer.toString(user.getId()))
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setToken(token);
        newUser.setLoginTime(new Timestamp(System.currentTimeMillis()));
        userMapper.update(newUser);

        newUser.setAccount(user.getAccount());
        newUser.setUsername(user.getUsername());
        newUser.setAvatar(user.getAvatar());
        newUser.setRole(user.getRole());
        return newUser;
    }

    @Transactional
    public User getUser(int id, boolean isLogin) {
        User user = userMapper.findById(id);
        User retUser = new User();

        if (user != null) {
            retUser.setId(user.getId());
            retUser.setAccount(user.getAccount());
            retUser.setUsername(user.getUsername());
            retUser.setAvatar(user.getAvatar());
            retUser.setToken(user.getToken());
            retUser.setRole(user.getRole());

            if (isLogin) {
                User newUser = new User();
                newUser.setId(user.getId());
                newUser.setLoginTime(new Timestamp(System.currentTimeMillis()));
                userMapper.update(newUser);
            }
        }

        return retUser;
    }

    @Transactional
    public void addUser(MultipartFile avatar, String account, String username, String role) {
        User user = userMapper.findByAccount(account);
        if (user != null) throw new CustomException("账号" + account + "已经存在，请换个账号");

        String avatarScr = null;
        if (avatar != null) avatarScr = qiniuyunUtil.uploadImage(avatar);

        User newUser = new User();
        newUser.setAccount(account);
        newUser.setUsername(username);
        newUser.setRole(role);
        newUser.setAvatar(avatarScr);
        newUser.setPassword(passwordEncoder.encode(this.DEFAULT_PASSWORD));

        userMapper.create(newUser);
    }

    @Transactional
    public void batchAddUser(MultipartFile file) {
        if (file == null) throw new CustomException("导入用户文件不能为空");
        if (file.getContentType() == null || !file.getContentType().equals("application/vnd.ms-excel"))
            throw new CustomException("文件格式不正确，请修改后再上传");

        List<User> userList = this.convertToList(file);
        userMapper.batchCreate(userList);
    }

    public Map<String, Object> getUserList(QueryUser queryUserParams) {
        User user = new User();
        user.setAccount(queryUserParams.getAccount());
        user.setUsername(queryUserParams.getUsername());
        user.setRole(queryUserParams.getRole());

        PageHelper.startPage(queryUserParams.getPageNum(), queryUserParams.getPageSize());
        List<User> userList = userMapper.getUserList(user);

        PageInfo<User> page = new PageInfo<>(userList);

        Map<String, Object> map = new HashMap<>();
        map.put("list", userList);
        map.put("total", page.getTotal());

        return map;
    }

    public void deleteUser(int id) {
        userMapper.delete(id);
    }

    public void updateUserById(int id, UpdateUser updateUserParams) {
        if (this.existUserAccount(id, updateUserParams.getAccount()))
            throw new CustomException("账号" + updateUserParams.getAccount() + "已经存在，请换个账号");

        User newUser = new User();

        newUser.setId(id);
        newUser.setAccount(updateUserParams.getAccount());
        newUser.setUsername(updateUserParams.getUsername());
        newUser.setRole(updateUserParams.getRole());

        userMapper.update(newUser);
    }

    @Transactional
    public void updateUser(MultipartFile avatar, int id, String account, String username, String password) {
        if (this.existUserAccount(id, account))
            throw new CustomException("账号" + account + "已经存在，请换个账号");

        String avatarScr = null;
        if (avatar != null) avatarScr = qiniuyunUtil.uploadImage(avatar);

        User newUser = new User();
        newUser.setId(id);
        newUser.setAccount(account);
        newUser.setUsername(username);

        if (password != null && !password.isEmpty())
            newUser.setPassword(passwordEncoder.encode(password));
        newUser.setAvatar(avatarScr);

        userMapper.update(newUser);
    }

    @Transactional
    public void updatePassword(int id, String oldPassword, String newPassword) {
        User user = userMapper.findById(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new CustomException("原密码不正确，请重新输入");

        User newUser = new User();
        newUser.setId(id);
        newUser.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(newUser);
    }

    private boolean existUserAccount(int id, String account) {
        User userByName = userMapper.findByAccount(account);

        if(userByName == null) {
            return false;
        } else {
            User userById = userMapper.findById(id);
            return !account.equals(userById.getAccount());
        }
    }

    private List<User> convertToList(MultipartFile file) {
        List<User> userList = new ArrayList<>();

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

            if (physicalNumberOfRows < 1) return null;

            for (int i = 1; i < physicalNumberOfRows; i++) {
                HSSFRow row = sheet.getRow(i);
                User user = new User();

                HSSFCell cell1 = row.getCell(0);
                HSSFCell cell2 = row.getCell(1);
                HSSFCell cell3 = row.getCell(2);

                String[] roles = {"STUDENT", "TEACHER", "ADMIN"};
                String role = "";

                if (cell1 == null)
                    throw new CustomException("第" + i + "行的用户账号不能为空，请修改后再上传");
                if (cell2 == null)
                    throw new CustomException("第" + i + "行的用户名不能为空，请修改后再上传");

                if (cell3 == null) role = "STUDENT";
                else role = cell3.toString();

                String account = cell1.toString();
                String username = cell2.toString();

                if (userMapper.findByAccount(account) != null)
                    throw new CustomException("第" + i + "行的用户账号（" + account + "）已存在，请修改后再上传");

                if (Arrays.stream(roles).noneMatch(role::equals)) role = "STUDENT";

                user.setAccount(account);
                user.setUsername(username);
                user.setRole(role);
                user.setPassword(passwordEncoder.encode(this.DEFAULT_PASSWORD));

                userList.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }
}
