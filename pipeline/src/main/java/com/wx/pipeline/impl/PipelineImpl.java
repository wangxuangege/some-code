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

package com.wx.pipeline.impl;

import com.wx.pipeline.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 对<code>Pipeline</code>的实现。
 *
 * @author Michael Zhou
 */
public class PipelineImpl implements Pipeline {
    private Valve[] valves;
    private String label;

    public Valve[] getValves() {
        return valves;
    }

    public void setValves(Valve[] valves) {
        this.valves = valves;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PipelineInvocationHandle newInvocation() {
        return new PipelineContextImpl(null);
    }

    public PipelineInvocationHandle newInvocation(PipelineContext parentContext) {
        return new PipelineContextImpl(parentContext);
    }

    /** 实现<code>PipelineContext</code>。 */
    private final class PipelineContextImpl implements PipelineContext, PipelineInvocationHandle {
        private final PipelineContext parentContext;
        private final int             level;
        private int executedIndex  = -1;
        private int executingIndex = -1;
        private boolean             broken;
        private Map<String, Object> attributes;

        public PipelineContextImpl(PipelineContext parentContext) {
            this.parentContext = parentContext;

            if (parentContext == null) {
                this.level = 1;
            } else {
                this.level = parentContext.level() + 1;
            }
        }

        public int level() {
            return level;
        }

        public int index() {
            return executingIndex + 1;
        }

        public int findLabel(String label) throws LabelNotDefinedException {
            boolean isTop = TOP_LABEL.equals(label);

            if (isTop && parentContext == null) {
                return 0;
            } else if (label.equals(getLabel())) {
                return 0;
            } else if (parentContext != null) {
                return parentContext.findLabel(label) + 1;
            } else {
                throw new LabelNotDefinedException("Could not find pipeline or sub-pipeline with label \"" + label
                                                   + "\" in the pipeline invocation stack");
            }
        }

        public void invokeNext() {
            if (broken) {
                return;
            }

            try {
                executingIndex++;

                if (executingIndex <= executedIndex) {
                    throw new IllegalStateException(descCurrentValve() + " has already been invoked: "
                                                    + valves[executingIndex]);
                }

                executedIndex++;

                if (executingIndex < valves.length) {
                    Valve valve = valves[executingIndex];

                    try {
                        System.out.println("Entering " + descCurrentValve() + ":" + valve);

                        valve.invoke(this);
                    } catch (PipelineException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new PipelineException("Failed to invoke " + descCurrentValve() + ": " + valve, e);
                    } finally {
                        System.out.println("...Exited " + descCurrentValve() + ":" + valve);
                    }

                    if (executedIndex < valves.length && executedIndex == executingIndex) {
                        System.out.println(descCurrentPipeline() + " execution was interrupted by "  + descCurrentValve() + ":" + valve);
                    }
                } else {
                    System.out.println(descCurrentPipeline() + " reaches its end.");
                }
            } finally {
                executingIndex--;
            }
        }

        public void breakPipeline(int levels) {

            broken = true;

            if (levels > 0) {
                parentContext.breakPipeline(levels - 1);
            }
        }

        public void breakPipeline(String label) throws LabelNotDefinedException {
            breakPipeline(findLabel(label));
        }

        public boolean isBroken() {
            return broken;
        }

        public boolean isFinished() {
            return !broken && executedIndex >= valves.length;
        }

        public void invoke() throws IllegalStateException {
            executingIndex = executedIndex = -1;
            invokeNext();
        }

        public Object getAttribute(String key) {
            Object value = null;

            if (attributes != null) {
                value = attributes.get(key);
            }

            if (value == null && parentContext != null) {
                value = parentContext.getAttribute(key);
            }

            return value;
        }

        public void setAttribute(String key, Object value) {
            if (attributes == null) {
                attributes = new HashMap<>();
            }

            attributes.put(key, value);
        }

        @Override
        public String toString() {
            return "Executing Pipeline " + descCurrentValve();
        }

        private String descCurrentPipeline() {
            return "Pipeline[level " + level() + "]";
        }

        private String descCurrentValve() {
            return "Valve[#" + index() + "/" + valves.length + ", level " + level() + "]";
        }
    }
}
