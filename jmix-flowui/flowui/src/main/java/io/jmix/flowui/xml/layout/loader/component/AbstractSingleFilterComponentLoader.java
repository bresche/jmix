/*
 * Copyright 2022 Haulmont.
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

package io.jmix.flowui.xml.layout.loader.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import io.jmix.flowui.component.SupportsLabelPosition;
import io.jmix.flowui.component.filer.SingleFilterComponentBase;
import io.jmix.flowui.exception.GuiDevelopmentException;
import io.jmix.flowui.model.DataLoader;
import io.jmix.flowui.xml.layout.ComponentLoader;
import io.jmix.flowui.xml.layout.loader.AbstractComponentLoader;
import org.dom4j.Element;

import java.util.List;

public abstract class AbstractSingleFilterComponentLoader<C extends SingleFilterComponentBase>
        extends AbstractComponentLoader<C> {

    @Override
    public void loadComponent() {
        loadAttributesBeforeValueComponent();

        loadDataLoader(resultComponent, element);
        loadBoolean(element, "autoApply", resultComponent::setAutoApply);
        loadValueComponent(resultComponent, element);

        componentLoader().loadLabel(resultComponent, element);
        loadBoolean(element, "labelVisible", resultComponent::setLabelVisible);
        loadEnum(element, SupportsLabelPosition.LabelPosition.class, "labelPosition",
                resultComponent::setLabelPosition);
        loadString(element, "labelWidth", resultComponent::setLabelWidth);

        componentLoader().loadRequired(resultComponent, element, context);
        componentLoader().loadValueAndElementAttributes(resultComponent, element);
        componentLoader().loadValidationAttributes(resultComponent, element, context);
        componentLoader().loadSizeAttributes(resultComponent, element);
    }

    protected void loadAttributesBeforeValueComponent() {
        componentLoader().loadEnabled(resultComponent, element);
        componentLoader().loadClassNames(resultComponent, element);
        componentLoader().loadThemeNames(resultComponent, element);
        componentLoader().loadHelperText(resultComponent, element);
        componentLoader().loadTabIndex(resultComponent, element);
    }

    protected void loadDataLoader(C resultComponent, Element element) {
        loadString(element, "dataLoader")
                .ifPresent(dataLoaderId -> {
                    DataLoader dataLoader = getComponentContext().getViewData().getLoader(dataLoaderId);
                    resultComponent.setDataLoader(dataLoader);
                });
    }

    protected void loadValueComponent(C resultComponent, Element element) {
        Component valueComponent;

        if (!element.elements().isEmpty()) {
            valueComponent = createValueComponent(element.elements());
        } else {
            valueComponent = generateValueComponent();
        }

        if (!(valueComponent instanceof HasValueAndElement)) {
            throw new GuiDevelopmentException("Value component of filter component must implement " +
                    HasValueAndElement.class.getSimpleName(), context);
        }

        resultComponent.setValueComponent(((HasValueAndElement<?, ?>) valueComponent));
    }

    protected Component createValueComponent(List<Element> elements) {
        if (elements.size() > 1) {
            throw new GuiDevelopmentException("Only one value component can be defined", context);
        }

        Element valueComponentElement = elements.get(0);
        ComponentLoader<?> valueComponentLoader = getLayoutLoader().createComponentLoader(valueComponentElement);
        valueComponentLoader.initComponent();
        valueComponentLoader.loadComponent();

        return valueComponentLoader.getResultComponent();
    }

    protected abstract Component generateValueComponent();
}
