package com.wx.pipeline;

import com.google.common.util.concurrent.*;
import com.wx.pipeline.impl.Callback;
import com.wx.pipeline.impl.PipelineImpl;
import com.wx.pipeline.impl.valve.ChooseValve;
import org.junit.After;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

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
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value1-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value1-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value1-end");
                    }
                },
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value2-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value2-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value2-end");
                    }
                },
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value3-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value3-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value3-end");
                    }
                }
        };
        PipelineImpl pipeline = new PipelineImpl();
        pipeline.setValves(values);
        pipeline.setLabel("test1");
        pipeline.newInvocation().invoke(new Callback() {
            @Override
            public void callback(PipelineContext pipelineContext) {
                System.out.println("callback");
            }
        });
    }

    @Test
    public void test2() {
        Condition condition1 = pipelineStates -> true;
        PipelineImpl pipeline1 = new PipelineImpl();
        pipeline1.setValves(new Valve[] {
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value11-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value11-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value11-end");
                    }
                },
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value12-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value12-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value12-end");
                    }
                }
        });
        pipeline1.setLabel("test2_value1");

        ChooseValve value1 = new ChooseValve();
        value1.setWhenConditions(new Condition[] {condition1});
        value1.setWhenBlocks(new Pipeline[] {pipeline1});

        Valve[] values = new Valve[]{
                value1,
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value2-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value2-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value2-end");
                    }
                },
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value3-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value3-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value3-end");
                    }
                }
        };
        PipelineImpl pipeline = new PipelineImpl();
        pipeline.setValves(values);
        pipeline.setLabel("test2");
        pipeline.newInvocation().invoke(new Callback() {
            @Override
            public void callback(PipelineContext pipelineContext) {
                System.out.println("callback");
            }
        });
    }

    @Test
    public void test3() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(1);

        Valve[] values = new Valve[]{
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value0-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value0-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value0-end");
                    }
                },
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
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
                                pipelineContext.invokeNext(new Callback() {
                                    @Override
                                    public void callback(PipelineContext pipelineContext) {
                                        System.out.println("value1-success-callback");
                                        callback.callback(pipelineContext);
                                    }
                                });
                                System.out.println("value1-end");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                pipelineContext.invokeNext(new Callback() {
                                    @Override
                                    public void callback(PipelineContext pipelineContext) {
                                        System.out.println("value1-fail-callback");
                                        callback.callback(pipelineContext);
                                    }
                                });
                            }
                        });
                    }
                },
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value2-begin");
                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value2-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value2-end");
                    }
                },
                new Valve() {
                    @Override
                    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
                        System.out.println("value3-begin");

                        pipelineContext.invokeNext(new Callback() {
                            @Override
                            public void callback(PipelineContext pipelineContext) {
                                System.out.println("value3-callback");
                                callback.callback(pipelineContext);
                            }
                        });
                        System.out.println("value3-end");

                        cdl.countDown();
                    }
                }
        };
        PipelineImpl pipeline = new PipelineImpl();
        pipeline.setValves(values);
        pipeline.setLabel("test3");
        pipeline.newInvocation().invoke(new Callback() {
            @Override
            public void callback(PipelineContext pipelineContext) {
                System.out.println("callback");
            }
        });

        cdl.await();
    }
}