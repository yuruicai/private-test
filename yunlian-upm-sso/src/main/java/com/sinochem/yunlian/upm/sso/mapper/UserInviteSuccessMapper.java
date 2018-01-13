package com.sinochem.yunlian.upm.sso.mapper;

import com.sinochem.yunlian.upm.sso.domain.UserInviteSuccess;
import com.sinochem.yunlian.upm.sso.domain.UserInviteSuccessExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserInviteSuccessMapper {
    int countByExample(UserInviteSuccessExample example);

    int deleteByExample(UserInviteSuccessExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserInviteSuccess record);

    int insertSelective(UserInviteSuccess record);

    List<UserInviteSuccess> selectByExampleWithRowbounds(UserInviteSuccessExample example, RowBounds rowBounds);

    List<UserInviteSuccess> selectByExample(UserInviteSuccessExample example);

    UserInviteSuccess selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserInviteSuccess record, @Param("example") UserInviteSuccessExample example);

    int updateByExample(@Param("record") UserInviteSuccess record, @Param("example") UserInviteSuccessExample example);

    int updateByPrimaryKeySelective(UserInviteSuccess record);

    int updateByPrimaryKey(UserInviteSuccess record);
}