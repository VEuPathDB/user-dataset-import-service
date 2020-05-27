package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProjectType {
  @JsonProperty("AmoebaDB")
  AMOEBADB("AmoebaDB"),

  @JsonProperty("ClinepiDB")
  CLINEPIDB("ClinepiDB"),

  @JsonProperty("CryptoDB")
  CRYPTODB("CryptoDB"),

  @JsonProperty("FungiDB")
  FUNGIDB("FungiDB"),

  @JsonProperty("GiardiaDB")
  GIARDIADB("GiardiaDB"),

  @JsonProperty("HostDB")
  HOSTDB("HostDB"),

  @JsonProperty("MicrobiomeDB")
  MICROBIOMEDB("MicrobiomeDB"),

  @JsonProperty("MicrosporidiaDB")
  MICROSPORIDIADB("MicrosporidiaDB"),

  @JsonProperty("Orthomcl")
  ORTHOMCL("Orthomcl"),

  @JsonProperty("PiroplasmaDB")
  PIROPLASMADB("PiroplasmaDB"),

  @JsonProperty("PlasmoDB")
  PLASMODB("PlasmoDB"),

  @JsonProperty("SchistoDB")
  SCHISTODB("SchistoDB"),

  @JsonProperty("ToxoDB")
  TOXODB("ToxoDB"),

  @JsonProperty("TrichDB")
  TRICHDB("TrichDB"),

  @JsonProperty("TritrypDB")
  TRITRYPDB("TritrypDB"),

  @JsonProperty("Vectorbase")
  VECTORBASE("Vectorbase");

  private final String name;

  ProjectType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
