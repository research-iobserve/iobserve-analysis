package org.palladiosimulator.protocom.lang.xml.impl;

import com.google.common.base.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.ITestPlan;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class TestPlan extends GeneratedFile<ITestPlan> implements ITestPlan {
  private final String scriptRegex = "(<stringProp name=\"BeanShellSampler.query\">)(.*?)(</stringProp>)";
  
  /**
   * Searches for script tags and removes the indentation added by Xtend's whitespace handling.
   * This is not required but increases the readability of the scripts in JMeter.
   */
  private String postProcessScripts(final String output) {
    String _xblockexpression = null;
    {
      Pattern pattern = Pattern.compile(this.scriptRegex, Pattern.DOTALL);
      Matcher matcher = pattern.matcher(output);
      StringBuffer result = new StringBuffer();
      while (matcher.find()) {
        {
          String _group = matcher.group(2);
          String script = _group.replaceAll("\n\\u0020*", "\n");
          String _quoteReplacement = Matcher.quoteReplacement(script);
          script = _quoteReplacement;
          matcher.appendReplacement(result, (("$1" + script) + "$3"));
        }
      }
      matcher.appendTail(result);
      _xblockexpression = result.toString();
    }
    return _xblockexpression;
  }
  
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<jmeterTestPlan version=\"1.2\" properties=\"2.6\" jmeter=\"2.11 r1554548\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("    ");
    CharSequence _testPlan = this.testPlan();
    _builder.append(_testPlan, "    ");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("      ");
    CharSequence _arguments = this.arguments();
    _builder.append(_arguments, "      ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("      ");
    CharSequence _requestDefaults = this.requestDefaults();
    _builder.append(_requestDefaults, "      ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("      ");
    CharSequence _setupThreadGroup = this.setupThreadGroup();
    _builder.append(_setupThreadGroup, "      ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("        ");
    String _scenarioName = this.scenarioName();
    CharSequence _httpRequest = this.httpRequest("Start Request", "org.palladiosimulator.temporary/api/experiment/start", "PUT", _scenarioName);
    _builder.append(_httpRequest, "        ");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("      ");
    CharSequence _threadGroup = this.threadGroup();
    _builder.append(_threadGroup, "      ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("        ");
    CharSequence _simpleController = this.simpleController("Start Iteration");
    _builder.append(_simpleController, "        ");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("          ");
    CharSequence _iterationScript = this.iterationScript();
    _builder.append(_iterationScript, "          ");
    _builder.newLineIfNotEmpty();
    _builder.append("          ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("          ");
    CharSequence _httpRequest_1 = this.httpRequest("Start Request", "org.palladiosimulator.temporary/api/experiment/iteration/start/${_ITERATION_ID}", "PUT", null);
    _builder.append(_httpRequest_1, "          ");
    _builder.newLineIfNotEmpty();
    _builder.append("          ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("        ");
    CharSequence _simpleController_1 = this.simpleController("Iteration");
    _builder.append(_simpleController_1, "        ");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("          ");
    String _content = this.content();
    _builder.append(_content, "          ");
    _builder.newLineIfNotEmpty();
    _builder.append("          ");
    CharSequence _thinkTimeDelay = this.thinkTimeDelay();
    _builder.append(_thinkTimeDelay, "          ");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("        ");
    CharSequence _simpleController_2 = this.simpleController("Stop Iteration");
    _builder.append(_simpleController_2, "        ");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("          ");
    CharSequence _httpRequest_2 = this.httpRequest("Stop Request", "org.palladiosimulator.temporary/api/experiment/iteration/stop/${_ITERATION_ID}", "PUT", null);
    _builder.append(_httpRequest_2, "          ");
    _builder.newLineIfNotEmpty();
    _builder.append("          ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("      ");
    CharSequence _postThreadGroup = this.postThreadGroup();
    _builder.append(_postThreadGroup, "      ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<hashTree>");
    _builder.newLine();
    _builder.append("        ");
    CharSequence _httpRequest_3 = this.httpRequest("Stop Request", "org.palladiosimulator.temporary/api/experiment/stop", "PUT", null);
    _builder.append(_httpRequest_3, "        ");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("      ");
    CharSequence _summaryReport = this.summaryReport();
    _builder.append(_summaryReport, "      ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("      ");
    CharSequence _viewResultsTree = this.viewResultsTree();
    _builder.append(_viewResultsTree, "      ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<hashTree/>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</hashTree>");
    _builder.newLine();
    _builder.append("</jmeterTestPlan>");
    _builder.newLine();
    return this.postProcessScripts(_builder.toString());
  }
  
  /**
   * Generates the XML for the test plan.
   * @returns a string containing the XML for the test plan
   */
  private CharSequence testPlan() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"TestPlan.comments\"></stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"TestPlan.functional_mode\">false</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"TestPlan.serialize_threadgroups\">false</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"TestPlan.tearDown_on_shutdown\">true</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<collectionProp name=\"Arguments.arguments\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</elementProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>");
    _builder.newLine();
    _builder.append("</TestPlan>");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * Generates the XML for the HTTP request defaults
   * @returns a string containing the XML for the HTTP request defaults
   */
  private CharSequence requestDefaults() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<ConfigTestElement guiclass=\"HttpDefaultsGui\" testclass=\"ConfigTestElement\" testname=\"HTTP Request Defaults\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\" guiclass=\"HTTPArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<collectionProp name=\"Arguments.arguments\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</elementProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"HTTPSampler.domain\">localhost</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"HTTPSampler.port\">8080</stringProp>");
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
    _builder.append("<stringProp name=\"HTTPSampler.path\"></stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"HTTPSampler.concurrentPool\">4</stringProp>");
    _builder.newLine();
    _builder.append("</ConfigTestElement>");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * Generates the XML for the thread group.
   * @returns a string containing the XML for the thread group
   */
  private CharSequence threadGroup() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<ThreadGroup guiclass=\"ThreadGroupGui\" testclass=\"ThreadGroup\" testname=\"Experiment\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.on_sample_error\">stoptest</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop Controller\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<boolProp name=\"LoopController.continue_forever\">false</boolProp>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<stringProp name=\"LoopController.loops\">${MEASUREMENTS_PER_USER}</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</elementProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.num_threads\">");
    int _population = this.population();
    _builder.append(_population, "  ");
    _builder.append("</stringProp>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.ramp_time\">0</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<longProp name=\"ThreadGroup.start_time\">1409475705000</longProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<longProp name=\"ThreadGroup.end_time\">1409475705000</longProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"ThreadGroup.scheduler\">false</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.duration\"></stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.delay\"></stringProp>");
    _builder.newLine();
    _builder.append("</ThreadGroup>");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * Generates the XML for the summary report.
   * @returns a string containing the XML for the summary report
   */
  private CharSequence summaryReport() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<ResultCollector guiclass=\"SummaryReport\" testclass=\"ResultCollector\" testname=\"Summary Report\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"ResultCollector.error_logging\">false</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<objProp>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<name>saveConfig</name>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<value class=\"SampleSaveConfiguration\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<time>true</time>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<latency>true</latency>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<timestamp>true</timestamp>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<success>true</success>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<label>true</label>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<code>true</code>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<message>true</message>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<threadName>true</threadName>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<dataType>true</dataType>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<encoding>false</encoding>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<assertions>true</assertions>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<subresults>true</subresults>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<responseData>false</responseData>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<samplerData>false</samplerData>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<xml>false</xml>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<fieldNames>false</fieldNames>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<responseHeaders>false</responseHeaders>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<requestHeaders>false</requestHeaders>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<responseDataOnError>false</responseDataOnError>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<assertionsResultsToSave>0</assertionsResultsToSave>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<bytes>true</bytes>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</value>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</objProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"filename\"></stringProp>");
    _builder.newLine();
    _builder.append("</ResultCollector>");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence thinkTimeDelay() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<TestAction guiclass=\"TestActionGui\" testclass=\"TestAction\" testname=\"Think Time\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<intProp name=\"ActionProcessor.action\">1</intProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<intProp name=\"ActionProcessor.target\">0</intProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ActionProcessor.duration\">");
    int _thinkTime = this.thinkTime();
    _builder.append(_thinkTime, "  ");
    _builder.append("</stringProp>");
    _builder.newLineIfNotEmpty();
    _builder.append("</TestAction>");
    _builder.newLine();
    _builder.append("<hashTree/>");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence arguments() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<Arguments guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"Configuration\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<collectionProp name=\"Arguments.arguments\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<elementProp name=\"MEASUREMENTS_PER_USER\" elementType=\"Argument\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<stringProp name=\"Argument.name\">MEASUREMENTS_PER_USER</stringProp>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<stringProp name=\"Argument.value\">1</stringProp>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<stringProp name=\"Argument.desc\">Number of measurements (iterations) per user</stringProp>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<stringProp name=\"Argument.metadata\">=</stringProp>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</elementProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</collectionProp>");
    _builder.newLine();
    _builder.append("</Arguments>");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence setupThreadGroup() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<SetupThreadGroup guiclass=\"SetupThreadGroupGui\" testclass=\"SetupThreadGroup\" testname=\"Start\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.on_sample_error\">stoptest</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop Controller\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<boolProp name=\"LoopController.continue_forever\">false</boolProp>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<stringProp name=\"LoopController.loops\">1</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</elementProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.num_threads\">1</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.ramp_time\">1</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<longProp name=\"ThreadGroup.start_time\">1410291848000</longProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<longProp name=\"ThreadGroup.end_time\">1410291848000</longProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"ThreadGroup.scheduler\">false</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.duration\"></stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.delay\"></stringProp>");
    _builder.newLine();
    _builder.append("</SetupThreadGroup>");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence postThreadGroup() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<PostThreadGroup guiclass=\"PostThreadGroupGui\" testclass=\"PostThreadGroup\" testname=\"Stop\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.on_sample_error\">stoptest</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop Controller\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<boolProp name=\"LoopController.continue_forever\">false</boolProp>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<stringProp name=\"LoopController.loops\">1</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</elementProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.num_threads\">1</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.ramp_time\">1</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<longProp name=\"ThreadGroup.start_time\">1410291879000</longProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<longProp name=\"ThreadGroup.end_time\">1410291879000</longProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"ThreadGroup.scheduler\">false</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.duration\"></stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"ThreadGroup.delay\"></stringProp>");
    _builder.newLine();
    _builder.append("</PostThreadGroup>");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence viewResultsTree() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"Debug\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"ResultCollector.error_logging\">false</boolProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<objProp>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<name>saveConfig</name>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<value class=\"SampleSaveConfiguration\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<time>true</time>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<latency>true</latency>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<timestamp>true</timestamp>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<success>true</success>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<label>true</label>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<code>true</code>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<message>true</message>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<threadName>true</threadName>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<dataType>true</dataType>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<encoding>false</encoding>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<assertions>true</assertions>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<subresults>true</subresults>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<responseData>false</responseData>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<samplerData>false</samplerData>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<xml>false</xml>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<fieldNames>false</fieldNames>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<responseHeaders>false</responseHeaders>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<requestHeaders>false</requestHeaders>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<responseDataOnError>false</responseDataOnError>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<assertionsResultsToSave>0</assertionsResultsToSave>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<bytes>true</bytes>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</value>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</objProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"filename\"></stringProp>");
    _builder.newLine();
    _builder.append("</ResultCollector>");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence httpRequest(final String name, final String path, final String method, final String body) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<HTTPSamplerProxy guiclass=\"HttpTestSampleGui\" testclass=\"HTTPSamplerProxy\" testname=\"");
    _builder.append(name, "");
    _builder.append("\" enabled=\"true\">");
    _builder.newLineIfNotEmpty();
    {
      boolean _notEquals = (!Objects.equal(body, null));
      if (_notEquals) {
        _builder.append("  ");
        _builder.append("<boolProp name=\"HTTPSampler.postBodyRaw\">true</boolProp>");
        _builder.newLine();
      }
    }
    _builder.append("  ");
    _builder.append("<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<collectionProp name=\"Arguments.arguments\">");
    _builder.newLine();
    {
      boolean _notEquals_1 = (!Objects.equal(body, null));
      if (_notEquals_1) {
        _builder.append("      ");
        _builder.append("<elementProp name=\"\" elementType=\"HTTPArgument\">");
        _builder.newLine();
        _builder.append("      ");
        _builder.append("  ");
        _builder.append("<boolProp name=\"HTTPArgument.always_encode\">false</boolProp>");
        _builder.newLine();
        _builder.append("      ");
        _builder.append("  ");
        _builder.append("<stringProp name=\"Argument.value\">");
        _builder.append(body, "        ");
        _builder.append("</stringProp>");
        _builder.newLineIfNotEmpty();
        _builder.append("      ");
        _builder.append("  ");
        _builder.append("<stringProp name=\"Argument.metadata\">=</stringProp>");
        _builder.newLine();
        _builder.append("      ");
        _builder.append("</elementProp>");
        _builder.newLine();
      }
    }
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
    _builder.append("<stringProp name=\"HTTPSampler.path\">");
    _builder.append(path, "  ");
    _builder.append("</stringProp>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<stringProp name=\"HTTPSampler.method\">");
    _builder.append(method, "  ");
    _builder.append("</stringProp>");
    _builder.newLineIfNotEmpty();
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
    return _builder;
  }
  
  private CharSequence simpleController(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"");
    _builder.append(name, "");
    _builder.append("\" enabled=\"true\"/>");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  private CharSequence iterationScript() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<BeanShellSampler guiclass=\"BeanShellSamplerGui\" testclass=\"BeanShellSampler\" testname=\"Initialize\" enabled=\"true\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"BeanShellSampler.query\">vars.put(&quot;_ITERATION_ID&quot;, UUID.randomUUID().toString());");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"BeanShellSampler.filename\"></stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<stringProp name=\"BeanShellSampler.parameters\"></stringProp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<boolProp name=\"BeanShellSampler.resetInterpreter\">false</boolProp>");
    _builder.newLine();
    _builder.append("</BeanShellSampler>");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  public String content() {
    return this.provider.content();
  }
  
  @Override
  public int population() {
    return this.provider.population();
  }
  
  @Override
  public int thinkTime() {
    return this.provider.thinkTime();
  }
  
  @Override
  public String scenarioName() {
    return this.provider.scenarioName();
  }
}
