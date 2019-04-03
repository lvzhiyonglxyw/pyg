package com.pyg.pyg_shop_web;

import com.pyg.pyg_sellergoods_interface.SellerService;
import com.pyg_pojo.pojo.TbSeller;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.ContextLoaderListener;

import java.util.ArrayList;
import java.util.List;

public class SellerUserServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TbSeller tbSeller = sellerService.findOne(username);
        if (tbSeller == null&&!tbSeller.getStatus().equals("1")) {
            //认证失败
            return null;
        }

        //认证成功
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //添加权限
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        UserDetails ud = new User(tbSeller.getSellerId(), tbSeller.getPassword(), authorities);
        return ud;
    }
}
