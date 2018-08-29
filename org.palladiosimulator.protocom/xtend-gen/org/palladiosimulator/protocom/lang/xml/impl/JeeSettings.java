package org.palladiosimulator.protocom.lang.xml.impl;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IJeeSettings;

@SuppressWarnings("all")
public class JeeSettings extends GeneratedFile<IJeeSettings> implements IJeeSettings {
  @Override
  public String generate() {
    return this.content();
  }
  
  @Override
  public String content() {
    return this.provider.content();
  }
}
