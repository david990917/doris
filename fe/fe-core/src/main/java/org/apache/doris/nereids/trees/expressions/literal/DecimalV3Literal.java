// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.nereids.trees.expressions.literal;

import org.apache.doris.analysis.LiteralExpr;
import org.apache.doris.nereids.trees.expressions.visitor.ExpressionVisitor;
import org.apache.doris.nereids.types.DecimalV3Type;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Literal for DecimalV3 Type
 */
public class DecimalV3Literal extends Literal {

    private final BigDecimal value;

    public DecimalV3Literal(BigDecimal value) {
        super(DecimalV3Type.createDecimalV3Type(value));
        this.value = Objects.requireNonNull(value);
    }

    public DecimalV3Literal(DecimalV3Type dataType, BigDecimal value) {
        super(DecimalV3Type.createDecimalV3Type(dataType.getPrecision(), dataType.getScale()));
        Objects.requireNonNull(value, "value not be null");
        BigDecimal adjustedValue = value.scale() < 0 ? value : value.setScale(dataType.getScale(), RoundingMode.DOWN);
        this.value = Objects.requireNonNull(adjustedValue);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public <R, C> R accept(ExpressionVisitor<R, C> visitor, C context) {
        return visitor.visitDecimalV3Literal(this, context);
    }

    @Override
    public LiteralExpr toLegacyLiteral() {
        return new org.apache.doris.analysis.DecimalLiteral(value, getDataType().toCatalogDataType());
    }

    @Override
    public double getDouble() {
        return value.doubleValue();
    }
}
