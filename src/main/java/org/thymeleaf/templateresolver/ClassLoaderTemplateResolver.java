/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.templateresolver;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresource.ClassLoaderTemplateResource;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.ClassLoaderUtils;
import org.thymeleaf.util.Validate;

/**
 * <p>
 *   Implementation of {@link ITemplateResolver} that extends {@link AbstractConfigurableTemplateResolver}
 *   and creates {@link ClassLoaderTemplateResource} instances for template resources.
 * </p>
 * <p>
 *   Note a class with this name existed since 1.0, but it was completely rewritten in Thymeleaf 3.0.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 3.0.0
 *
 */
public class ClassLoaderTemplateResolver extends AbstractConfigurableTemplateResolver {


    private final ClassLoader classLoader;
    


    public ClassLoaderTemplateResolver() {
        this(ClassLoaderUtils.getClassLoader(ClassLoaderTemplateResolver.class));
    }


    public ClassLoaderTemplateResolver(final ClassLoader classLoader) {
        super();
        Validate.notNull(classLoader, "Class Loader cannot be null");
        this.classLoader = classLoader;
    }


    @Override
    protected ITemplateResource computeTemplateResource(
            final IEngineConfiguration configuration, final String template, final String resourceName, final String characterEncoding) {
        return new ClassLoaderTemplateResource(this.classLoader, resourceName, characterEncoding);
    }

}