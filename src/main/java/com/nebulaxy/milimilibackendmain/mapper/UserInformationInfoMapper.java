package com.nebulaxy.milimilibackendmain.mapper;

import com.nebulaxy.milimilibackendmain.entity.UserInformationInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserInformationInfoMapper {

    List<UserInformationInfo> selectAll();

    List<UserInformationInfo> selectUsers(String nickname);

    List<UserInformationInfo> selectAdmin(String nickname);

    void insertAdmin(UserInformationInfo userInformationInfo);

    void insertUser(UserInformationInfo userInformationInfo);

    @Select("select * from `UserInformationInfo` where role = 0 and username = #{username}")
    UserInformationInfo selectUserByUsername(String username);

    @Select("select * from `UserInformationInfo` where role = 1 and username = #{username}")
    UserInformationInfo selectAdminByUsername(String username);

    void updateByUid(UserInformationInfo userInformationInfo);

    @Delete("delete from `UserInformationInfo` where uid = #{uid}")
    void deleteByUid(Integer uid);

    @Select("select * from `UserInformationInfo` where uid =#{uId} ")
    UserInformationInfo selectByUid(String uId);
}
