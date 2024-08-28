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
package com.ericsson.oss.service.fm.testing.model;

public class AlarmTestingInfo {
    String type;
    Long id;
    String fdn;
    String oor;
    long poid = -2;

    /**
     * @return the poid
     */
    public long getPoid() {
        return poid;
    }

    /**
     * @param poid
     *            the poid to set
     */
    public void setPoid(final long poid) {
        this.poid = poid;
    }

    public AlarmTestingInfo(final String type, final Long id, final String fdn, final String oor) {
        this.type = type;
        this.id = id;
        this.fdn = fdn;
        this.oor = oor;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the fdn
     */
    public String getFdn() {
        return fdn;
    }

    /**
     * @param fdn
     *            the fdn to set
     */
    public void setFdn(final String fdn) {
        this.fdn = fdn;
    }

    /**
     * @return the oor
     */
    public String getOor() {
        return oor;
    }

    /**
     * @param oor
     *            the oor to set
     */
    public void setOor(final String oor) {
        this.oor = oor;
    }

    @Override
    public String toString() {
        return id.toString() + " " + oor + " " + fdn + " " + type;
    }

}
