package org.palladiosimulator.protocom.lang.txt.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.txt.IReadMe;

@SuppressWarnings("all")
public class JeeReadMe extends GeneratedFile<IReadMe> implements IReadMe {
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("The generated Java EE performance prototype contains the method \"setContext\" and the \"throw RemoteException\"");
    _builder.newLine();
    _builder.append("statement which have to be removed before the deployment on a real server. The generation of these is needed ");
    _builder.newLine();
    _builder.append("because of the ProtoCom framework that isn\'t changed to support Java EE. It is fit to Java SE. ");
    _builder.newLine();
    _builder.newLine();
    _builder.append("The \"setContext\" method, which has to be removed, is in the classes that represent the BasicComponents:");
    _builder.newLine();
    {
      HashMap<String, String> _basicComponentClassName = this.basicComponentClassName();
      Set<String> _keySet = _basicComponentClassName.keySet();
      for(final String basicComponentClassProject : _keySet) {
        _builder.append("The class ");
        HashMap<String, String> _basicComponentClassName_1 = this.basicComponentClassName();
        String _get = _basicComponentClassName_1.get(basicComponentClassProject);
        _builder.append(_get, "");
        _builder.append(" in the project ");
        _builder.append(this.projectURI, "");
        _builder.append(basicComponentClassProject, "");
        _builder.append(". ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("The \"throw RemoteException\" has to be removed from the Port classes of the BasicComponents: ");
    _builder.newLine();
    {
      ArrayListMultimap<String, String> _basicComponentPortClassName = this.basicComponentPortClassName();
      Set<String> _keySet_1 = _basicComponentPortClassName.keySet();
      for(final String basicComponentPortClassProject : _keySet_1) {
        {
          ArrayListMultimap<String, String> _basicComponentPortClassName_1 = this.basicComponentPortClassName();
          List<String> _get_1 = _basicComponentPortClassName_1.get(basicComponentPortClassProject);
          for(final String basicComponentPortClass : _get_1) {
            _builder.append("The class ");
            _builder.append(basicComponentPortClass, "");
            _builder.append(" in the project ");
            _builder.append(this.projectURI, "");
            _builder.append(basicComponentPortClassProject, "");
            _builder.append(".");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("Also note that the current EJB ProtoType is tied to a Glassfish 4 runtime environment. We currently require to have such an");
    _builder.newLine();
    _builder.append("environment with the name \"GlassFish 4\" installed. Check your Eclipse settings under \"Server -> Runtime Environments\" to");
    _builder.newLine();
    _builder.append("check this requirement. In future versions, we plan to parametrize the target runtime; see:");
    _builder.newLine();
    _builder.append("org.palladiosimulator.protocom/src/org/palladiosimulator/protocom/tech/iiop/repository/JavaEEIIOPFacetCore.xtend");
    _builder.newLine();
    return _builder.toString();
  }
  
  @Override
  public HashMap<String, String> basicComponentClassName() {
    return this.provider.basicComponentClassName();
  }
  
  @Override
  public ArrayListMultimap<String, String> basicComponentPortClassName() {
    return this.provider.basicComponentPortClassName();
  }
}
