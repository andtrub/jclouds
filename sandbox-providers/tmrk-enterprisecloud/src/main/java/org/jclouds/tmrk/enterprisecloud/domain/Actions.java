/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.tmrk.enterprisecloud.domain;

import com.google.common.collect.Sets;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wraps individual Action elements.
 * Needed because parsing is done with JAXB and it does not handle Generic collections
 * @author Jason King
 */
public class Actions {

   @SuppressWarnings("unchecked")
   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromActions(this);
   }

   public static class Builder {

       private Set<Action> actions = Sets.newLinkedHashSet();

       /**
        * @see Actions#getActions
        */
       public Builder actions(Set<Action> actions) {
          this.actions = Sets.newLinkedHashSet(checkNotNull(actions, "actions"));
          return this;
       }

       public Builder addAction(Action action) {
          actions.add(checkNotNull(action,"action"));
          return this;
       }

       public Actions build() {
           return new Actions(actions);
       }

       public Builder fromActions(Actions in) {
         return actions(in.getActions());
       }
   }

   private Actions() {
      //For JAXB and builder use
   }

   private Actions(Set<Action> actions) {
      this.actions = Sets.newLinkedHashSet(actions);
   }

   @XmlElement(name = "Action")
   private Set<Action> actions = Sets.newLinkedHashSet();

   public Set<Action> getActions() {
      return Collections.unmodifiableSet(actions);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Actions actions1 = (Actions) o;

      if (!actions.equals(actions1.actions)) return false;

      return true;
   }

   @Override
   public int hashCode() {
      return actions.hashCode();
   }

   public String toString() {
      return "["+ actions.toString()+"]";
   }
}