/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson AB. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.service.fm.testing.api;

/**
 * Class for general constants.
 */
public final class AddInfoConstants {

    public static final String INVALID_TARGET_ADDITIONAL_INFORMATION = "Invalid targetAdditionalInformation";
    public static final String NO_ADDITIONAL_ATTRIBUTES = "Additional attributes not found";
    public static final String NO_TARGET_ADDITIONAL_INFORMATION = "targetAdditionalInformation not found";

    public static final String NO_CI_KEY = "CI key not present";
    public static final String NO_COMPUTE_RESOURCE = "C tag not present, is required";
    public static final String JSON_EXCEPTION = "Json exception found: ";
    public static final String NEITHER_P_NOR_S = "Neither P nor S tags present";
    public static final String BOTH_P_AND_S = "Both P and S tags present";

    public static final String INFOCI_ROOT = "ROOT";

    public static final String TARGET_ADDITIONAL_INFORMATION = "targetAdditionalInformation";
    public static final String CORRELATION_INFORMATION = "CI";
    public static final String CI_GROUP_1 = "ciFirstGroup";
    public static final String CI_GROUP_2 = "ciSecondGroup";
    public static final String ROOT = "root";
    public static final String PRIMARY_TAG = "P";
    public static final String SECONDARY_TAG = "S";
    public static final String COMPUTE_RESOURCE_TAG = "C";
    public static final String FAKE_KEY_TAG = "F";

    private AddInfoConstants() {
    }

}
