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
import com.wx.pipeline.support.AbstractValve;

/**
 * 支持try-catch-finally结构。
 *
 * @author Michael Zhou
 */
public class TryCatchFinallyValve extends AbstractValve {
    private final static String DEFAULT_EXCEPTION_NAME = "exception";
    private Pipeline tryPipeline;
    private Pipeline catchPipeline;
    private Pipeline finallyPipeline;
    private String exceptionName;

    public Pipeline getTry() {
        return tryPipeline;
    }

    public void setTry(Pipeline tryPipeline) {
        this.tryPipeline = tryPipeline;
    }

    public Pipeline getCatch() {
        return catchPipeline;
    }

    public void setCatch(Pipeline catchPipeline) {
        this.catchPipeline = catchPipeline;
    }

    public String getExceptionName() {
        return exceptionName == null ? DEFAULT_EXCEPTION_NAME : exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public Pipeline getFinally() {
        return finallyPipeline;
    }

    public void setFinally(Pipeline finallyPipeline) {
        this.finallyPipeline = finallyPipeline;
    }

    public void invoke(PipelineContext pipelineContext) throws Exception {
        try {
            if (tryPipeline != null) {
                tryPipeline.newInvocation(pipelineContext).invoke();
            }
        } catch (Exception e) {
            if (catchPipeline != null) {
                PipelineInvocationHandle handle = catchPipeline.newInvocation(pipelineContext);
                handle.setAttribute(getExceptionName(), e);
                handle.invoke();
            } else {
                throw e;
            }
        } finally {
            if (finallyPipeline != null) {
                finallyPipeline.newInvocation(pipelineContext).invoke();
            }
        }

        pipelineContext.invokeNext();
    }
}
