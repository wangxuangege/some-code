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


import com.wx.pipeline.Condition;
import com.wx.pipeline.PipelineContext;
import com.wx.pipeline.PipelineInvocationHandle;
import com.wx.pipeline.impl.Callback;

/**
 * 当条件满足时，执行循环体。
 *
 * @author Michael Zhou
 */
public class WhileLoopValve extends LoopValve {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {

        PipelineInvocationHandle handle = initLoop(pipelineContext);

        Callback nextCallback = new Callback() {
            @Override
            public void callback(PipelineContext pipelineContext) {
                callback.callback(pipelineContext);
            }
        };

        while (condition.isSatisfied(handle)) { // 注意：condition的上下文为handle而非当前pipelineContext
            final Callback runCallback = nextCallback;
            nextCallback = new Callback() {
                @Override
                public void callback(PipelineContext pipelineContext1) {
                    runCallback.callback(pipelineContext1);
                }
            };
            invokeBody(handle, runCallback);

            if (handle.isBroken()) {
                break;
            }
        }

        final Callback runCallback = nextCallback;
        pipelineContext.invokeNext(new Callback() {
            @Override
            public void callback(PipelineContext pipelineContext) {
                runCallback.callback(pipelineContext);
            }
        });
    }
}
