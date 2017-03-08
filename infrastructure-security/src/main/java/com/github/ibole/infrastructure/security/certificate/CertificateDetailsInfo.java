package com.github.ibole.infrastructure.security.certificate;

import java.util.Calendar;
import java.util.Date;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


public class CertificateDetailsInfo {
  private String alias;

  /**
   * Issuer name
   */
  private String commonName;

  /**
   * Owner name
   */
  private String organization;

  private String organizationUnit;

  private String locality;

  private String country;

  private String state;

  private String entryPassword; // this is NOT the keystore password

  private Date expirationDate;
  
  private String emailAddress;

  public CertificateDetailsInfo(String alias, String commonName, String organization,
      String organizationUnit, String locality, String country, String state, String validity, String emailAddress,
      String entryPassword) {
    this.alias = alias;
    this.commonName = commonName;
    this.organization = organization;
    this.organizationUnit = organizationUnit;
    this.locality = locality;
    this.country = country;
    this.state = state;
    this.emailAddress = emailAddress;
    this.entryPassword = entryPassword;

    int validityYears = Integer.parseInt(validity);

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, validityYears);

    this.expirationDate = cal.getTime();
  }

  /**
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @return the commonName
   */
  public String getCommonName() {
    return commonName;
  }

  /**
   * @return the organization
   */
  public String getOrganization() {
    return organization;
  }

  /**
   * @return the organizationUnit
   */
  public String getOrganizationUnit() {
    return organizationUnit;
  }

  /**
   * @return the locality
   */
  public String getLocality() {
    return locality;
  }

  /**
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * @return the expirationDate
   */
  public Date getExpirationDate() {
    return expirationDate;
  }

  /**
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * @return the emailAddress
   */
  public String getEmailAddress() {
    return emailAddress;
  }

  /**
   * @param emailAddress the emailAddress to set
   */
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  /**
   * @return the keyPassword
   */
  public String getEntryPassword() {
    return entryPassword;
  }

}
