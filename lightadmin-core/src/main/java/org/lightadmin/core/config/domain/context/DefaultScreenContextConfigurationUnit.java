/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.config.domain.context;

import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;

public class DefaultScreenContextConfigurationUnit extends DomainTypeConfigurationUnit implements ScreenContextConfigurationUnit {

    private final String screenName;
    private final boolean i18n;

    DefaultScreenContextConfigurationUnit(Class<?> domainType, final String screenName) {
        this(domainType, screenName, false);
    }

    DefaultScreenContextConfigurationUnit(Class<?> domainType, final String screenName, final boolean i18n) {
        super(domainType);

        this.screenName = screenName;
        this.i18n = i18n;
    }

    @Override
    public String getScreenName() {
        return screenName;
    }

    @Override
    public boolean i18n() {
        return i18n;
    }

    @Override
    public DomainConfigurationUnitType getDomainConfigurationUnitType() {
        return DomainConfigurationUnitType.SCREEN_CONTEXT;
    }
}