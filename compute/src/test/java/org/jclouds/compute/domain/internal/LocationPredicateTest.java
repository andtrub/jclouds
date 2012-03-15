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
package org.jclouds.compute.domain.internal;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.HardwareBuilder;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.testng.annotations.Test;

import com.google.common.base.Suppliers;

/**
 * 
 * @author Adrian Cole
 */
@Test(testName = "LocationPredicateTest")
public class LocationPredicateTest {
   Location provider = new LocationBuilder().scope(LocationScope.PROVIDER).id("aws-ec2").description("aws-ec2").build();

   Location region = new LocationBuilder().scope(LocationScope.REGION).id("us-east-1").description("us-east-1")
         .parent(provider).build();

   Location zone = new LocationBuilder().scope(LocationScope.ZONE).id("us-east-1a").description("us-east-1a")
         .parent(region).build();

   Location host = new LocationBuilder().scope(LocationScope.HOST).id("xxxxx").description("xxxx").parent(zone).build();

   Location otherRegion = new LocationBuilder().scope(LocationScope.REGION).id("us-west-1").description("us-west-1")
         .parent(provider).build();

   Location otherZone = new LocationBuilder().scope(LocationScope.ZONE).id("us-west-1a").description("us-west-1a")
         .parent(otherRegion).build();

   Location orphanedRegion = new LocationBuilder().scope(LocationScope.REGION).id("us-east-1").description("us-east-1")
         .build();
   
   Location orphanedZone = new LocationBuilder().scope(LocationScope.ZONE).id("us-east-1a")
         .description("us-east-1a").build();
   /**
    * If the current location id is null, then we don't care where to launch a
    */
   public void testReturnTrueWhenIDontSpecifyALocation() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.<Location> ofInstance(null));
      Hardware md = new HardwareBuilder().id("foo").location(region).build();
      assertTrue(predicate.apply(md));
   }

   /**
    * If the input location is null, then the data isn't location sensitive
    */
   public void testReturnTrueWhenISpecifyALocationAndInputLocationIsNull() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(region));
      Hardware md = new HardwareBuilder().id("foo").location(null).build();
      assertTrue(predicate.apply(md));
   }

   /**
    * If the input location is null, then the data isn't location sensitive
    */
   public void testReturnTrueWhenIDontSpecifyALocationAndInputLocationIsNull() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.<Location> ofInstance(null));
      Hardware md = new HardwareBuilder().id("foo").location(null).build();
      assertTrue(predicate.apply(md));
   }

   @Test
   public void testReturnTrueWhenISpecifyARegionAndInputLocationIsProvider() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(region));
      Hardware md = new HardwareBuilder().id("foo").location(provider).build();
      assertTrue(predicate.apply(md));
   }

   /**
    * When locations are equal
    */
   @Test
   public void testReturnFalseWhenISpecifyALocationWhichTheSameScopeByNotEqualToInputLocationAndParentsAreNull() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(region));
      Hardware md = new HardwareBuilder().id("foo").location(otherRegion).build();
      assertFalse(predicate.apply(md));
   }

   /**
    * If the input location is null, then the data isn't location sensitive
    */
   public void testReturnFalseWhenISpecifyALocationWhichTheSameScopeByNotEqualToInputLocationAndParentsAreNotNull() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(zone));
      Hardware md = new HardwareBuilder().id("foo").location(otherZone).build();
      assertFalse(predicate.apply(md));
   }

   /**
    * If the input location is a parent of the specified location, then we are
    * ok.
    */
   public void testReturnTrueWhenISpecifyALocationWhichIsAChildOfInput() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(zone));
      Hardware md = new HardwareBuilder().id("foo").location(region).build();
      assertTrue(predicate.apply(md));
   }

   /**
    * If the input location is a parent of the specified location, then we are
    * ok.
    */
   public void testReturnFalseWhenISpecifyALocationWhichIsNotAChildOfInput() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(zone));
      Hardware md = new HardwareBuilder().id("foo").location(otherRegion).build();
      assertFalse(predicate.apply(md));
   }

   /**
    * If the input location is a grandparent of the specified location, then we
    * are ok.
    */
   public void testReturnTrueWhenISpecifyALocationWhichIsAGrandChildOfInput() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(host));

      Hardware md = new HardwareBuilder().id("foo").location(host).build();
      assertTrue(predicate.apply(md));
   }

   /**
    * If the input location is a grandparent of the specified location, then we
    * are ok.
    */
   public void testReturnFalseWhenISpecifyALocationWhichIsNotAGrandChildOfInput() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(host));
      Hardware md = new HardwareBuilder().id("foo").location(otherRegion).build();
      assertFalse(predicate.apply(md));
   }
   
   /**
    * a provider is not an assignable location.
    * 
    * For example, all cloud providers to date, vms are assigned to zones or
    * regions, and listAssignableLocations does not include elements of PROVIDER scope.
    * <p/>
    * 
    * If someone somehow gets a hold of a provider instance, this should throw an IllegalArgumentException.
    * Asking to assign this to a provider, is not the correct syntax for
    * 
    * FIXME: this should not NPE,
    */
   @Test(enabled = false, expectedExceptions = IllegalArgumentException.class)
   public void testThrowIllegalArgumentExceptionWhenWhenISpecifyAProviderAndInputLocationAsOpposedToNull() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(provider));
      Hardware md = new HardwareBuilder().id("foo").location(region).build();
      predicate.apply(md);
   }

   /**
    * Only the PROVIDER scope should have a null parent, It is an illegal state if a ZONE or REGION are orphaned
    * 
    * FIXME: this should not NPE,
    */
   @Test(enabled = false, expectedExceptions = IllegalStateException.class)
   public void testThrowIllegalStateExceptionWhenInputIsAnOrphanedRegion() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(region));
      Hardware md = new HardwareBuilder().id("foo").location(orphanedRegion).build();
      predicate.apply(md);
   }
   
   /**
    * Only the PROVIDER scope should have a null parent, It is an illegal state if a ZONE or REGION are orphaned
    * 
    * FIXME: this should not NPE,
    */
   @Test(enabled = false, expectedExceptions = IllegalStateException.class)
   public void testThrowIllegalStateExceptionWhenInputIsAnOrphanedZone() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(region));
      Hardware md = new HardwareBuilder().id("foo").location(orphanedZone).build();
      predicate.apply(md);
   }

   /**
    * Only the PROVIDER scope should have a null parent, It is an illegal state if a ZONE or REGION are orphaned
    * 
    * FIXME: this should not NPE,
    */
   @Test(enabled = false, expectedExceptions = IllegalArgumentException.class)
   public void testThrowIllegalArgumentExceptionWhenWhenISpecifyAnOrphanedRegion() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(orphanedRegion));
      Hardware md = new HardwareBuilder().id("foo").location(region).build();
      predicate.apply(md);
   }
   
   /**
    * Only the PROVIDER scope should have a null parent, It is an illegal state if a ZONE or REGION are orphaned
    * 
    * FIXME: this should not NPE,
    */
   @Test(enabled = false, expectedExceptions = IllegalArgumentException.class)
   public void testThrowIllegalArgumentExceptionWhenWhenISpecifyAnOrphanedZone() {
      LocationPredicate predicate = new LocationPredicate(Suppliers.ofInstance(orphanedZone));
      Hardware md = new HardwareBuilder().id("foo").location(region).build();
      predicate.apply(md);
   }

}