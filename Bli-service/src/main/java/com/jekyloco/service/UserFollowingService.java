package com.jekyloco.service;

import com.jekyloco.constant.UserConstant;
import com.jekyloco.dao.UserFollowingDao;
import com.jekyloco.domain.FollowingGroup;
import com.jekyloco.domain.User;
import com.jekyloco.domain.UserFollowing;
import com.jekyloco.domain.UserInfo;
import com.jekyloco.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;

    @Autowired
    private FollowingGroupService followingGroupService;

    //不直接用Dao来达到松耦合的模式
    @Autowired
    private UserService userService;

    //建立用户关注信息（添加、更新（删除and添加））
    //事务处理，该方法中的操作要么都成功，要么都不做。发生中断则回滚
    //该update操作由delete和add完成，必须保证原子性
    @Transactional
    public void addUserFollowing(UserFollowing userFollowing) {
        //关注分组ID
        Long groupId = userFollowing.getGroupId();
        if (groupId == null) {
            //没有传入分组ID则设置为默认分组
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            //否则获取分组ID并判断分组是否存在
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if (followingGroup == null) {
                throw new ConditionException("关注的分组不存在");
            }
        }
        Long followingId = userFollowing.getFollowingId();
        User user = userService.getUserById(followingId);
        if (user == null) {
            throw new ConditionException("关注的用户不存在！");
        }
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), followingId);
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);
    }

    //第一步：获取关注的用户列表
    //第二步：根据关注用户的id查询关注用户的基本信息
    //第三步：将关注用户按关注分组进行分类
    public List<FollowingGroup> getUserFollowings(Long userId) {
        //获取关注的用户列表
        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        //根据关注用户的id查询关注用户的基本信息
        List<UserInfo> userInfoList = new ArrayList<>();
        if (followingIdSet.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }
        for (UserFollowing userFollowing : list) {
            for (UserInfo userInfo : userInfoList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        //获取用户的分组信息
        //将用户关注的用户分组存入FollowingGroup中
        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for (FollowingGroup group : groupList) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                if (group.getId().equals(userFollowing.getGroupId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        return result;
    }

    /**
     * 获取粉丝列表
     * 同时查询并存储粉丝的基本信息并设置粉丝是否存在互粉状态
     *
     * @param userId
     * @return
     */
    public List<UserFollowing> getUserFans(Long userId) {
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);
        //粉丝用户列表
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (fanIdSet.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }
        List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId);
        for (UserFollowing fan : fanList) {
            //根据粉丝的用户Id查询基本信息
            for (UserInfo userInfo : userInfoList) {
                if (fan.getUserId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }
            //设置当前用户与粉丝的互粉状态followed
            for (UserFollowing following : followingList) {
                if (following.getFollowingId().equals(fan.getUserId())) {
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return fanList;
    }

    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followingGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupService.getUserFollowingGroups(userId);
    }

    public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId) {
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowings(userId);
        for (UserInfo userInfo : userInfoList) {
            userInfo.setFollowed(false);
            for (UserFollowing userFollowing : userFollowingList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(true);
                }
            }
        }
        return userInfoList;
    }
}
