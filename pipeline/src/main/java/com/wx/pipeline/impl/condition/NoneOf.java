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

package com.wx.pipeline.impl.condition;


import com.wx.pipeline.Condition;
import com.wx.pipeline.PipelineStates;
import com.wx.pipeline.support.AbstractCompositeCondition;

/**
 * 当下属的所有conditions均返回<code>false</code>时，才返回<code>true</code>。
 *
 * @author Michael Zhou
 */
public class NoneOf extends AbstractCompositeCondition {
    public boolean isSatisfied(PipelineStates pipelineStates) {
        for (Condition condition : getConditions()) {
            if (condition.isSatisfied(pipelineStates)) {
                return false;
            }
        }

        return true;
    }
}
