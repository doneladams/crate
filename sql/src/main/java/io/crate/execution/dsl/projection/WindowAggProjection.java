/*
 * Licensed to Crate under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Crate licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial
 * agreement.
 */

package io.crate.execution.dsl.projection;

import io.crate.expression.symbol.Symbol;
import io.crate.expression.symbol.Symbols;
import io.crate.expression.symbol.WindowFunction;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WindowAggProjection extends Projection {

    private final List<Symbol> outputs;
    private final List<WindowFunction> windowFunctions;

    public WindowAggProjection(List<Symbol> outputs, List<WindowFunction> windowFunctions) {
        super();
        this.outputs = outputs;
        this.windowFunctions = windowFunctions;
    }

    public WindowAggProjection(StreamInput in) throws IOException {
        outputs = Symbols.listFromStream(in);
        int size = in.readVInt();
        windowFunctions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            windowFunctions.add((WindowFunction) Symbols.fromStream(in));
        }
    }

    @Override
    public ProjectionType projectionType() {
        return ProjectionType.WINDOW_AGGREGATION;
    }

    @Override
    public <C, R> R accept(ProjectionVisitor<C, R> visitor, C context) {
        return null;
    }

    @Override
    public List<? extends Symbol> outputs() {
        return null;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        Symbols.toStream(outputs, out);
        Symbols.toStream(windowFunctions, out);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
