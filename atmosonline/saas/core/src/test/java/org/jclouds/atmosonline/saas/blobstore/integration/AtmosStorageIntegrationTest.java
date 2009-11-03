/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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
 * ====================================================================
 */
package org.jclouds.atmosonline.saas.blobstore.integration;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.jclouds.atmosonline.saas.AtmosStorageClient;
import org.jclouds.blobstore.integration.internal.BaseBlobIntegrationTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = { "integration", "live" }, testName = "emcsaas.AtmosStorageIntegrationTest")
public class AtmosStorageIntegrationTest extends BaseBlobIntegrationTest<AtmosStorageClient> {

   @DataProvider(name = "delete")
   // no unicode support
   @Override
   public Object[][] createData() {
      return new Object[][] { { "normal" } };
   }

   @Override
   @Test(enabled = false)
   public void testGetIfMatch() throws InterruptedException, ExecutionException, TimeoutException,
            IOException {
      // no etag support
   }

   @Override
   @Test(enabled = false)
   public void testGetIfModifiedSince() throws InterruptedException, ExecutionException,
            TimeoutException, IOException {
      // not supported
   }

   @Override
   @Test(enabled = false)
   public void testGetIfNoneMatch() throws InterruptedException, ExecutionException,
            TimeoutException, IOException {
      // no etag support
   }

   @Override
   @Test(enabled = false)
   public void testGetIfUnmodifiedSince() throws InterruptedException, ExecutionException,
            TimeoutException, IOException {
      // not supported
   }

   @Override
   @Test(enabled = false)
   public void testGetTwoRanges() throws InterruptedException, ExecutionException,
            TimeoutException, IOException {
      // not supported
   }

}