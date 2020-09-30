/*
 * Copyright (c) 2002-2012 Alibaba Group Holding Limited.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wx.pipeline.impl.valve;

import com.wx.pipeline.Pipeline;
import com.wx.pipeline.PipelineContext;
import com.wx.pipeline.PipelineInvocationHandle;
import com.wx.pipeline.TooManyLoopsException;
import com.wx.pipeline.impl.Callback;
import com.wx.pipeline.support.AbstractValve;


/**
 * 用来反复执行同一个子pipeline。
 *
 * @author Michael Zhou
 */
public class LoopValve extends AbstractValve {
    private final static int    DEFAULT_MAX_LOOP          = 10;
    private final static String DEFAULT_LOOP_COUNTER_NAME = "loopCount";
    private Pipeline loopBody;
    private Integer maxLoopCount;
    private String loopCounterName;

    public Pipeline getLoopBody() {
        return loopBody;
    }

    public void setLoopBody(Pipeline loopBody) {
        this.loopBody = loopBody;
    }

    public int getMaxLoopCount() {
        return maxLoopCount == null ? DEFAULT_MAX_LOOP : maxLoopCount;
    }

    public void setMaxLoopCount(int maxLoopCount) {
        this.maxLoopCount = maxLoopCount <= 0 ? 0 : maxLoopCount;
    }

    public String getLoopCounterName() {
        return loopCounterName == null ? DEFAULT_LOOP_COUNTER_NAME : loopCounterName;
    }

    public void setLoopCounterName(String loopCounterName) {
        this.loopCounterName = loopCounterName;
    }

    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {

        PipelineInvocationHandle handle = initLoop(pipelineContext);

        Callback newCallback = new Callback() {
            @Override
            public void callback(PipelineContext pipelineContext) {

            }
        };
        Callback loopCallback = new Callback() {
            @Override
            public void callback(PipelineContext pipelineContext) {
                callback.callback(pipelineContext);
            }
        };
        do {
            invokeBody(handle, loopCallback);
        } while (!handle.isBroken());

        pipelineContext.invokeNext(loopCallback);
    }

    protected PipelineInvocationHandle initLoop(PipelineContext pipelineContext) {
        PipelineInvocationHandle handle = getLoopBody().newInvocation(pipelineContext);
        handle.setAttribute(getLoopCounterName(), 0);
        return handle;
    }

    protected void invokeBody(PipelineInvocationHandle handle, Callback callback) {
        String loopCounterName = getLoopCounterName();
        int loopCount = (Integer) handle.getAttribute(loopCounterName);
        int maxLoopCount = getMaxLoopCount();

        // maxLoopCount<=0，意味着没有循环次数的限制。
        if (maxLoopCount > 0 && loopCount >= maxLoopCount) {
            throw new TooManyLoopsException("Too many loops: exceeds the maximum count: " + maxLoopCount);
        }

        handle.invoke(callback);
        handle.setAttribute(loopCounterName, ++loopCount);
    }
}
