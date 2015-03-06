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
package org.thymeleaf.exceptions;



/**
 * <p>
 *   General exception for errors raised during the process of a template.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * @author Guven Demir
 * 
 * @since 1.0
 *
 */
public class TemplateProcessingException extends TemplateEngineException {

    private static final long serialVersionUID = 5985749439214775193L;

    private String templateName;
    private Integer line;
    private Integer col;
    
    
    
    public TemplateProcessingException(final String message) {
        this(message, (String)null);
    }
    
    public TemplateProcessingException(final String message, final Throwable cause) {
        this(message, null, cause);
    }
    
    public TemplateProcessingException(
            final String message, final String templateName) {
        super(message);
        this.templateName = templateName;
        this.line = null;
        this.col = null;
    }
    
    public TemplateProcessingException(
            final String message, final String templateName, final Throwable cause) {
        super(message, cause);
        this.templateName = templateName;
        this.line = null;
        this.col = null;
    }

    /**
     *
     * @since 3.0.0
     */
    public TemplateProcessingException(
            final String message, final String templateName, final int line, final int col) {
        super(message);
        this.templateName = templateName;
        this.line = (line < 0? null : Integer.valueOf(line));
        this.col = (col < 0? null : Integer.valueOf(col));
    }

    /**
     *
     * @since 3.0.0
     */
    public TemplateProcessingException(
            final String message, final String templateName, final int line, final int col, final Throwable cause) {
        super(message, cause);
        this.templateName = templateName;
        this.line = (line < 0? null : Integer.valueOf(line));
        this.col = (col < 0? null : Integer.valueOf(col));
    }

    
    
    
    public String getTemplateName() {
        return this.templateName;
    }
    
    public boolean hasTemplateName() {
        return this.templateName != null;
    }

    public Integer getLine() {
        return this.line;
    }

    public Integer getCol() {
        return this.col;
    }

    public boolean hasLine() {
        return this.line != null;
    }

    public boolean hasCol() {
        return this.col != null;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public void setLine(final int line) {
        this.line = (line < 0? null : Integer.valueOf(line));
    }

    public void setCol(final int col) {
        this.col = (col < 0? null : Integer.valueOf(col));
    }

    
    

    @Override
    public String getMessage() {
        
        final StringBuilder sb = new StringBuilder();

        sb.append(super.getMessage());
        
        if (this.templateName != null) {
            sb.append(' ');
            sb.append('(');
            sb.append('"');
            sb.append(this.templateName);
            sb.append('"');
            if (this.line != null || this.col != null) {
                sb.append(": ");
                if (this.line != null) {
                    sb.append("line ");
                    sb.append(this.line);
                }
                if (this.col != null) {
                    sb.append(", col ");
                    sb.append(this.col);
                }
            }
            sb.append(')');
        }

        return sb.toString();
        
    }
    
    
}