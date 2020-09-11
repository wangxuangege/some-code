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

package com.wx.pipeline.support;

import com.wx.pipeline.Condition;

/**
 * 组合式的condition基类。
 *
 * @author Michael Zhou
 */
public abstract class AbstractCompositeCondition extends AbstractCondition {
    private static final Condition[] EMPTY_CONDITIONS = new Condition[0];
    private Condition[] conditions;

    public Condition[] getConditions() {
        return conditions == null ? EMPTY_CONDITIONS : conditions;
    }

    public void setConditions(Condition[] conditions) {
        this.conditions = conditions;
    }

    protected String getDesc() {
        return getClass().getSimpleName();
    }
}
