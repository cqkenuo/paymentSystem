package com.nnxy.payment.consumer81.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.nnxy.common.utils.R;
import com.nnxy.payment.consumer81.entitys.AccountEntity;
import com.nnxy.payment.consumer81.entitys.AllEntity;
import com.nnxy.payment.consumer81.entitys.FlowEntity;
import com.nnxy.payment.consumer81.entitys.OrderEntity;
import com.nnxy.payment.consumer81.feign.BankServiceFeign;
import com.nnxy.payment.consumer81.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author litianfu
 * @version 1.0
 * @email 1035869369@qq.com
 * @date 2020-04-11 15:02
 */
@RestController("/consumer")
public class ConsumerController {

    @Autowired
    private BankServiceFeign bankServiceFeign;

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/test")
    @SentinelResource(value = "test",blockHandler = "consumerBlockHandlerMethod",fallback = "consumerFallBackMethod")
    public String test() {
        System.out.println("我是consumer，我准备调用bank9001服务");
        return bankServiceFeign.test();
    }

    @PostMapping("/orderList")
    public R orderList(@RequestBody AccountEntity accountEntity){
        return consumerService.orderList(accountEntity);
    }

    /**
     * 登录
     * @param accountEntity
     * @return
     */
    @PostMapping("/login")
    public R login(AccountEntity accountEntity) {
        return bankServiceFeign.login(accountEntity);
    }

    /**
     * 注册
     *
     * @param accountEntity
     * @return
     */
    @PostMapping("/register")
    public R register(AccountEntity accountEntity) {
        return bankServiceFeign.register(accountEntity);
    }

    /**
     * 充值
     *
     * @param allEntity
     * @return
     */
    @PostMapping("/recharge")
    public R recharge(@RequestBody AllEntity allEntity) {
        return consumerService.recharge(allEntity.getAccountEntity(),allEntity.getFlowEntity(),allEntity.getOrderEntity());
    }

    /**
     * 查询余额
     *
     * @param accountEntity
     * @return
     */
    @PostMapping("/checkingBalance")
    public R checkingBalance(@RequestBody AccountEntity accountEntity) {
        return bankServiceFeign.checkingBalance(accountEntity);
    }

    /**
     * 支付
     */
    @PostMapping("/payment")
    public R payment(@RequestBody AllEntity allEntity) {
        System.out.println("支付中。。。。。。。。。。。。。");
        return consumerService.payment(allEntity.getAccountEntity(), allEntity.getFlowEntity(), allEntity.getOrderEntity());

    }

    /**
     * @return
     */
    @PostMapping("/transfer")
    public R transfer(@RequestBody AllEntity allEntity) {
        System.out.println("转账中。。。。。。。。。。。。。。。。");
        return consumerService.transfer(allEntity.getAccountEntity(), allEntity.getTarget(),
                allEntity.getFlowEntity(), allEntity.getOrderEntity());
    }

    /**
     * 熔断处理
     * @return
     */
    public R consumerBlockHandlerMethod(){
        return R.error(444,"目前服务器太拥挤了，请稍后再试");
    }

    public R consumerFallBackMethod(){
        return R.error(444,"服务器冒烟了，请稍后再试");
    }
}
