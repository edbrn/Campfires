package com.edbrn.Campfires.files.jsonmodel;

import java.util.ArrayList;
import java.util.Map;

public final class CampfireConfig {
  public Map<String, ArrayList<Campfire>> campfires;

  public CampfireConfig(Map<String, ArrayList<Campfire>> campfires) {
    this.campfires = campfires;
  }
}
