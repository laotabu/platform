package com.laotabu.framework.web.service;

import com.laotabu.common.utils.SecurityUtils;
import com.laotabu.system.service.ISysPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.laotabu.common.core.domain.entity.SysUser;
import com.laotabu.common.core.domain.model.LoginUser;
import com.laotabu.common.enums.UserStatus;
import com.laotabu.common.exception.ServiceException;
import com.laotabu.common.utils.MessageUtils;
import com.laotabu.common.utils.StringUtils;
import com.laotabu.system.service.ISysUserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户验证处理
 *
 * @author laotabu
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;
    
//    @Autowired
//    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysPostService sysPostService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

//        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        /**
         * activiti 需要对角色以及分组信息做定制处理【加上前缀】，否则会报没有权限问题
         * 解决方案
         * （1）方案一： 手动拼接，如下
         * （2）方案二： 直接数据库设置好
         */
        // 根据用户id获取用户角色代码的集合【比如项目经理re，普通用户user等】
        Set<String> postCode = sysPostService.selectPostCodeByUserId(user.getUserId());
        postCode = postCode.parallelStream().map( s ->  "GROUP_" + s).collect(Collectors.toSet());
        // 防止有些用户没有分配角色，加了一个默认的，可以根据要求取消掉
        postCode.add("ROLE_ACTIVITI_USER");
        List<SimpleGrantedAuthority> collect = postCode.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
//        return new LoginUser(user, permissionService.getMenuPermission(user), collect);
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user), collect);
//        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }

    /**
     * 登录成功后会自动更新数据库的密码【如果要更新加密算法】
     * @param user
     * @param newPassword
     * @return
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
//        LoginUser loginUser = SecurityUtils.getLoginUser();
        LoginUser loginUser = (LoginUser) user;
        if (userService.resetUserPwd(user.getUsername(), newPassword) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
        }
        return loginUser;
    }

}
