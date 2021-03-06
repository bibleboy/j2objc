/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.devtools.j2objc.ast;

import com.google.devtools.j2objc.jdt.BindingConverter;
import java.util.List;
import javax.lang.model.element.VariableElement;
import org.eclipse.jdt.core.dom.IVariableBinding;

/**
 * Node type for a declaration of a single variable. Used in parameter lists
 * and catch clauses.
 */
public class SingleVariableDeclaration extends VariableDeclaration {

  private boolean isVarargs = false;
  private final ChildList<Annotation> annotations = ChildList.create(Annotation.class, this);
  private final ChildLink<Type> type = ChildLink.create(Type.class, this);

  public SingleVariableDeclaration() {}

  public SingleVariableDeclaration(SingleVariableDeclaration other) {
    super(other);
    isVarargs = other.isVarargs();
    annotations.copyFrom(other.getAnnotations());
    type.copyFrom(other.getType());
  }

  // TODO(tball): remove when javac migration is complete.
  public SingleVariableDeclaration(IVariableBinding variableBinding) {
    this((VariableElement) BindingConverter.getElement(variableBinding));
  }

  public SingleVariableDeclaration(VariableElement variableElement) {
    super(variableElement, null);
    type.set(Type.newType(variableElement.asType()));
  }

  @Override
  public Kind getKind() {
    return Kind.SINGLE_VARIABLE_DECLARATION;
  }

  public boolean isVarargs() {
    return isVarargs;
  }

  public SingleVariableDeclaration setIsVarargs(boolean value) {
    isVarargs = value;
    return this;
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public SingleVariableDeclaration addAnnotation(Annotation ann) {
    annotations.add(ann);
    return this;
  }

  public Type getType() {
    return type.get();
  }

  public SingleVariableDeclaration setType(Type newType) {
    type.set(newType);
    return this;
  }

  @Override
  protected void acceptInner(TreeVisitor visitor) {
    if (visitor.visit(this)) {
      annotations.accept(visitor);
      type.accept(visitor);
      name.accept(visitor);
      initializer.accept(visitor);
    }
    visitor.endVisit(this);
  }

  @Override
  public SingleVariableDeclaration copy() {
    return new SingleVariableDeclaration(this);
  }
}
