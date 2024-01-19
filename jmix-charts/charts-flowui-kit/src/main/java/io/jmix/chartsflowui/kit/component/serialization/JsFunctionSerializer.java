/*
 * Copyright 2024 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.chartsflowui.kit.component.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.jmix.chartsflowui.kit.component.model.shared.JsFunction;

import java.io.IOException;

public class JsFunctionSerializer extends AbstractSerializer<JsFunction> {

    public JsFunctionSerializer() {
        super(JsFunction.class);
    }

    @Override
    public void serializeNonNullValue(JsFunction value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(value.getCode());
    }
}
