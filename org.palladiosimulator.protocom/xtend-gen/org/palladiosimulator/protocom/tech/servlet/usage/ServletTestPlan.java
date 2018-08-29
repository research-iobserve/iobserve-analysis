package org.palladiosimulator.protocom.tech.servlet.usage;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.Workload;
import org.palladiosimulator.protocom.lang.xml.ITestPlan;
import org.palladiosimulator.protocom.model.repository.OperationInterfaceAdapter;
import org.palladiosimulator.protocom.model.repository.OperationProvidedRoleAdapter;
import org.palladiosimulator.protocom.model.repository.SignatureAdapter;
import org.palladiosimulator.protocom.model.usage.BranchAdapter;
import org.palladiosimulator.protocom.model.usage.BranchTransitionAdapter;
import org.palladiosimulator.protocom.model.usage.DelayAdapter;
import org.palladiosimulator.protocom.model.usage.EntryLevelSystemCallAdapter;
import org.palladiosimulator.protocom.model.usage.LoopAdapter;
import org.palladiosimulator.protocom.model.usage.ScenarioBehaviourAdapter;
import org.palladiosimulator.protocom.model.usage.StartAdapter;
import org.palladiosimulator.protocom.model.usage.StopAdapter;
import org.palladiosimulator.protocom.model.usage.UsageScenarioAdapter;
import org.palladiosimulator.protocom.model.usage.UserActionAdapter;
import org.palladiosimulator.protocom.tech.ConceptMapping;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ServletTestPlan extends ConceptMapping<UsageScenario> implements ITestPlan {
  private final UsageScenarioAdapter entity;
  
  public ServletTestPlan(final UsageScenarioAdapter entity, final UsageScenario pcmEntity) {
    super(pcmEntity);
    this.entity = entity;
  }
  
  private CharSequence buildRequest(final String method) {
    CharSequence _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("[\"de.uka.ipd.sdq.simucomframework.variables.StackContext\"]");
      final String formalTypes = _builder.toString();
      final String actualTypes = formalTypes;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("{\"name\":\"");
      _builder_1.append(method, "");
      _builder_1.append("\",\"formalTypes\":");
      _builder_1.append(formalTypes, "");
      _builder_1.append(",\"actualTypes\":");
      _builder_1.append(actualTypes, "");
      _builder_1.append(",\"arguments\":[{}]}");
      _xblockexpression = _builder_1;
    }
    return _xblockexpression;
  }
  
  /**
   * @param action
   */
  private String userActions(final UserActionAdapter<? extends AbstractUserAction> action) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _userAction = this.userAction(action);
    _builder.append(_userAction, "");
    _builder.newLineIfNotEmpty();
    {
      boolean _isInstance = StopAdapter.class.isInstance(action);
      boolean _not = (!_isInstance);
      if (_not) {
        UserActionAdapter<? extends AbstractUserAction> _successor = action.getSuccessor();
        String _userActions = this.userActions(_successor);
        _builder.append(_userActions, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  private CharSequence _userAction(final StartAdapter action) {
    return null;
  }
  
  private CharSequence _userAction(final StopAdapter action) {
    return null;
  }
  
  private CharSequence _userAction(final EntryLevelSystemCallAdapter action) {
    CharSequence _xblockexpression = null;
    {
      OperationProvidedRoleAdapter _providedRole = action.getProvidedRole();
      final String port = _providedRole.getPortClassName();
      SignatureAdapter _operationSignature = action.getOperationSignature();
      final String method = _operationSignature.getSignatureName();
      OperationProvidedRoleAdapter _providedRole_1 = action.getProvidedRole();
      OperationInterfaceAdapter _providedInterface = _providedRole_1.getProvidedInterface();
      String _safeName = _providedInterface.getSafeName();
      String _plus = (_safeName + ".");
      SignatureAdapter _operationSignature_1 = action.getOperationSignature();
      String _name = _operationSignature_1.getName();
      final String name = (_plus + _name);
      final CharSequence request = this.buildRequest(method);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<HTTPSamplerProxy guiclass=\"HttpTestSampleGui\" testclass=\"HTTPSamplerProxy\" testname=\"");
      _builder.append(name, "");
      _builder.append("\" enabled=\"true\">");
      _builder.newLineIfNotEmpty();
      _builder.append("  ");
      _builder.append("<boolProp name=\"HTTPSampler.postBodyRaw\">true</boolProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\">");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<collectionProp name=\"Arguments.arguments\">");
      _builder.newLine();
      _builder.append("      ");
      _builder.append("<elementProp name=\"\" elementType=\"HTTPArgument\">");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("<boolProp name=\"HTTPArgument.always_encode\">false</boolProp>");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("<stringProp name=\"Argument.value\">");
      _builder.append(request, "        ");
      _builder.append("</stringProp>");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("<stringProp name=\"Argument.metadata\"></stringProp>");
      _builder.newLine();
      _builder.append("      ");
      _builder.append("</elementProp>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("</collectionProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("</elementProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.domain\"></stringProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.port\"></stringProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.connect_timeout\"></stringProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.response_timeout\"></stringProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.protocol\"></stringProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.contentEncoding\"></stringProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.path\">org.palladiosimulator.temporary/");
      _builder.append(port, "  ");
      _builder.append("</stringProp>");
      _builder.newLineIfNotEmpty();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.method\">POST</stringProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<boolProp name=\"HTTPSampler.follow_redirects\">true</boolProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<boolProp name=\"HTTPSampler.auto_redirects\">false</boolProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<boolProp name=\"HTTPSampler.use_keepalive\">true</boolProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<boolProp name=\"HTTPSampler.DO_MULTIPART_POST\">false</boolProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<boolProp name=\"HTTPSampler.monitor\">false</boolProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"HTTPSampler.embedded_url_re\"></stringProp>");
      _builder.newLine();
      _builder.append("</HTTPSamplerProxy>");
      _builder.newLine();
      _builder.append("<hashTree/>");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  private CharSequence _userAction(final BranchAdapter action) {
    CharSequence _xblockexpression = null;
    {
      List<BranchTransitionAdapter> _branchTransitions = action.getBranchTransitions();
      final Function1<BranchTransitionAdapter, Double> _function = (BranchTransitionAdapter it) -> {
        return Double.valueOf(it.getProbability());
      };
      final List<BranchTransitionAdapter> branches = IterableExtensions.<BranchTransitionAdapter, Double>sortBy(_branchTransitions, _function);
      int value = 0;
      BigDecimal p = new BigDecimal("0.0");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("double val = new Random().nextDouble();");
      _builder.newLine();
      _builder.newLine();
      {
        boolean _hasElements = false;
        for(final BranchTransitionAdapter branch : branches) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(" else ", "");
          }
          _builder.append("if (val &lt; ");
          String _plainString = (p = p.add(new BigDecimal(Double.valueOf(branch.getProbability()).toString()))).toPlainString();
          _builder.append(_plainString, "");
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("vars.put(&quot;BRANCH&quot;, &quot;");
          int _plusPlus = value++;
          _builder.append(_plusPlus, "\t");
          _builder.append("&quot;);");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
        }
      }
      _builder.newLineIfNotEmpty();
      final String script = _builder.toString();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("<BeanShellSampler guiclass=\"BeanShellSamplerGui\" testclass=\"BeanShellSampler\" testname=\"Branch Selector\" enabled=\"true\">");
      _builder_1.newLine();
      _builder_1.append("  ");
      _builder_1.append("<stringProp name=\"BeanShellSampler.query\">");
      _builder_1.append(script, "  ");
      _builder_1.append("</stringProp>");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("  ");
      _builder_1.append("<stringProp name=\"BeanShellSampler.filename\"></stringProp>");
      _builder_1.newLine();
      _builder_1.append("  ");
      _builder_1.append("<stringProp name=\"BeanShellSampler.parameters\"></stringProp>");
      _builder_1.newLine();
      _builder_1.append("  ");
      _builder_1.append("<boolProp name=\"BeanShellSampler.resetInterpreter\">false</boolProp>");
      _builder_1.newLine();
      _builder_1.append("</BeanShellSampler>");
      _builder_1.newLine();
      _builder_1.append("<hashTree/>");
      _builder_1.newLine();
      _builder_1.append("<SwitchController guiclass=\"SwitchControllerGui\" testclass=\"SwitchController\" testname=\"Branch\" enabled=\"true\">");
      _builder_1.newLine();
      _builder_1.append("  ");
      _builder_1.append("<stringProp name=\"SwitchController.value\">${BRANCH}</stringProp>");
      _builder_1.newLine();
      _builder_1.append("</SwitchController>");
      _builder_1.newLine();
      _builder_1.append("<hashTree>");
      _builder_1.newLine();
      {
        for(final BranchTransitionAdapter branch_1 : branches) {
          _builder_1.append("  ");
          _builder_1.append("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"p = ");
          double _probability = branch_1.getProbability();
          _builder_1.append(_probability, "  ");
          _builder_1.append("\" enabled=\"true\"/>");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("  ");
          _builder_1.append("<hashTree>");
          _builder_1.newLine();
          _builder_1.append("  ");
          _builder_1.append("  ");
          StartAdapter _start = branch_1.getStart();
          String _userActions = this.userActions(_start);
          _builder_1.append(_userActions, "    ");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("  ");
          _builder_1.append("</hashTree>");
          _builder_1.newLine();
        }
      }
      _builder_1.append("</hashTree>");
      _builder_1.newLine();
      _xblockexpression = _builder_1;
    }
    return _xblockexpression;
  }
  
  private CharSequence _userAction(final LoopAdapter action) {
    CharSequence _xblockexpression = null;
    {
      int _xtrycatchfinallyexpression = (int) 0;
      try {
        int _xblockexpression_1 = (int) 0;
        {
          String _iterationCount = action.getIterationCount();
          final String spec = this.entity.safeSpecification(_iterationCount);
          _xblockexpression_1 = Integer.parseInt(spec);
        }
        _xtrycatchfinallyexpression = _xblockexpression_1;
      } catch (final Throwable _t) {
        if (_t instanceof NumberFormatException) {
          final NumberFormatException e = (NumberFormatException)_t;
          _xtrycatchfinallyexpression = 1;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final int iterations = _xtrycatchfinallyexpression;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<LoopController guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop\" enabled=\"true\">");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<boolProp name=\"LoopController.continue_forever\">true</boolProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"LoopController.loops\">");
      _builder.append(iterations, "  ");
      _builder.append("</stringProp>");
      _builder.newLineIfNotEmpty();
      _builder.append("</LoopController>");
      _builder.newLine();
      _builder.append("<hashTree>");
      _builder.newLine();
      _builder.append("  ");
      ScenarioBehaviourAdapter _scenarioBehaviour = action.getScenarioBehaviour();
      StartAdapter _start = _scenarioBehaviour.getStart();
      String _userActions = this.userActions(_start);
      _builder.append(_userActions, "  ");
      _builder.newLineIfNotEmpty();
      _builder.append("</hashTree>");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  private CharSequence _userAction(final DelayAdapter action) {
    CharSequence _xblockexpression = null;
    {
      int _xtrycatchfinallyexpression = (int) 0;
      try {
        int _xblockexpression_1 = (int) 0;
        {
          String _delay = action.getDelay();
          final String spec = this.entity.safeSpecification(_delay);
          double _parseDouble = Double.parseDouble(spec);
          double _multiply = (_parseDouble * 1000);
          _xblockexpression_1 = ((int) _multiply);
        }
        _xtrycatchfinallyexpression = _xblockexpression_1;
      } catch (final Throwable _t) {
        if (_t instanceof NumberFormatException) {
          final NumberFormatException e = (NumberFormatException)_t;
          _xtrycatchfinallyexpression = 0;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      int delay = _xtrycatchfinallyexpression;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<TestAction guiclass=\"TestActionGui\" testclass=\"TestAction\" testname=\"Delay\" enabled=\"true\">");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<intProp name=\"ActionProcessor.action\">1</intProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<intProp name=\"ActionProcessor.target\">0</intProp>");
      _builder.newLine();
      _builder.append("  ");
      _builder.append("<stringProp name=\"ActionProcessor.duration\">");
      _builder.append(delay, "  ");
      _builder.append("</stringProp>");
      _builder.newLineIfNotEmpty();
      _builder.append("</TestAction>");
      _builder.newLine();
      _builder.append("<hashTree/>");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  @Override
  public int population() {
    int _xblockexpression = (int) 0;
    {
      Workload workload = this.pcmEntity.getWorkload_UsageScenario();
      int _switchResult = (int) 0;
      boolean _matched = false;
      if (!_matched) {
        if (workload instanceof ClosedWorkload) {
          _matched=true;
          _switchResult = ((ClosedWorkload)workload).getPopulation();
        }
      }
      if (!_matched) {
        _switchResult = 1;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  @Override
  public int thinkTime() {
    int _xblockexpression = (int) 0;
    {
      Workload workload = this.pcmEntity.getWorkload_UsageScenario();
      int _switchResult = (int) 0;
      boolean _matched = false;
      if (!_matched) {
        if (workload instanceof ClosedWorkload) {
          _matched=true;
          int _xtrycatchfinallyexpression = (int) 0;
          try {
            int _xblockexpression_1 = (int) 0;
            {
              final PCMRandomVariable time = ((ClosedWorkload)workload).getThinkTime_ClosedWorkload();
              String _specification = time.getSpecification();
              final String spec = this.entity.safeSpecification(_specification);
              double _parseDouble = Double.parseDouble(spec);
              double _multiply = (_parseDouble * 1000);
              _xblockexpression_1 = ((int) _multiply);
            }
            _xtrycatchfinallyexpression = _xblockexpression_1;
          } catch (final Throwable _t) {
            if (_t instanceof NumberFormatException) {
              final NumberFormatException e = (NumberFormatException)_t;
              _xtrycatchfinallyexpression = 0;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
          _switchResult = _xtrycatchfinallyexpression;
        }
      }
      if (!_matched) {
        _switchResult = 0;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  @Override
  public String scenarioName() {
    return this.pcmEntity.getEntityName();
  }
  
  @Override
  public String filePath() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/src/usagescenarios/jmx/");
    String _safeName = this.entity.getSafeName();
    _builder.append(_safeName, "");
    _builder.append(".jmx");
    return _builder.toString();
  }
  
  @Override
  public String projectName() {
    return null;
  }
  
  @Override
  public String content() {
    ScenarioBehaviourAdapter _scenarioBehaviour = this.entity.getScenarioBehaviour();
    StartAdapter _start = _scenarioBehaviour.getStart();
    return this.userActions(_start);
  }
  
  private CharSequence userAction(final UserActionAdapter<? extends AbstractUserAction> action) {
    if (action instanceof BranchAdapter) {
      return _userAction((BranchAdapter)action);
    } else if (action instanceof DelayAdapter) {
      return _userAction((DelayAdapter)action);
    } else if (action instanceof EntryLevelSystemCallAdapter) {
      return _userAction((EntryLevelSystemCallAdapter)action);
    } else if (action instanceof LoopAdapter) {
      return _userAction((LoopAdapter)action);
    } else if (action instanceof StartAdapter) {
      return _userAction((StartAdapter)action);
    } else if (action instanceof StopAdapter) {
      return _userAction((StopAdapter)action);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(action).toString());
    }
  }
}
