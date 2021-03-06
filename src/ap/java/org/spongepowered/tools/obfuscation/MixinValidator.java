/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.tools.obfuscation;

import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;


/**
 * Base class for mixin validators
 */
public abstract class MixinValidator implements IMixinValidator {
    
    /**
     * Processing environment for this validator, used for raising compiler
     * errors and warnings 
     */
    protected final ProcessingEnvironment processingEnv;
    
    /**
     * Pass to run this validator in 
     */
    protected final ValidationPass pass;

    /**
     * ctor
     * 
     * @param processingEnv Processing environment
     */
    public MixinValidator(ProcessingEnvironment processingEnv, ValidationPass pass) {
        this.processingEnv = processingEnv;
        this.pass = pass;
    }
    
    /* (non-Javadoc)
     * @see org.spongepowered.tools.obfuscation.IMixinValidator#validate(
     *      org.spongepowered.tools.obfuscation.IMixinValidator.ValidationPass,
     *      javax.lang.model.element.TypeElement,
     *      javax.lang.model.element.AnnotationMirror, java.util.Collection)
     */
    @Override
    public final boolean validate(ValidationPass pass, TypeElement mixin, AnnotationMirror annotation, Collection<TypeHandle> targets) {
        if (pass != this.pass) {
            return true;
        }
        
        return this.validate(mixin, annotation, targets);
    }

    protected abstract boolean validate(TypeElement mixin, AnnotationMirror annotation, Collection<TypeHandle> targets);
    
    /**
     * Output a compiler note
     * 
     * @param note Message
     * @param element Element to attach the note to
     */
    protected final void note(String note, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.NOTE, note, element);
    }
    
    /**
     * Output a compiler note
     * 
     * @param warning Message
     * @param element Element to attach the warning to
     */
    protected final void warning(String warning, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.WARNING, warning, element);
    }
    
    /**
     * Output a compiler note
     * 
     * @param error Message
     * @param element Element to attach the error to
     */
    protected final void error(String error, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.ERROR, error, element);
    }

    protected final Collection<TypeMirror> getMixinsTargeting(TypeMirror targetType) {
        return AnnotatedMixins.getMixinsForEnvironment(this.processingEnv).getMixinsTargeting(targetType);
    }
}
