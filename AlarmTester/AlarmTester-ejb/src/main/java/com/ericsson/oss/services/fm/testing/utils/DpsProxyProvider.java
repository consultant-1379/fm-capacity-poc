/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.fm.testing.utils;

import javax.enterprise.context.ApplicationScoped;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;

@ApplicationScoped
public class DpsProxyProvider {
    @EServiceRef
    private DataPersistenceService dps;

    public DataPersistenceService getDataPersistenceService() {
        return dps;
    }

    public DataBucket getLiveBucket() {
        return dps.getLiveBucket();
    }

    public QueryBuilder getQueryBuilder() {
        return dps.getQueryBuilder();
    }
}