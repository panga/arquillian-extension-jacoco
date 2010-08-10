/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.framework.jacoco.container;

import java.io.FileOutputStream;

import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.IRuntime;
import org.jboss.arquillian.spi.Context;
import org.jboss.arquillian.spi.event.suite.EventHandler;
import org.jboss.arquillian.spi.event.suite.SuiteEvent;

/**
 * StartCoverageData
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class ShutdownCoverageData implements EventHandler<SuiteEvent>
{
   /* (non-Javadoc)
    * @see org.jboss.arquillian.spi.event.suite.EventHandler#callback(org.jboss.arquillian.spi.Context, java.lang.Object)
    */
   public void callback(Context context, SuiteEvent event) throws Exception
   {
      IRuntime runtime = context.get(IRuntime.class);
      if(runtime != null)
      {
         FileOutputStream coverageFile = null;
         try
         {
            coverageFile = new FileOutputStream(
                  "/home/aslak/dev/source/testing/arquillian/frameworks/jacoco/target/coverage.data");
   
            ExecutionDataWriter writer = new ExecutionDataWriter(coverageFile);
            runtime.collect(writer, writer, true);
         } 
         finally 
         {
            runtime.shutdown();
            if(coverageFile != null)
            {
               try
               {
                  coverageFile.close();
               }
               catch (Exception e) 
               {
                  throw new RuntimeException("Could not close coverage file", e);
               }
            }
         }
      }
   }
}
