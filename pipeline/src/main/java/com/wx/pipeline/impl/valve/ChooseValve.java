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
import com.wx.pipeline.Pipeline;
import com.wx.pipeline.PipelineContext;
import com.wx.pipeline.support.AbstractValve;

/**
 * 代表一个多重条件选择。
 *
 * @author Michael Zhou
 */
public class ChooseValve extends AbstractValve {
    private Condition[] whenConditions;
    private Pipeline[]  whenBlocks;
    private Pipeline otherwiseBlock;

    public Condition[] getWhenConditions() {
        return whenConditions;
    }

    public void setWhenConditions(Condition[] whenConditions) {
        this.whenConditions = whenConditions;
    }

    public Pipeline[] getWhenBlocks() {
        return whenBlocks;
    }

    public void setWhenBlocks(Pipeline[] whenBlocks) {
        this.whenBlocks = whenBlocks;
    }

    public Pipeline getOtherwiseBlock() {
        return otherwiseBlock;
    }

    public void setOtherwiseBlock(Pipeline otherwiseBlock) {
        this.otherwiseBlock = otherwiseBlock;
    }


    public void invoke(PipelineContext pipelineContext) throws Exception {
        boolean satisfied = false;

        for (int i = 0; i < whenConditions.length; i++) {
            if (whenConditions[i].isSatisfied(pipelineContext)) {
                satisfied = true;
                whenBlocks[i].newInvocation(pipelineContext).invoke();
                break;
            }
        }

        if (!satisfied && otherwiseBlock != null) {
            otherwiseBlock.newInvocation(pipelineContext).invoke();
        }

        pipelineContext.invokeNext();
    }
}
