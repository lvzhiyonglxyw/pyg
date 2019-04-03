package com.pinyougou.cart.controlle;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.utils.CookieUtil;
import com.pyg.pyg_user_service.service.AddressService;
import com.pyg_pojo.entity.Cart;
import com.pyg_pojo.entity.CartVo;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/cartController")
public class CartController {
    //localhost:9107/cartController/addItemToCartList.do?itemId=1369333&num=2
    //localhost:9107/cartController/addItemToCartList.do?itemId=536563&num=2
    //localhost:9107/cartController/addItemToCartList.do?itemId=1109357&num=2
    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    //显示用户信息
    @RequestMapping("/showName")
    public Map showName() {
        Map<String, String> map = new HashMap<>();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("name", name);
        return map;
    }

    /**
     * 从redis中获取数据
     *
     * @return
     */
    //使用LocalStorage
    /*@RequestMapping("/findRedisCart")
    public List<Cart> findCartList(@RequestBody CartVo vo) {
        //未登录购物车查询
        List<Cart> redisCart1 = vo.getCartList();
        //获取登录用户名
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        //判断当前是否登录
        if (!"anonymousUser".equals(loginName)) {
            List<Cart> redisCart2 = cartService.findRedisCart(loginName);
            //判断未登录的购物车集合中的购物车个数是否大于0
            if (redisCart1.size() > 0) {
                //合并购物车信息
                List<Cart> cartList = cartService.mergeCartList(redisCart1, redisCart2);
                //将未登录的购物移除
                redisCart1 = cartList;
                //将合并后的购物车添加到redis中
                cartService.addRedisCart(loginName, cartList);
                System.out.println("合并了");
            } else {
                redisCart1 = redisCart2;
                System.out.println("查询了");
            }
        }
        return redisCart1;
    }*/
    //使用redis
    @RequestMapping("/findCartListFromRedis")
    public List<Cart> findRedisCart() {
        String uuid = getUuid();
        //未登录购物车查询
        List<Cart> redisCart1 = cartService.findRedisCart(uuid);
        //获取登录用户名
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        //判断当前是否登录
        if (!"anonymousUser".equals(loginName)) {
            List<Cart> redisCart2 = cartService.findRedisCart(loginName);
            //判断未登录的购物车集合中的购物车个数是否大于0
            if (redisCart1.size() > 0) {
                //合并购物车信息
                List<Cart> cartList = cartService.mergeCartList(redisCart1, redisCart2);
                //将合并后的购物车添加到redis中
                cartService.addRedisCart(loginName, cartList);
                //将未登录的购物从redis中移除
                cartService.removeRedisCart(uuid);
                System.out.println("合并了");
            } else {
                redisCart1 = redisCart2;
                System.out.println("查询了");
            }
        }
        return redisCart1;
    }

    /**
     * 生成一个唯一的id保存到cookie中
     *
     * @return
     */
    public String getUuid() {
        //从cookie中获取值
        String uuid = CookieUtil.getCookieValue(request, "uuid", "UTF-8");
        //判断从cookie中取出的值是否为空
        if (uuid == null) {
            //为空把生成的uuid存入到cookie中
            uuid = UUID.randomUUID().toString();
            CookieUtil.setCookie(request, response, "uuid", uuid, 48 * 60 * 60, "UTF-8");
        }
        return uuid;
    }

    /**
     * 向购物车中添加数据并保存到redis中
     *
     * @param vo
     * @return
     */
    //localhost:9107/cartController/addItemCartList.do?itemId=1369333&num=2
    //http://localhost:9107/cart.html#?itemId=1369333&num=1
    //http://localhost:9107/cart.html#?itemId=536563&num=1
    //http://localhost:9107/cart.html#?itemId=1109357&num=1



    //使用LocalStorage
   /* @CrossOrigin(origins = "*", allowCredentials = "true")
    @RequestMapping("/addItemCartList")
    public Result addItemToCartList(@RequestBody(required = false) CartVo vo) {
        try {
            List<Cart> cartList =
                    cartService.addItemCartList(vo.getCartList(), vo.getNum(), vo.getItemId());
            return new Result(true, JSON.toJSONString(cartList));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }*/
    @CrossOrigin(origins = "*", allowCredentials = "true")
    @RequestMapping("/addItemToCartList")
    public Result addItemToCartList(Long itemId, Integer num) {
        try {
            //获取唯一的id
            String uuid = getUuid();
            //获取登录用户名
            String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
            //判断当前是否登录
            if (!"anonymousUser".equals(loginName)) {
                //已登录使用当前登录用户名做键保存购物车到redis
                uuid = loginName;
            }

            //根据uuid从redis中查询
            List<Cart> cartList = cartService.findRedisCart(uuid);
            //把商品添加到新的购物车
            List<Cart> addItemCartList = cartService.addItemCartList(cartList, num, itemId);
            //向redis中添加购物车
            cartService.addRedisCart(uuid, addItemCartList);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }
}

