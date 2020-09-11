package com.wx.pipeline;

import com.google.common.util.concurrent.*;
import com.wx.pipeline.impl.PipelineImpl;
import com.wx.pipeline.impl.valve.ChooseValve;
import org.junit.After;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author xinquan.huangxq
 */
public class PipelineTest {

    final static ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    @After
    public void shutdown() {
        executor.shutdown();
    }

    @Test
    public void test1() {
        Valve[] values = new Valve[]{
                pipelineContext -> {
                    System.out.println("value1-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value1-end");
                },
                pipelineContext -> {
                    System.out.println("value2-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value2-end");
                },
                pipelineContext -> {
                    System.out.println("value3-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value3-end");
                }
        };
        PipelineImpl pipeline = new PipelineImpl();
        pipeline.setValves(values);
        pipeline.setLabel("test1");
        pipeline.newInvocation().invoke();
    }

    @Test
    public void test2() {
        Condition condition1 = pipelineStates -> new Random().nextBoolean();
        PipelineImpl pipeline1 = new PipelineImpl();
        pipeline1.setValves(new Valve[] {
                pipelineContext -> {
                    System.out.println("value11-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value11-end");
                },
                pipelineContext -> {
                    System.out.println("value12-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value12-end");
                }
        });
        pipeline1.setLabel("test2_value1");

        ChooseValve value1 = new ChooseValve();
        value1.setWhenConditions(new Condition[] {condition1});
        value1.setWhenBlocks(new Pipeline[] {pipeline1});

        Valve[] values = new Valve[]{
                value1,
                pipelineContext -> {
                    System.out.println("value2-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value2-end");
                },
                pipelineContext -> {
                    System.out.println("value3-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value3-end");
                }
        };
        PipelineImpl pipeline = new PipelineImpl();
        pipeline.setValves(values);
        pipeline.setLabel("test2");
        pipeline.newInvocation().invoke();
    }

    @Test
    public void test3() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(1);

        Valve[] values = new Valve[]{
                pipelineContext -> {
                    System.out.println("value1-begin");

                    ListenableFuture<Boolean> booleanTask = executor.submit(() -> {
                        System.out.println("value1-future-begin");
                        try {
                            Thread.sleep(3000L);
                        } catch (InterruptedException ignored) {
                        }
                        System.out.println("value1-future-end");

                        return true;
                    });
                    Futures.addCallback(booleanTask, new FutureCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            System.out.println("value1-result:" + result);
                            pipelineContext.invokeNext();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            pipelineContext.invokeNext();
                        }
                    });
                    System.out.println("value1-end");
                },
                pipelineContext -> {
                    System.out.println("value2-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value2-end");
                },
                pipelineContext -> {
                    System.out.println("value3-begin");
                    pipelineContext.invokeNext();
                    System.out.println("value3-end");

                    cdl.countDown();
                }
        };
        PipelineImpl pipeline = new PipelineImpl();
        pipeline.setValves(values);
        pipeline.setLabel("test3");
        pipeline.newInvocation().invoke();

        cdl.await();
    }
}