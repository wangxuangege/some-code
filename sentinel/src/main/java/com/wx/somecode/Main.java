package com.wx.somecode;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xinquan.huangxq
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {
        System.out.println("start");

        final CountDownLatch cdl = new CountDownLatch(1);

       // 设置限流规则
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setCount(2);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);

        final AtomicLong blockCount = new AtomicLong(0);
        final AtomicLong accessCount = new AtomicLong(0);

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.schedule(new Runnable() {
            public void run() {
                Entry entry = null;
                try {
                    entry = SphU.entry("HelloWorld");
                    accessCount.getAndIncrement();
                } catch (BlockException e) {
                    blockCount.getAndIncrement();
                } finally {
                    if (entry != null) {
                        entry.exit();
                    }
                }
                if (accessCount.get() + blockCount.get() >= 100000) {
                    cdl.countDown();
                }
            }
        }, 1, TimeUnit.SECONDS);

        cdl.await();

        System.out.println("blockCount=" + blockCount.get());
        System.out.println("accessCount=" + accessCount.get());

        System.out.println("finish");
    }
}
