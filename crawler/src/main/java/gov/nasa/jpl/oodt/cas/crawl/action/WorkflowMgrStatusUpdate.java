/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package gov.nasa.jpl.oodt.cas.crawl.action;

//JDK imports
import java.io.File;
import java.net.URL;

//OODT imports
import gov.nasa.jpl.oodt.cas.crawl.structs.exceptions.CrawlerActionException;
import gov.nasa.jpl.oodt.cas.filemgr.metadata.CoreMetKeys;
import gov.nasa.jpl.oodt.cas.metadata.Metadata;
import gov.nasa.jpl.oodt.cas.workflow.system.XmlRpcWorkflowManagerClient;

import org.springframework.beans.factory.annotation.Required;

/**
 * 
 * @author bfoster
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Updates the Workflow Manager and notifies it that the crawled {@link Product}
 * has been ingested successfully
 * </p>.
 */
public class WorkflowMgrStatusUpdate extends CrawlerAction implements
        CoreMetKeys {

    public String ingestSuffix;

    public String workflowMgrUrl;

    public WorkflowMgrStatusUpdate() {
    	ingestSuffix = "Ingest";
    }

    public boolean performAction(File product, Metadata productMetadata)
            throws CrawlerActionException {
        try {
            XmlRpcWorkflowManagerClient wClient = new XmlRpcWorkflowManagerClient(
                    new URL(this.workflowMgrUrl));
            String ingestSuffix = this.ingestSuffix;
            return wClient.sendEvent(productMetadata.getMetadata(PRODUCT_TYPE)
                    + ingestSuffix, productMetadata);
        } catch (Exception e) {
            throw new CrawlerActionException(
                    "Failed to update workflow manager : " + e.getMessage());
        }
    }

    public void setIngestSuffix(String ingestSuffix) {
        this.ingestSuffix = ingestSuffix;
    }

    @Required
    public void setWorkflowMgrUrl(String workflowMgrUrl) {
        this.workflowMgrUrl = workflowMgrUrl;
    }

}