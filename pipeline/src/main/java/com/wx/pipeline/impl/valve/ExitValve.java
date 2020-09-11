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
import com.wx.pipeline.impl.Callback;
import com.wx.pipeline.support.AbstractValve;

/**
 * 用来结束顶级pipeline的执行，相当于<code>&lt;break toLabel="#TOP" /&gt;</code>。
 *
 * @author Michael Zhou
 */
public class ExitValve extends AbstractValve {
    public void invoke(PipelineContext pipelineContext, Callback callback) throws Exception {
        pipelineContext.breakPipeline(Pipeline.TOP_LABEL);
        pipelineContext.invokeNext(callback);
    }

    @Override
    public String toString() {
        return "ExitValve";
    }
}
